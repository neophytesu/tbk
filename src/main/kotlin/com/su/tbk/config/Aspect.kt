package com.su.tbk.config

import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import com.su.tbk.service.AuditService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuditLog(val operation: OperationType, val data: AuditDataType, val scheme: String)

@Aspect
@Component
class AuditLogAspect {
    @Autowired
    private lateinit var auditService: AuditService

    @Pointcut("@annotation(AuditLog)")
    fun auditLogMethods() {
        // Pointcut for methods annotated with @AuditLog
    }

    @After("auditLogMethods()&&@annotation(auditLog)")
    fun afterAuditLogMethods(joinPoint: JoinPoint, auditLog: AuditLog) {
        auditService.saveAuditLog(auditLog.operation, auditLog.data, auditLog.scheme)
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAOP

@Aspect
@Component
class TestAOPAspect {
    @Pointcut("@annotation(TestAOP)")
    fun testAOPMethods() {
        // Pointcut for methods annotated with @TestAOP}
    }

    @After("testAOPMethods()")
    fun beforeTestAOPMethods() {
        println("Test AOP Method")
    }
}

