package com.su.tbk.controller

import com.su.tbk.common.BaseResponse
import com.su.tbk.core.Slf4j
import com.su.tbk.core.Slf4j.Companion.log
import com.su.tbk.domain.dao.Users
import com.su.tbk.service.UsersService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@Tag(name = "测试")
class TestController {
    @Autowired
    lateinit var usersService: UsersService
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>
    @Resource(name = "objectRedisTemplate")
    lateinit var objectRedisTemplate: RedisTemplate<String, Any>
    @Operation(summary = "测试接口")
    @GetMapping("/hello")
    fun hello(): String {
        log.info("hello")
        val name=usersService.hello()
        val name2= redisTemplate.opsForValue()["username"]
        return "hello,I am $name, $name2!"
    }
    @PostMapping("/login")
    fun login(@RequestBody user:Users):BaseResponse{
        return usersService.login(user)
    }
    @PostMapping("/register")
    @Operation(summary = "注册接口")
    fun register(@RequestBody user:Users):BaseResponse{
        return usersService.register(user)
    }
}