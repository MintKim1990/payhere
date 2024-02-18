package com.homework.payhere.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.homework.payhere.application.seller.response.SellerSignupResponse
import com.homework.payhere.test.config.EmbeddedRedisConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import kotlin.reflect.KClass

@SpringBootTest
@Import(EmbeddedRedisConfig::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ControllerTestHelper {

    @Autowired
    lateinit var mockmvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun MockHttpServletRequestDsl.jsonContent(value: Any) {
        content = toString(value)
        contentType = MediaType.APPLICATION_JSON
    }

    fun MockHttpServletRequestDsl.bearer(token: String) {
        header(HttpHeaders.AUTHORIZATION, bearerToken(token))
    }

    fun toString(value: Any): String {
        return objectMapper.writeValueAsString(value)
    }

    fun <T> toResponse(value: String, valueType: TypeReference<Response<T>>): Response<T> {
        return objectMapper.readValue(value, valueType)
    }

    private fun bearerToken(token: String): String = "Bearer $token"

}