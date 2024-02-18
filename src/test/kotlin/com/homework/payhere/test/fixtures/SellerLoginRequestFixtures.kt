package com.homework.payhere.test.fixtures

import com.homework.payhere.application.seller.request.SellerLoginRequest

private const val SELLER_PHONENUMBER: String = "01011112222"
private const val SELLER_PASSWORD: String = "1234"

fun createSellerLoginRequest(
    phoneNumber: String = SELLER_PHONENUMBER,
    password: String = SELLER_PASSWORD,
) = SellerLoginRequest(phoneNumber, password)