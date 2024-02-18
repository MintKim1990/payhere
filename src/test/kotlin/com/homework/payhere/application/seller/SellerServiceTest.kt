package com.homework.payhere.application.seller

import com.homework.payhere.test.config.EmbeddedRedisConfig
import com.homework.payhere.application.seller.request.SellerLoginRequest
import com.homework.payhere.application.seller.request.SellerSignupRequest
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.test.fixtures.createSellerLoginRequest
import com.homework.payhere.test.fixtures.createSellerSignupRequest
import com.homework.payhere.test.utils.beforeRootTest
import com.homework.payhere.utils.exception.LoginFailException
import com.homework.payhere.utils.exception.SellerNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.isRootTest
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Import(EmbeddedRedisConfig::class)
@ActiveProfiles("test")
class SellerServiceTest(
    private val sellerService: SellerService,
    private val redisTemplate: RedisTemplate<String, String>,
    private val sellerRepository: SellerRepository,
) : BehaviorSpec({

    beforeRootTest {
        redisTemplate.connectionFactory?.let {
            it.connection.serverCommands().flushAll()
        }
        sellerRepository.deleteAll()
    }

    Given("가입요청 데이터 생성 후") {

        val sellerSignupRequest = createSellerSignupRequest()

        When("회원가입 요청 시") {

            val token = sellerService.signup(sellerSignupRequest).token

            Then("가입이 완료되고 토큰이 발급된다.") {
                token.shouldNotBeNull()
            }
        }
    }

    Given("회원가입 완료 후") {

        val sellerSignupRequest = createSellerSignupRequest()
        sellerService.signup(sellerSignupRequest)

        When("중복가입 요청 시") {

            val sellerSignupRequest = createSellerSignupRequest()

            Then("IllegalStateException이 발생한다.") {
                shouldThrow<IllegalStateException> {
                    sellerService.signup(sellerSignupRequest)
                }
            }
        }

        When("로그인 요청 시") {

            val sellerLoginRequest = createSellerLoginRequest()
            val token = sellerService.login(sellerLoginRequest).token

            Then("토큰이 발급된다.") {
                token.shouldNotBeNull()
            }
        }

        When("로그인 요청 시 패스워드가 틀린경우") {

            val sellerLoginRequest = createSellerLoginRequest(password = "111")

            Then("LoginFailException이 발생한다.") {
                shouldThrow<LoginFailException> {
                    sellerService.login(sellerLoginRequest)
                }
            }
        }

        When("가입되있지 않은 판매자 로그인 요청 시") {

            val sellerLoginRequest = createSellerLoginRequest(phoneNumber = "010")

            Then("SellerNotFoundException이 발생한다.") {
                shouldThrow<SellerNotFoundException> {
                    sellerService.login(sellerLoginRequest)
                }
            }
        }
    }

})