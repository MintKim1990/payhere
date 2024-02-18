package com.homework.payhere.test.fixtures

import com.homework.payhere.application.seller.request.SellerSignupRequest

private const val SELLER_PHONENUMBER: String = "01011112222"
private const val SELLER_PASSWORD: String = "1234"

fun createSellerSignupRequest(
    phoneNumber: String = SELLER_PHONENUMBER,
    password: String = SELLER_PASSWORD,
) = SellerSignupRequest(phoneNumber, password)