package com.homework.payhere.domain.seller

import com.homework.payhere.utils.exception.NotAvailablePhoneNumberException
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class SellerDetail(
    phoneNumber: String
) {

    @Column(name = "phone_number", nullable = false, length = 11)
    var phoneNumber: String = validatePhoneNumber(phoneNumber)
        private set

    private fun validatePhoneNumber(phoneNumber: String): String {
        val regex = Regex("^\\d{11}\$")
        if (!regex.matches(phoneNumber)) {
            throw NotAvailablePhoneNumberException()
        }
        return phoneNumber
    }

}