package com.su.tbk.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseResponse (val code: Int, val message: String, val data: Any?)
fun success(data: Any? = null) = BaseResponse(200, "success", data)
fun fail(message: String) = BaseResponse(500, message, null)
fun error(message: String) = BaseResponse(400, message, null)
fun notFound(message: String) = BaseResponse(404, message, null)
fun unauthorized(message: String) = BaseResponse(401, message, null)
fun forbidden(message: String) = BaseResponse(403, message, null)