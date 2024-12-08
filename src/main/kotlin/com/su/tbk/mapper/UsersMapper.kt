package com.su.tbk.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.su.tbk.domain.dao.Users

/**
 * @author Lenovo
 * @description 针对表【users】的数据库操作Mapper
 * @createDate 2024-12-05 14:57:12
 * @Entity com.su.tbk.domain.dao.Users
 */
interface UsersMapper : BaseMapper<Users?>{
    fun selectByUsername(username: String): String
}




