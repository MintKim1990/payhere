package com.homework.payhere.utils.exception

class LoginFailException(override val message: String = "로그인 정보가 정확하지 않습니다.") : RuntimeException(message)
class NotAvailablePhoneNumberException(override val message: String = "휴대폰번호가 유효하지 않습니다.") : RuntimeException(message)
class SellerNotFoundException(override val message: String = "판매자가 존재하지 않습니다.") : RuntimeException(message)