package com.homework.payhere.controller.seller

import com.homework.payhere.controller.ControllerTestHelper
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.test.fixtures.createSellerLoginRequest
import com.homework.payhere.test.fixtures.createSellerSignupRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class SellerControllerTest : ControllerTestHelper() {

    @Autowired
    lateinit var sellerRepository: SellerRepository

    @BeforeEach
    internal fun setUp() {
        sellerRepository.deleteAll()
    }

    @Test
    fun `회원가입요청 성공 시 토큰이 발급된다`() {

        val sellerSignupRequest = createSellerSignupRequest()

        mockmvc.post("/seller") {
            jsonContent(sellerSignupRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.token") { exists() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `로그인 성공 시 토큰이 발급된다`() {

        val sellerSignupRequest = createSellerSignupRequest()

        mockmvc.post("/seller") {
            jsonContent(sellerSignupRequest)
        }

        val sellerLoginRequest = createSellerLoginRequest()

        mockmvc.post("/seller/login") {
            content = objectMapper.writeValueAsString(sellerLoginRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.token") { exists() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `비정상적인 휴대폰 번호로 회원가입 요청 시 실패응답이 리턴된다`() {

        val sellerSignupRequest = createSellerSignupRequest(phoneNumber = "000")

        mockmvc.post("/seller") {
            content = objectMapper.writeValueAsString(sellerSignupRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.data.phoneNumber") { exists() }
        }.andDo {
            print()
        }
    }

    @Test
    fun `비정상적인 비밀번호로 회원가입 요청 시 실패응답이 리턴된다`() {

        val sellerSignupRequest = createSellerSignupRequest(password = "")

        mockmvc.post("/seller") {
            content = objectMapper.writeValueAsString(sellerSignupRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.data.password") { exists() }
        }.andDo {
            print()
        }
    }


}