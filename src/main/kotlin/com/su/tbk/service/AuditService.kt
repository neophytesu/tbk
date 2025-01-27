package com.su.tbk.service

import com.baomidou.mybatisplus.extension.service.IService
import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import com.su.tbk.domain.dao.Audit

/**
 * @author Lenovo
 * @description 针对表【audit】的数据库操作Service
 * @createDate 2025-01-26 17:00:03
 */
interface AuditService : IService<Audit?> {
    fun saveAuditLog(operation: OperationType, data: AuditDataType, scheme: String)
}
