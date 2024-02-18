package com.homework.payhere.test.fixtures

import com.homework.payhere.application.product.request.ProductUpdateRequest
import com.homework.payhere.application.product.response.ProductCreateResponse
import java.time.LocalDate

fun createProductNameUpdateRequest(
    name: String,
    productCreateResponse: ProductCreateResponse,
) = ProductUpdateRequest(
    id = productCreateResponse.id,
    category = productCreateResponse.category,
    price = productCreateResponse.price,
    cost = productCreateResponse.cost,
    name = name,
    description = productCreateResponse.description,
    barcode = productCreateResponse.barcode,
    expirationDate = productCreateResponse.expirationDate,
    size = productCreateResponse.size,
    status = productCreateResponse.status
)

fun createProductNameUpdateRequest(
    expirationDate: LocalDate,
    productCreateResponse: ProductCreateResponse,
) = ProductUpdateRequest(
    id = productCreateResponse.id,
    category = productCreateResponse.category,
    price = productCreateResponse.price,
    cost = productCreateResponse.cost,
    name = productCreateResponse.name,
    description = productCreateResponse.description,
    barcode = productCreateResponse.barcode,
    expirationDate = expirationDate,
    size = productCreateResponse.size,
    status = productCreateResponse.status
)