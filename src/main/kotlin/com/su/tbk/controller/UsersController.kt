package com.su.tbk.controller

import com.su.tbk.common.BaseResponse
import com.su.tbk.domain.dao.Users
import com.su.tbk.service.UsersService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "用户基本功能接口")
class UsersController {
    @Autowired
    lateinit var usersService: UsersService
    @PostMapping("/login")
    @Operation(summary = "登录接口")
    fun login(@RequestBody user: Users): BaseResponse {
        return usersService.login(user)
    }

    @PostMapping("/register")
    @Operation(summary = "注册接口")
    fun register(@RequestBody user: Users): BaseResponse {
        return usersService.register(user)
    }
}