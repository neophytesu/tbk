package com.su.tbk.controller

import com.su.tbk.common.*
import com.su.tbk.service.ContractService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.error

@RestController
@RequestMapping("/user/didDocument")
@Tag(name = "did", description = "did文档在区块链上的存储与管理相关接口")
class DidDocumentController {
    @Autowired
    private lateinit var contractService: ContractService

    @PutMapping("/create")
    fun createDidDocument(@RequestBody request: CreateDidDocumentRequest): BaseResponse {
        return if (contractService.creatDidDocument(request)) success()
        else error("创建did文档失败")
    }

    @GetMapping("/get/{did}")
    fun getDidDocument(@PathVariable did: String): BaseResponse {
        return if (contractService.getDidDocument(did).isNullOrEmpty()) success()
        else error("获取did文档失败")
    }
    @DeleteMapping("/delete")
    fun deleteDidDocument(@RequestBody request: DeleteDidDocumentRequest): BaseResponse {
        return if (contractService.deleteDidDocument(request)) success()
        else error("删除did文档失败")
    }
    @PostMapping("/update")
    fun updateDidDocument(@RequestBody request: UpdateDidDocumentRequest): BaseResponse {
        return if (contractService.updateDidDocument(request)) success()
        else error("更新did文档失败")
    }
}