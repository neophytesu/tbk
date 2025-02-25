package com.su.tbk.controller

import cn.hutool.core.util.ZipUtil
import com.su.tbk.common.BaseResponse
import com.su.tbk.common.success
import com.su.tbk.service.SbomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/user/sbom")
class SbomController {
    @Autowired
    private lateinit var sbomService: SbomService

    @PostMapping("/upload/spdx")
    fun upload(@RequestParam("file") file: MultipartFile): BaseResponse {
        val unzipFile: File
        if (!file.isEmpty) {
            try {
                val bytes = file.bytes
                val path = Paths.get(System.getProperty("java.io.tmpdir"), file.originalFilename)
                Files.write(path, bytes)
                unzipFile = ZipUtil.unzip(File(System.getProperty("java.io.tmpdir")))
                return success(unzipFile)
            } catch (e: Exception) {
                return error("上传失败")
            }
        } else {
            return error("文件为空")
        }
    }

}