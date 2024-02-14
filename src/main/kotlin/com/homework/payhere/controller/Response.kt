package com.homework.payhere.controller

import org.springframework.http.HttpStatus

data class Response<T>(
    val meta: Meta,
    val data: T? = null
) {
    constructor(data: T?): this(Meta(), data)
    constructor(status: HttpStatus, message: String): this(Meta(status.value(), message))
}

data class Meta(
    val code: Int = HttpStatus.OK.value(),
    val message: String = "ok",
)