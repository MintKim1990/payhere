package com.homework.payhere.controller

import com.homework.payhere.utils.exception.LoginFailException
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(LoginFailException::class)
    fun loginFailExceptionHandler(exception: LoginFailException): ResponseEntity<Response<Unit>> {
        logger.error("LoginFailException", exception)
        return ResponseEntity.status(UNAUTHORIZED).body(Response(UNAUTHORIZED, exception.message))
    }

}