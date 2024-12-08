package com.su.tbk.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@AutoConfigureMockMvc
@SpringBootTest
class TestControllerTest @Autowired constructor(private val mockMvc: MockMvc) {

    @Test
    @WithAnonymousUser
    fun hello() {
        mockMvc.perform(get("/user/hello"))
            .andExpect(status().isUnauthorized)
    }
    @Test
    @WithUserDetails
    fun hello2() {
        mockMvc.perform(get("/user/hello"))
            .andExpect(status().isOk)
    }
    @Test
    @WithUserDetails("admin")
    fun hello3() {
        mockMvc.perform(get("/user/hello"))
            .andExpect(status().isOk)
    }
}