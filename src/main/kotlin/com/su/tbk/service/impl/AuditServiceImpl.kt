package com.su.tbk.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import com.su.tbk.core.AuthenticationHandler
import com.su.tbk.domain.dao.Audit
import com.su.tbk.mapper.AuditMapper
import com.su.tbk.service.AuditService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Lenovo
 * @description 针对表【audit】的数据库操作Service实现
 * @createDate 2025-01-26 17:00:03
 */
@Service
class AuditServiceImpl : ServiceImpl<AuditMapper?, Audit?>(), AuditService {
    @Autowired
    private lateinit var auditMapper: AuditMapper

    @Autowired
    private lateinit var authenticationHandler: AuthenticationHandler
    override fun saveAuditLog(operation: OperationType, data: AuditDataType, scheme: String) {
        val time = Date(System.currentTimeMillis())
        val audit = Audit(
            username = authenticationHandler.getCurrentUsername(),
            operate = operation,
            dataType = data,
            scheme = scheme,
            completeTime = time
        )
        if (operation in arrayOf(OperationType.CREATE, OperationType.UPDATE, OperationType.DELETE)) {
            auditMapper.insert(audit)
        }
    }
}




