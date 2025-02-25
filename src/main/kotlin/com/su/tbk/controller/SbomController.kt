package com.su.tbk.controller

import cn.hutool.core.util.ZipUtil
import com.su.tbk.common.BaseResponse
import com.su.tbk.common.success
import com.su.tbk.service.SbomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/user/sbom")
class SbomController {
    @Autowired
    private lateinit var sbomService: SbomService

    @PostMapping("/upload/{type}")
    fun uploadSpdx(@RequestParam("file") file: MultipartFile, @PathVariable("type") type: String): BaseResponse {
        val unzipFile: File
        val out: File
        if (!file.isEmpty) {
            try {
                val bytes = file.bytes
                val path = Paths.get(System.getProperty("java.io.tmpdir"), file.originalFilename)
                Files.write(path, bytes)
                unzipFile = ZipUtil.unzip(File(System.getProperty("java.io.tmpdir")))
                out = when (type) {
                    "spdx" -> {
                        sbomService.generateSbom(unzipFile,"spdx-json")
                    }
                    "cdx" -> {
                        sbomService.generateSbom(unzipFile,"cyclonedx")
                    }
                    else -> {
                        throw IllegalArgumentException("类型错误")
                    }
                }
            } catch (e: Exception) {
                return error("上传失败")
            }
        } else {
            return error("文件为空")
        }
        return success(out)
    }
}