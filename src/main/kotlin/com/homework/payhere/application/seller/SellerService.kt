package com.homework.payhere.application.seller

import com.homework.payhere.application.seller.request.SellerLoginRequest
import com.homework.payhere.application.seller.request.SellerSignupRequest
import com.homework.payhere.application.seller.response.SellerLoginResponse
import com.homework.payhere.application.seller.response.SellerSignupResponse
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.utils.exception.SellerNotFoundException
import com.homework.payhere.utils.security.JwtTokenProvider
import com.homework.payhere.utils.security.UserSessionManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Transactional(readOnly = true)
@Service
class SellerService(
    private val sellerRepository: SellerRepository,
    private val userSessionManager: UserSessionManager,
) {

    @Transactional
    fun signup(sellerSignupRequest: SellerSignupRequest): SellerSignupResponse {
        check(!sellerRepository.existsBySellerDetailPhoneNumber(sellerSignupRequest.phoneNumber)) {
            "이미 가입된 판매자입니다."
        }
        val seller = sellerRepository.save(sellerSignupRequest.toSeller())
        val token = userSessionManager.login(seller)
        return SellerSignupResponse(token)
    }

    fun login(sellerLoginRequest: SellerLoginRequest): SellerLoginResponse {
        val seller = sellerRepository.findBySellerDetailPhoneNumber(sellerLoginRequest.phoneNumber)
            ?: throw SellerNotFoundException()

        seller.authenticate(sellerLoginRequest.password)
        val token = userSessionManager.login(seller)
        return SellerLoginResponse(token)
    }

    fun logout(token: String) {
        userSessionManager.logout(token)
    }

}