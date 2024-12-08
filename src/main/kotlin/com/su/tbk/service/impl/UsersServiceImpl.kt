package com.su.tbk.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.su.tbk.common.BaseResponse
import com.su.tbk.common.error
import com.su.tbk.common.success
import com.su.tbk.config.UserDetailsImpl
import com.su.tbk.core.Slf4j
import com.su.tbk.core.Slf4j.Companion.log
import com.su.tbk.core.createJWT
import com.su.tbk.domain.dao.Users
import com.su.tbk.mapper.UsersMapper
import com.su.tbk.service.UsersService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Objects.isNull
import java.util.concurrent.TimeUnit

/**
 * @author Lenovo
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2024-12-05 14:57:12
 */
@Service
@Slf4j
class UsersServiceImpl : ServiceImpl<UsersMapper?, Users?>(), UsersService {
    @Autowired
    lateinit var usersMapper: UsersMapper

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var objectRedisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    override fun hello(): String {
        val users = usersMapper.selectByUsername("su")
        Slf4j.log.info("hello{}", users)
        val users2 = usersMapper.selectById(1)
        Slf4j.log.info("hello{}", users2)
        objectRedisTemplate.opsForValue()["hello"] = users2!!
        return usersMapper.selectById(1)?.username!!
    }

    override fun login(user: Users): BaseResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(user.username, user.password)
        val authenticate = authenticationManager.authenticate(authenticationToken)
        if (isNull(authenticate)) {
            return error("用户名或密码错误")
        }
        val users = authenticate.principal as UserDetailsImpl
        val jwt = createJWT(users.username)
        val redisKey = "login:${users.username}"
        objectRedisTemplate.opsForValue()[redisKey]=users
        objectRedisTemplate.expire(redisKey, 1, TimeUnit.HOURS)
        return success(data = mapOf("token" to jwt))
    }

    override fun register(user: Users): BaseResponse {
        val encodePassword = passwordEncoder.encode(user.password)
        val queryWrapper = QueryWrapper<Users>()
        if (usersMapper.selectOne(queryWrapper.eq("username", user.username)) != null) {
            return error("用户名已存在")
        }
        if (usersMapper.insert(
                Users(
                    username = user.username,
                    password = encodePassword,
                    role = arrayOf("ROlE_USER")
                )
            ) > 0
        ) {
            return success()
        }
        return error("注册失败")
    }
}




