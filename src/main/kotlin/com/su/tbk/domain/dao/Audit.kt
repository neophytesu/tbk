package com.su.tbk.domain.dao

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.su.tbk.common.AuditDataType
import com.su.tbk.common.OperationType
import com.su.tbk.core.AuditDataTypeHandler
import com.su.tbk.core.OperateTypeHandler
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.util.*

/**
 *
 * @TableName audit
 */
@TableName(value = "audit", autoResultMap = true)
@Schema(description = "审计日志")
data class Audit(
    @Schema(description = "主键")
    @TableId
    private val id: Int? = null,

    @Schema(description = "用户名")
    private val username: String? = null,

    @Schema(description = "操作完成时间")
    private val completeTime: Date? = null,

    @Schema(description = "数据表名")
    private val scheme: String? = null,
    @Schema(description = "数据索引ID")
    private val index: String? = null,
    @Schema(description = "数据类型")
    @TableField(typeHandler = AuditDataTypeHandler::class)
    private val dataType: AuditDataType? = null,
    @Schema(description = "操作类型")
    @TableField(typeHandler = OperateTypeHandler::class)
    private val operate: OperationType? = null
) : Serializable {
    companion object {
        @TableField(exist = false)
        private const val serialVersionUID = 1L
    }
}