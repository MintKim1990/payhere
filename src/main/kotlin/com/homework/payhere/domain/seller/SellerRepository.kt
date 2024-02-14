package com.homework.payhere.domain.seller

import org.springframework.data.jpa.repository.JpaRepository

interface SellerRepository: JpaRepository<Seller, Long> {

    fun existsBySellerDetailPhoneNumber(phoneNumber: String): Boolean

    fun findBySellerDetailPhoneNumber(phoneNumber: String): Seller?

}