package com.su.tbk.domain.dao

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.su.tbk.core.StringTypeHandler
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

/**
 *
 * @TableName users
 */
@TableName(value = "users", autoResultMap = true)
@Schema(description = "用户实体")
data class Users(
    @Schema(description = "用户id")
    @TableId
    val id: Int? = null,
    @Schema(description = "用户名")
    val username: String? = null,
    @Schema(description = "密码")
    val password: String? = null,
    @Schema(description = "角色列表")
    @TableField(typeHandler = StringTypeHandler::class)
    val role: Array<String>? = null
) : Serializable {
    companion object {
        @TableField(exist = false)
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Users

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (role != null) {
            if (other.role == null) return false
            if (!role.contentEquals(other.role)) return false
        } else if (other.role != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (role?.contentHashCode() ?: 0)
        return result
    }
}
