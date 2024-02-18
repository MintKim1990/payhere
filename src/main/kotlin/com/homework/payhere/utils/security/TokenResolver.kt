package com.homework.payhere.utils.security

import com.homework.payhere.utils.exception.LoginFailException
import com.homework.payhere.utils.exception.NotAvailableTokenException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val BEARER = "Bearer"

@Component
class TokenResolver(
    private val userSessionManager: UserSessionManager
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Token::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): String {
        val token = extractBearerToken(webRequest)
        if (!userSessionManager.isAvailableToken(token)) {
            throw NotAvailableTokenException()
        }
        return token
    }

    private fun extractBearerToken(request: NativeWebRequest): String {
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw LoginFailException()
        val (tokenType, token) = splitToTokenFormat(authorization)
        if (tokenType != BEARER) {
            throw LoginFailException()
        }
        return token
    }

    private fun splitToTokenFormat(authorization: String): Pair<String, String> {
        return try {
            val tokenFormat = authorization.split(" ")
            tokenFormat[0] to tokenFormat[1]
        } catch (e: IndexOutOfBoundsException) {
            throw LoginFailException()
        }
    }

}