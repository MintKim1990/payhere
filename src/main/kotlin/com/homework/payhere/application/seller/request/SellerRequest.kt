package com.homework.payhere.application.seller.request

import com.homework.payhere.domain.seller.Seller
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class SellerSignupRequest(
    @field:Pattern(regexp = "\\d{11}", message = "올바른 형식의 전화번호여야 합니다")
    val phoneNumber: String,
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String
) {
    fun toSeller(): Seller {
        return Seller(phoneNumber, password)
    }
}

data class SellerLoginRequest(
    @field:Pattern(regexp = "\\d{11}", message = "올바른 형식의 전화번호여야 합니다")
    val phoneNumber: String,
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String
)