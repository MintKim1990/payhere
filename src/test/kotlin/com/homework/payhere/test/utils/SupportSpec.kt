package com.homework.payhere.test.utils

import io.kotest.core.spec.BeforeTest
import io.kotest.core.spec.Spec
import io.kotest.core.test.isRootTest

fun Spec.beforeRootTest(f: BeforeTest) {
    beforeTest {
        if (it.isRootTest()) f(it)
    }
}