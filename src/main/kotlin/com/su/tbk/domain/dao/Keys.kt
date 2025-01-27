package com.su.tbk.domain.dao

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.io.Serializable

/**
 *
 * @TableName keys
 */
@TableName(value = "keys", autoResultMap = true)
@Schema(description = "用户的公钥私钥表")
data class Keys(
    @Schema(description = "主键")
    @TableId
    val id: Int? = null,

    @Schema(description = "用户名")
    val username: String? = null,

    @Schema(description = "公钥")
    val publicKey: String? = null,

    @Schema(description = "私钥")
    val privateKey: String? = null
) : Serializable {
    companion object {
        @TableField(exist = false)
        private const val serialVersionUID = 1L
    }
}