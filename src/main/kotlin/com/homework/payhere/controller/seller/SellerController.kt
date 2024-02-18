package com.homework.payhere.controller.seller

import com.homework.payhere.application.seller.SellerService
import com.homework.payhere.application.seller.request.SellerLoginRequest
import com.homework.payhere.application.seller.request.SellerSignupRequest
import com.homework.payhere.application.seller.response.SellerLoginResponse
import com.homework.payhere.application.seller.response.SellerSignupResponse
import com.homework.payhere.controller.Response
import com.homework.payhere.utils.security.Token
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/seller")
class SellerController(
    private val sellerService: SellerService
) {

    @PostMapping
    fun singup(
        @RequestBody @Validated sellerSignupRequest: SellerSignupRequest
    ): ResponseEntity<Response<SellerSignupResponse>> {
        val sellerSignupResponse = sellerService.signup(sellerSignupRequest)
        return ResponseEntity.ok(Response(sellerSignupResponse))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Validated sellerLoginRequest: SellerLoginRequest
    ): ResponseEntity<Response<SellerLoginResponse>> {
        val sellerLoginResponse = sellerService.login(sellerLoginRequest)
        return ResponseEntity.ok(Response(sellerLoginResponse))
    }

    @PostMapping("/logout")
    fun logout(
        @Token token: String
    ): ResponseEntity<Response<Nothing>> {
        sellerService.logout(token)
        return ResponseEntity.ok(Response())
    }

}