package com.su.tbk.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.security.core.authority.SimpleGrantedAuthority

// 在Test类中使用自定义的反序列化器
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Test @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("age") val age: Int,
    @JsonProperty("nums") val nums: List<SimpleGrantedAuthority>
)