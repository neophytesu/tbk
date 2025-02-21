package com.su.tbk.controller

import com.su.tbk.common.BaseResponse
import com.su.tbk.common.success
import com.su.tbk.common.error
import com.su.tbk.service.DidService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Resource
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/did")
@Tag(name = "did", description = "did相关接口")
class DidController {
    @Resource
    private lateinit var didService: DidService

    @Operation(summary = "创建秘钥", description = "创建DID的公私钥对，并返回十六进制编码的公钥和私钥")
    @GetMapping("/createKey")
    fun createKey(): BaseResponse {
        return success(didService.createKey())
    }

    @Operation(summary = "获取密钥列表", description = "获取当前用户的所有密钥列表")
    @GetMapping("/getKeyList")
    fun getKeyList(): BaseResponse {
        return success(didService.getKeyList())
    }

    @Operation(summary = "删除密钥", description = "删除指定DID的公私钥对")
    @DeleteMapping("/removeKey/{privateKey}")
    fun removeKey(@PathVariable privateKey: String): BaseResponse {
        if (didService.removeKey(privateKey)) {
            return success()
        }
        return error("删除失败")
    }

    @Operation(summary = "创建DID", description = "根据十六进制主备公钥生成DID")
    @GetMapping("/createDid/{publicKey1}/{publicKey2}")
    fun createDid(@PathVariable publicKey1: String, @PathVariable publicKey2: String): BaseResponse {
        val did = didService.createDid(publicKey1, publicKey2)
        return success(did)
    }
}