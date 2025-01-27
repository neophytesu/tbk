package com.su.tbk.config

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.fasterxml.jackson.annotation.*
import com.su.tbk.core.parseJWT
import com.su.tbk.domain.dao.Users
import com.su.tbk.domain.dto.UserDetailDTO
import com.su.tbk.mapper.UsersMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Objects.isNull

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {
    @Autowired
    private lateinit var jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/user/**")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .anonymous()
            }.httpBasic {}.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.rememberMe {
                Customizer.withDefaults<Int>()
            }
        return http.build()
    }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.debug(false)
                .ignoring()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico/**")
        }
    }
}

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private lateinit var usersMapper: UsersMapper
    override fun loadUserByUsername(username: String?): UserDetails {
        val queryWrapper = QueryWrapper<Users>()
        queryWrapper.eq("username", username)
        val user = usersMapper.selectOne(queryWrapper) ?: throw UsernameNotFoundException(username)
        val roles = user.role!!.toList()
        val authorities = roles.map { SimpleGrantedAuthority(it) }.toMutableList()
        return UserDetailsImpl(user, authorities)
    }
}

class UserDetailsImpl @JsonCreator constructor(
    private val username: String?,
    private val password: String?,
    private val authorities: List<SimpleGrantedAuthority> = ArrayList()
) : UserDetails {

    constructor(user: Users, authorities: List<SimpleGrantedAuthority>) : this(
        user.username,
        user.password,
        authorities
    )

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return username!!
    }
}

@Component
class JwtAuthenticationTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var objectRedisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var usersMapper: UsersMapper
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response)
            return
        }
        val claims = parseJWT(token)
        val username = claims.subject
        val userDetailDTO = objectRedisTemplate.opsForValue()["login:$username"] as UserDetailDTO
        if (isNull(userDetailDTO)) {
            throw RuntimeException("用户未登录")
        }
        val roles = userDetailDTO.authorities.map { SimpleGrantedAuthority(it) }.toMutableList()
        val userDetailsImpl = UserDetailsImpl(userDetailDTO.username, userDetailDTO.password, roles)
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetailsImpl, null, roles)
        SecurityContextHolder.getContext().authentication = authenticationToken
        filterChain.doFilter(request, response)
    }
}