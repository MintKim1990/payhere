package com.homework.payhere.controller.product

import com.fasterxml.jackson.core.type.TypeReference
import com.homework.payhere.application.product.response.ProductCreateResponse
import com.homework.payhere.application.seller.response.SellerSignupResponse
import com.homework.payhere.controller.ControllerTestHelper
import com.homework.payhere.controller.Response
import com.homework.payhere.domain.product.ProductNameInitialCharacterRepository
import com.homework.payhere.domain.product.ProductRepository
import com.homework.payhere.domain.product.ProductStatus
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.test.fixtures.createProductCreateRequest
import com.homework.payhere.test.fixtures.createProductDeleteRequest
import com.homework.payhere.test.fixtures.createProductSearchCursorRequest
import com.homework.payhere.test.fixtures.createSellerSignupRequest
import io.kotest.matchers.string.haveLength
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.net.http.HttpHeaders

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest : ControllerTestHelper() {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var productNameInitialCharacterRepository: ProductNameInitialCharacterRepository

    var token = ""

    @BeforeAll
    fun init() {
        val mvcResult = mockmvc.post("/seller") { jsonContent(createSellerSignupRequest()) }.andReturn()
        token = toResponse(
            mvcResult.response.contentAsString,
            object : TypeReference<Response<SellerSignupResponse>>() {}
        ).data!!.token
    }

    @BeforeEach
    internal fun setUp() {
        productRepository.deleteAll()
        productNameInitialCharacterRepository.deleteAll()
    }

    @Test
    fun `상품생성 요청 시 생성된 상품정보가 응답된다`() {

        val createProductCreateRequest = createProductCreateRequest()

        mockmvc.post("/product") {
            jsonContent(createProductCreateRequest)
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.id") { exists() }
            jsonPath("$.data.category") { value(createProductCreateRequest.category) }
            jsonPath("$.data.price") { value(createProductCreateRequest.price) }
            jsonPath("$.data.cost") { value(createProductCreateRequest.cost) }
            jsonPath("$.data.name") { value(createProductCreateRequest.name) }
            jsonPath("$.data.description") { value(createProductCreateRequest.description) }
            jsonPath("$.data.barcode") { value(createProductCreateRequest.barcode) }
            jsonPath("$.data.expirationDate") { value(createProductCreateRequest.expirationDate.toString()) }
            jsonPath("$.data.size") { value(createProductCreateRequest.size.toString()) }
            jsonPath("$.data.status") { value(ProductStatus.SALE.toString()) }
        }
    }

    @Test
    fun `상품생성 후 상품명으로 조회시 생성한 상품정보가 응답된다`() {

        val createProductCreateRequest = createProductCreateRequest(name = "아이스 아메리카노")

        mockmvc.post("/product") {
            jsonContent(createProductCreateRequest)
            bearer(token)
        }

        mockmvc.get("/product/search") {
            param("name", "아이스 아메리카노")
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data[0].name") { value("아이스 아메리카노") }
        }
    }

    @Test
    fun `상품생성 후 초성으로 조회시 생성한 상품정보가 응답된다`() {

        val createProductCreateRequest = createProductCreateRequest(name = "아이스 아메리카노")

        mockmvc.post("/product") {
            jsonContent(createProductCreateRequest)
            bearer(token)
        }

        mockmvc.get("/product/search") {
            param("name", "ㅇㅇㅅ")
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data[0].name") { value("아이스 아메리카노") }
        }
    }

    @Test
    fun `상품생성 후 상품 ID로 조회시 생성한 상품정보가 응답된다`() {

        val createProductCreateRequest = createProductCreateRequest()

        val mvcResult = mockmvc.post("/product") {
            jsonContent(createProductCreateRequest)
            bearer(token)
        }.andReturn()

        val createProductId = toResponse(
            mvcResult.response.contentAsString,
            object : TypeReference<Response<ProductCreateResponse>>() {}
        ).data!!.id

        mockmvc.get("/product/{id}", createProductId) {
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.id") { value(createProductId) }
        }
    }

    @Test
    fun `상품생성 후 삭제 시 해당 상품이 조회되면 안된다`() {

        val createProductCreateRequest = createProductCreateRequest(name = "아이스 아메리카노")

        val mvcResult = mockmvc.post("/product") {
            jsonContent(createProductCreateRequest)
            bearer(token)
        }.andReturn()

        val createProductId = toResponse(
            mvcResult.response.contentAsString,
            object : TypeReference<Response<ProductCreateResponse>>() {}
        ).data!!.id

        val productDeleteRequest = createProductDeleteRequest(createProductId)

        mockmvc.delete("/product") {
            jsonContent(productDeleteRequest)
            bearer(token)
        }

        mockmvc.get("/product/{id}", createProductId) {
            bearer(token)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.meta.code") { value(400) }
            jsonPath("$.meta.message") { value("상품이 존재하지 않습니다.") }
        }
    }

    @Test
    fun `여러 상품생성 후 커서기반 조회를 하면 다음 조회 키값과 기본 10개에 상품정보를 응답한다`() {

        mockmvc.post("/product") {
            jsonContent(createProductCreateRequest(name = "아이스 아메리카노1"))
            bearer(token)
        }
        mockmvc.post("/product") {
            jsonContent(createProductCreateRequest(name = "아이스 아메리카노2"))
            bearer(token)
        }

        val productSearchCursorRequest = createProductSearchCursorRequest()

        mockmvc.get("/product") {
            jsonContent(productSearchCursorRequest)
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.nextProductSearchCursorRequest.key") { value(1) }
            jsonPath("$.data") { haveLength(2) }
        }
    }

    @Test
    fun `상품 미등록 상태에서 커서기반 조회를 하면 키값은 -1을 리턴한다`() {
        val productSearchCursorRequest = createProductSearchCursorRequest()

        mockmvc.get("/product") {
            jsonContent(productSearchCursorRequest)
            bearer(token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.nextProductSearchCursorRequest.key") { value(-1) }
            jsonPath("$.data") { haveLength(0) }
        }.andDo { print() }
    }

}
