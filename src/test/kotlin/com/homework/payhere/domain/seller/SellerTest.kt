package com.homework.payhere.domain.seller

import com.homework.payhere.createSeller
import com.homework.payhere.utils.exception.LoginFailException
import com.homework.payhere.utils.exception.NotAvailablePhoneNumberException
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

class SellerTest : StringSpec({

    "휴대폰 번호에 숫자가 아닌 문자가 들어있을 경우 판매자 생성에 실패한다." {
        shouldThrow<NotAvailablePhoneNumberException> { createSeller(phoneNumber = "aaaaaaaaaaa") }
    }

    "휴대폰 번호에 숫자가 아닌 특수문자가 들어있을 경우 판매자 생성에 실패한다." {
        shouldThrow<NotAvailablePhoneNumberException> { createSeller(phoneNumber = "000-000-000") }
    }

    "휴대폰 번호가 11자리 미만일 경우 판매자 생성에 실패한다." {
        shouldThrow<NotAvailablePhoneNumberException> { createSeller(phoneNumber = "010") }
    }

    "휴대폰 번호가 11자리 초과일 경우 판매자 생성에 실패한다." {
        shouldThrow<NotAvailablePhoneNumberException> { createSeller(phoneNumber = "0101111222233") }
    }

    "사용자 패스워드가 맞을경우 로그인실패 에러가 발생하면 안된다." {
        val seller = createSeller()
        shouldNotThrow<LoginFailException> { seller.authenticate("1234") }
    }

    "사용자 패스워드가 틀릴경우 로그인실패 에러가 발생해야 한다." {
        val seller = createSeller()
        shouldThrow<LoginFailException> { seller.authenticate("4321") }
    }

})