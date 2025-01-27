package com.su.tbk.service.impl

import com.su.tbk.core.generateHexKeys
import com.su.tbk.core.generateKeys
import com.su.tbk.service.DidService
import com.su.tbk.service.KeysService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DidServiceImpl : DidService {
    @Autowired
    private lateinit var keysService: KeysService
    override fun createKey(): Pair<String, String> {
        val keyPair = generateKeys()
        val hexKey = generateHexKeys(keyPair)
        keysService.saveKey(hexKey)
        return hexKey
    }

    override fun getKeyList(): List<Pair<String, String>> {
        val keyList = keysService.getKeyList()
        return keyList
    }

    override fun removeKey(privateKey: String): Boolean {
        return keysService.removeKey(privateKey)
    }
}