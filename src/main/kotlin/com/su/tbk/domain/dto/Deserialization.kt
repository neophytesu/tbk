package com.su.tbk.domain.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class UserDetailDTO @JsonCreator constructor(
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("authorities") val authorities: List<String>
)