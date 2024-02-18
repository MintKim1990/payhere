package com.homework.payhere.test.fixtures

import com.homework.payhere.application.product.request.ProductCreateRequest
import com.homework.payhere.domain.product.Size
import java.time.LocalDate
import java.util.*

private const val CATEGORY: String = "커피"
private const val PRICE: Int = 20000
private const val COST: Int = 10000
private const val NAME: String = "아이스 아메리카노"
private const val DESCRIPTION: String = "아이스 아메리카노입니다."
private val EXPIRATION_DATE: LocalDate = LocalDate.of(2024, 3, 30)
private val SIZE: Size = Size.SMALL

fun createProductCreateRequest(
    category: String = CATEGORY,
    price: Int = PRICE,
    cost: Int = COST,
    name: String = NAME,
    description: String = DESCRIPTION,
    barcode: String = UUID.randomUUID().toString(),
    expirationDate: LocalDate = EXPIRATION_DATE,
    size: Size = SIZE
) = ProductCreateRequest(category, price, cost, name, description, barcode, expirationDate, size)