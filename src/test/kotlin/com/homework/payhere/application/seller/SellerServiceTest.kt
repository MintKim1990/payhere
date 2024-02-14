package com.homework.payhere.application.seller

import com.homework.payhere.application.seller.request.SellerSignupRequest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SellerServiceTest(
    private val sellerService: SellerService
) : BehaviorSpec({

    Given("회원가입 요청시") {

        val sellerSignupRequest = SellerSignupRequest("01011112222", "1234")

        When("정상가입되면") {

            val token = sellerService.signup(sellerSignupRequest)

            Then("토큰이 발급된다.") {
                token.shouldNotBeNull()
            }
        }
    }

})