package com.homework.payhere.utils.security

import com.homework.payhere.createSeller
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.utils.exception.SellerNotFoundException
import io.kotest.assertions.any
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

class UserSessionManagerTest : BehaviorSpec({

    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val redisTemplate = mockk<RedisTemplate<String, String>>()
    val valueOperations = mockk<ValueOperations<String, String>>()
    val sellerRepository = mockk<SellerRepository>()
    val userSessionManager = UserSessionManager(jwtTokenProvider, redisTemplate, sellerRepository)

    Given("판매자 생성 후") {

        val seller = createSeller()

        When("로그인시") {

            every { jwtTokenProvider.createToken(any(), any(), any()) } answers { "token" }
            every { redisTemplate.opsForValue() } answers { valueOperations }
            every { valueOperations.set(any(), any(), any(), any()) } answers { }

            val token = userSessionManager.login(seller)

            Then("토큰이 생성되고 레디스에 저장한다.") {
                verify { valueOperations.set(any(), any(), any(), any()) }
                token shouldBe "token"
            }
        }

        When("로그아웃 시") {

            every { redisTemplate.delete("token") } answers { true }

            userSessionManager.logout("token")

            Then("레디스에 토큰정보가 삭제되야 한다.") {
                verify { redisTemplate.delete("token") }
            }
        }

        When("토큰이 유효하지 않은 경우") {

            every { jwtTokenProvider.isValidToken("token") } answers { false }
            every { redisTemplate.opsForValue() } answers { valueOperations }
            every { valueOperations.get(any()) } answers { "redisToken" }

            val isAvailable = userSessionManager.isAvailableToken("token")

            Then("토큰 유효성 검사에서 false가 리턴된다.") {
                isAvailable shouldBe false
            }
        }

        When("레디스에 토큰이 등록되지 않은 경우") {

            every { jwtTokenProvider.isValidToken("token") } answers { true }
            every { redisTemplate.opsForValue() } answers { valueOperations }
            every { valueOperations.get(any()) } answers { null }

            val isAvailable = userSessionManager.isAvailableToken("token")

            Then("토큰 유효성 검사에서 false가 리턴된다.") {
                isAvailable shouldBe false
            }
        }

        When("토큰정보로 판매자 정보 조회 시 DB에 등록되있다면") {

            every { jwtTokenProvider.getSubject(any()) } answers { "01011112222" }
            every { sellerRepository.findBySellerDetailPhoneNumber("01011112222") } answers { seller }

            Then("판매자 정보가 조회된다.") {
                val findSeller = userSessionManager.findSellerByToken("token")
                findSeller shouldBe seller
            }
        }

        When("토큰정보로 판매자 정보 조회 시 DB에 등록되있지 않다면") {

            every { jwtTokenProvider.getSubject(any()) } answers { "01011112222" }
            every { sellerRepository.findBySellerDetailPhoneNumber("01011112222") } answers { null }

            Then("SellerNotFoundException이 발생한다.") {
                shouldThrow<SellerNotFoundException> {
                    userSessionManager.findSellerByToken("token")
                }
            }
        }


    }
})
