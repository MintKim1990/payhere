package com.homework.payhere

import com.homework.payhere.domain.seller.Seller

private const val SELLER_PHONENUMBER: String = "01011112222"
private const val SELLER_PASSWORD: String = "1234"

fun createSeller(
    phoneNumber: String = SELLER_PHONENUMBER,
    password: String = SELLER_PASSWORD,
) = Seller(phoneNumber, password)