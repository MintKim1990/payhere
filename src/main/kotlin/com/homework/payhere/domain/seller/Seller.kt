package com.homework.payhere.domain.seller

import com.homework.payhere.domain.BaseEntity
import com.homework.payhere.utils.exception.LoginFailException
import jakarta.persistence.*

@Entity
class Seller(

    @Embedded
    val sellerDetail: SellerDetail,

    @Embedded
    val password: Password,

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val phoneNumber: String
        get() = sellerDetail.phoneNumber


    constructor(
        phoneNumber: String,
        password: String
    ) : this(SellerDetail(phoneNumber), Password(password))

    fun authenticate(password: String) {
        if (this.password != Password(password)) {
            throw LoginFailException()
        }
    }


}