package com.su.tbk.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.su.tbk.core.AuthenticationHandler
import com.su.tbk.domain.dao.Keys
import com.su.tbk.mapper.KeysMapper
import com.su.tbk.service.KeysService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Lenovo
 * @description 针对表【keys】的数据库操作Service实现
 * @createDate 2025-01-26 15:21:09
 */
@Service
class KeysServiceImpl : ServiceImpl<KeysMapper?, Keys?>(), KeysService {
    @Autowired
    private lateinit var keysMapper: KeysMapper

    @Autowired
    lateinit var authenticationHandler: AuthenticationHandler
    override fun saveKey(keyPair: Pair<String, String>): Boolean {
        val publicKey = keyPair.first
        val privateKey = keyPair.second
        return keysMapper.insert(
            Keys(
                publicKey = publicKey,
                privateKey = privateKey,
                username = authenticationHandler.getCurrentUsername()
            )
        ) > 0
    }

    override fun getKeyList(): List<Pair<String, String>> {
        val username = authenticationHandler.getCurrentUsername()
        val queryWrapper = QueryWrapper<Keys>()
        queryWrapper.eq("username", username)
        val keysList = keysMapper.selectList(queryWrapper)
            .filterNotNull()
            .filter { it.publicKey != null && it.privateKey != null }
            .mapNotNull {
                it.publicKey?.let { publicKey ->
                    it.privateKey?.let { privateKey ->
                        Pair(publicKey, privateKey)
                    }
                }
            }
        return keysList
    }

    override fun removeKey(privateKey: String): Boolean {
        val username = authenticationHandler.getCurrentUsername()
        val queryWrapper = QueryWrapper<Keys>()
        queryWrapper.eq("username", username)
        queryWrapper.eq("private_key", privateKey)
        return keysMapper.delete(queryWrapper) > 0
    }
}




