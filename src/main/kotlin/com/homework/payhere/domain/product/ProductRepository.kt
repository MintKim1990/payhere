package com.homework.payhere.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {

    fun findByIdAndSellerId(id: Long, sellerId: Long): Product?

    fun findAllBySellerIdAndNameContaining(sellerId: Long, name: String): List<Product>

    @Query("""
        SELECT p
          FROM Product p
         WHERE p.sellerId = :sellerId
      ORDER BY p.id desc
         LIMIT :size
    """)
    fun findAllBySellerIdAndCursor(sellerId: Long, size: Int): List<Product>

    @Query("""
        SELECT p
          FROM Product p
         WHERE p.sellerId = :sellerId 
           AND p.id < :id
      ORDER BY p.id desc
         LIMIT :size
    """)
    fun findAllBySellerIdAndCursor(sellerId: Long, id: Long, size: Int): List<Product>

}