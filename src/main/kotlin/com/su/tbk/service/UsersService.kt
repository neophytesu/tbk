package com.su.tbk.service

import com.baomidou.mybatisplus.extension.service.IService
import com.su.tbk.common.BaseResponse
import com.su.tbk.domain.dao.Users

/**
 * @author Lenovo
 * @description 针对表【users】的数据库操作Service
 * @createDate 2024-12-05 14:57:12
 */
interface UsersService : IService<Users?>{
    fun hello(): String
    fun login(user: Users): BaseResponse
    fun register(user: Users): BaseResponse
}
