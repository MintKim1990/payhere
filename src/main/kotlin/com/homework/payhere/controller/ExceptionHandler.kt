package com.homework.payhere.controller

import com.homework.payhere.utils.exception.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        logger.error("MethodArgumentNotValidException", exception)

        val fieldErrorsMap = mutableMapOf<String, String>()
        exception.bindingResult.fieldErrors.forEach {
            fieldErrorsMap[it.field] = it.defaultMessage.orEmpty()
        }

        return ResponseEntity.status(BAD_REQUEST).body(
            Response<Map<String, String>>(BAD_REQUEST, "파라미터가 유효하지 않습니다.", fieldErrorsMap)
        )
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(exception: Exception): ResponseEntity<Response<Unit>> {
        logger.error("Exception", exception)
        return ResponseEntity.status(SERVICE_UNAVAILABLE).body(Response(SERVICE_UNAVAILABLE, exception.message))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateExceptionHandler(exception: IllegalStateException): ResponseEntity<Response<Unit>> {
        logger.error("IllegalStateException", exception)
        return ResponseEntity.status(CONFLICT).body(Response(CONFLICT, exception.message))
    }

    @ExceptionHandler(LoginFailException::class)
    fun loginFailExceptionHandler(exception: LoginFailException): ResponseEntity<Response<Unit>> {
        logger.error("LoginFailException", exception)
        return ResponseEntity.status(UNAUTHORIZED).body(Response(UNAUTHORIZED, exception.message))
    }

    @ExceptionHandler(NotAvailablePhoneNumberException::class)
    fun notAvailablePhoneNumberExceptionHandler(exception: NotAvailablePhoneNumberException): ResponseEntity<Response<Unit>> {
        logger.error("NotAvailablePhoneNumberException", exception)
        return ResponseEntity.status(BAD_REQUEST).body(Response(BAD_REQUEST, exception.message))
    }

    @ExceptionHandler(SellerNotFoundException::class)
    fun sellerNotFoundExceptionHandler(exception: SellerNotFoundException): ResponseEntity<Response<Unit>> {
        logger.error("SellerNotFoundException", exception)
        return ResponseEntity.status(BAD_REQUEST).body(Response(BAD_REQUEST, exception.message))
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun productNotFoundExceptionHandler(exception: ProductNotFoundException): ResponseEntity<Response<Unit>> {
        logger.error("ProductNotFoundException", exception)
        return ResponseEntity.status(BAD_REQUEST).body(Response(BAD_REQUEST, exception.message))
    }

    @ExceptionHandler(NotAvailableTokenException::class)
    fun notAvailableTokenExceptionHandler(exception: NotAvailableTokenException): ResponseEntity<Response<Unit>> {
        logger.error("NotAvailableTokenException", exception)
        return ResponseEntity.status(BAD_REQUEST).body(Response(BAD_REQUEST, exception.message))
    }

}