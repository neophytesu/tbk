package com.su.tbk.service

import com.baomidou.mybatisplus.extension.service.IService
import com.su.tbk.domain.dao.Keys
import java.security.KeyPair

/**
 * @author Lenovo
 * @description 针对表【keys】的数据库操作Service
 * @createDate 2025-01-26 15:21:09
 */
interface KeysService : IService<Keys?>{
    fun saveKey(keyPair:Pair<String,String>):Boolean
    fun getKeyList(): List<Pair<String, String>>
    fun removeKey(privateKey: String): Boolean
}
