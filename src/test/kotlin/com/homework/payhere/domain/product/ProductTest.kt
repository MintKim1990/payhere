package com.homework.payhere.domain.product

import com.homework.payhere.test.fixtures.createProduct
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

internal class ProductTest : BehaviorSpec({

    Given("상품 생성 시") {

        val product = createProduct()

        When("상품상태를 조회하면") {

            val status = product.status

            Then("'판매중' 상태여야 한다.") {
                status shouldBe ProductStatus.SALE
            }
        }

        When("상품초성을 조회하면") {

            val name = product.name
            val productNameInitialCharacters = product.initialCharacters

            Then("상품명을 기준으로 초성이 생성되있어야 한다.") {
                name shouldBe "아이스 아메리카노"
                productNameInitialCharacters.size shouldBe 2
                productNameInitialCharacters[0].initialCharacter shouldBe "ㅇㅇㅅ"
                productNameInitialCharacters[1].initialCharacter shouldBe "ㅇㅁㄹㅋㄴ"
            }
        }

        When("상품명을 변경하면") {

            product.update(
                category = product.category,
                price = product.price,
                cost = product.cost,
                name = "슈크림 메가 라떼",
                description = product.description,
                barcode = product.barcode,
                expirationDate = product.expirationDate,
                size = product.size,
                status = product.status
            )

            Then("상품명이 변경되야 한다.") {
                product.name shouldBe "슈크림 메가 라떼"
            }
            Then("기존 초성정보가 삭제되고 변경된 이름에 초성정보가 저장되야 한다.") {
                val productNameInitialCharacters = product.initialCharacters
                productNameInitialCharacters.size shouldBe 3
                productNameInitialCharacters[0].initialCharacter shouldBe "ㅅㅋㄹ"
                productNameInitialCharacters[1].initialCharacter shouldBe "ㅁㄱ"
                productNameInitialCharacters[2].initialCharacter shouldBe "ㄹㄸ"
            }
        }

        When("유통기한을 금일 이전일로 변경하면") {

            product.update(
                category = product.category,
                price = product.price,
                cost = product.cost,
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                expirationDate = LocalDate.of(2024, 2, 1),
                size = product.size,
                status = product.status
            )

            Then("상품상태가 '판매중지' 상태여야 한다.") {
                product.status shouldBe ProductStatus.PAUSE
            }
        }
    }

    Given("유통기한이 지난 상품 생성 시") {

        val product = createProduct(expirationDate = LocalDate.of(2024, 2, 1))

        When("상품상태를 조회하면") {

            val status = product.status

            Then("'판매중지' 상태여야 한다.") {
                status shouldBe ProductStatus.PAUSE
            }
        }
    }

})