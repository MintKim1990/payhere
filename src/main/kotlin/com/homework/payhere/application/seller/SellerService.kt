package com.homework.payhere.application.seller

import com.homework.payhere.application.seller.request.SellerLoginRequest
import com.homework.payhere.application.seller.request.SellerSignupRequest
import com.homework.payhere.application.seller.response.SellerLoginResponse
import com.homework.payhere.application.seller.response.SellerSignupResponse
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.utils.exception.SellerNotFoundException
import com.homework.payhere.utils.security.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class SellerService(
    private val sellerRepository: SellerRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    fun signup(sellerSignupRequest: SellerSignupRequest): SellerSignupResponse {
        check(!sellerRepository.existsBySellerDetailPhoneNumber(sellerSignupRequest.phoneNumber)) {
            "이미 가입된 판매자입니다."
        }
        val seller = sellerRepository.save(sellerSignupRequest.toSeller())
        val token = jwtTokenProvider.createToken(seller.phoneNumber)
        return SellerSignupResponse(token)
    }

    fun login(sellerLoginRequest: SellerLoginRequest): SellerLoginResponse {
        val seller = sellerRepository.findBySellerDetailPhoneNumber(sellerLoginRequest.phoneNumber)
            ?: throw SellerNotFoundException()

        seller.authenticate(sellerLoginRequest.password)
        val token = jwtTokenProvider.createToken(seller.phoneNumber)
        return SellerLoginResponse(token)
    }

}