package com.homework.payhere.domain.seller

import com.homework.payhere.utils.encrypt.sha256Encrypt
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Password(
    password: String
) {

    @Column(name = "password", nullable = false)
    var value: String = sha256Encrypt(password)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Password) return false
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}