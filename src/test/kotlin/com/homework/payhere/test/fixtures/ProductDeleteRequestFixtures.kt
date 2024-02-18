package com.homework.payhere.test.fixtures

import com.homework.payhere.application.product.request.ProductDeleteRequest

fun createProductDeleteRequest(
    id: Long,
) = ProductDeleteRequest(id)