package com.su.tbk.controller

import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import com.su.tbk.config.AuditLog
import com.su.tbk.config.TestAOP
import com.su.tbk.core.Slf4j
import com.su.tbk.mapper.AuditMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@Tag(name = "测试")
@RequestMapping("/user")
class TestController {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Resource(name = "objectRedisTemplate")
    lateinit var objectRedisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var auditMapper: AuditMapper

    @Operation(summary = "测试接口")
    @GetMapping("/hello")
    @AuditLog(operation = OperationType.CREATE, data = AuditDataType.DID, scheme = "su")
    @TestAOP
    fun hello(): Boolean {
        return true
    }

}
