package com.homework.payhere.utils.security

import com.homework.payhere.domain.seller.Seller
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.utils.exception.SellerNotFoundException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class UserSessionManager(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, String>,
    private val sellerRepository: SellerRepository,
) {

    private val TIME_OUT: Long = 12
    private val TIME_UNIT: TimeUnit = TimeUnit.HOURS

    fun login(seller: Seller): String {
        return jwtTokenProvider.createToken(seller.phoneNumber, TIME_OUT, TIME_UNIT).also {
            token -> redisTemplate.opsForValue().set(token, seller.phoneNumber, TIME_OUT, TIME_UNIT)
        }
    }

    fun logout(token: String): Boolean {
        return redisTemplate.delete(token)
    }

    fun findSellerByToken(token: String): Seller {
        val sellerPhoneNumber = jwtTokenProvider.getSubject(token)
        return sellerRepository.findBySellerDetailPhoneNumber(sellerPhoneNumber) ?: throw SellerNotFoundException()
    }

    fun isAvailableToken(token: String): Boolean {
        return jwtTokenProvider.isValidToken(token) && redisTemplate.opsForValue().get(token) != null
    }

}