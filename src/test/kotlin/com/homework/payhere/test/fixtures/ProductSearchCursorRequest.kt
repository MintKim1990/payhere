package com.homework.payhere.test.fixtures

import com.homework.payhere.application.product.request.ProductSearchCursorRequest

fun createProductSearchCursorRequest(
    key: Long? = null,
    size: Int = 10
) = ProductSearchCursorRequest(key, size)