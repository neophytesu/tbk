package com.su.tbk.service.impl


import com.su.tbk.core.Slf4j
import com.su.tbk.core.Slf4j.Companion.log
import com.su.tbk.core.sbomResolve
import com.su.tbk.service.SbomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.log

@Service
@EnableScheduling
@Slf4j
class SbomServiceImpl : SbomService {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>
    override fun generateSbom(file: File, type: String): File {
        val newFileName = "${System.getProperty("user.dir")}\\src\\resources\\output\\${file.name}}.json"
        if (redisTemplate.opsForValue()[newFileName] != null) {
            return File(newFileName)
        }
        redisTemplate.opsForValue()[newFileName] = ""
        redisTemplate.expire(newFileName, 5, TimeUnit.MINUTES)
        redisTemplate.opsForSet().add("sbom", newFileName)
        val out = sbomResolve(file, type, newFileName)
        return out
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    fun clearRedisCache() {
        redisTemplate.opsForSet().members("sbom")?.forEach {
            if (redisTemplate.opsForValue()[it] == null) {
                redisTemplate.opsForSet().remove("sbom", it)
                val file = File(it)
                if (file.exists()) {
                    val deleted = file.delete()
                    if (!deleted) {
                        log.log.warn("删除${it}失败")
                    }
                }
            }
        }
    }
}