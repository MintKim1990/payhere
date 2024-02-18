package com.homework.payhere.config

import com.homework.payhere.utils.security.LoginSellerResolver
import com.homework.payhere.utils.security.TokenResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthenticationConfig(
    private val loginSellerResolver: LoginSellerResolver,
    private val tokenResolver: TokenResolver,
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginSellerResolver)
        resolvers.add(tokenResolver)
    }
}