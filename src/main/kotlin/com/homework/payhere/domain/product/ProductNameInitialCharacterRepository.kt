package com.homework.payhere.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductNameInitialCharacterRepository : JpaRepository<ProductNameInitialCharacter, Long> {

    @Query("""
        SELECT p
          FROM ProductNameInitialCharacter p
    JOIN FETCH p.product
         WHERE p.sellerId = :sellerId
           AND p.initialCharacter = :initialCharacter
    """)
    fun findAllBySellerIdAndInitialCharacter(sellerId: Long, initialCharacter: String): List<ProductNameInitialCharacter>

}