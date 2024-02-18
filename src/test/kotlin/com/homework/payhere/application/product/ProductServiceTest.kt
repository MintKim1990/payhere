package com.homework.payhere.application.product

import com.homework.payhere.application.product.request.ProductSearchCursorRequest
import com.homework.payhere.createSeller
import com.homework.payhere.domain.product.ProductNameInitialCharacterRepository
import com.homework.payhere.domain.product.ProductRepository
import com.homework.payhere.domain.product.ProductStatus
import com.homework.payhere.domain.seller.SellerRepository
import com.homework.payhere.test.config.EmbeddedRedisConfig
import com.homework.payhere.test.fixtures.createProductCreateRequest
import com.homework.payhere.test.fixtures.createProductDeleteRequest
import com.homework.payhere.test.fixtures.createProductNameUpdateRequest
import com.homework.payhere.test.utils.beforeRootTest
import com.homework.payhere.utils.exception.ProductNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@SpringBootTest
@Import(EmbeddedRedisConfig::class)
@ActiveProfiles("test")
class ProductServiceTest(
    private val productRepository: ProductRepository,
    private val productNameInitialCharacterRepository: ProductNameInitialCharacterRepository,
    private val sellerRepository: SellerRepository,
    private val productService: ProductService
) : BehaviorSpec({

    beforeRootTest {
        productRepository.deleteAll()
        productNameInitialCharacterRepository.deleteAll()
        sellerRepository.deleteAll()
    }

    Given("상품 생성 후") {

        val seller = sellerRepository.save(createSeller())
        val productCreateRequest = createProductCreateRequest(name = "아이스 아메리카노")
        val productCreateResponse = productService.create(productCreateRequest, seller)

        When("상품상태를 조회하면") {

            val status = productCreateResponse.status

            Then("'판매중' 상태여야 한다.") {
                status shouldBe ProductStatus.SALE
            }
        }

        When("'ㅇㅇㅅ' 초성으로 조회하면") {

            val productSearchResponses = productService.search("ㅇㅇㅅ", seller)

            Then("'아이스 아메리카노' 상품이 조회되야 한다.") {
                productSearchResponses.size shouldBe 1
                productSearchResponses.first().name shouldBe "아이스 아메리카노"
            }
        }

        When("'ㅇㅁㄹㅋㄴ' 초성으로 조회하면") {

            val productSearchResponses = productService.search("ㅇㅁㄹㅋㄴ", seller)

            Then("'아이스 아메리카노' 상품이 조회되야 한다.") {
                productSearchResponses.size shouldBe 1
                productSearchResponses.first().name shouldBe "아이스 아메리카노"
            }
        }

        When("상품명을 변경하면") {

            val productUpdateRequest = createProductNameUpdateRequest("슈크림 메가 라떼", productCreateResponse)
            val productUpdateResponse = productService.update(productUpdateRequest, seller)

            Then("상품명이 변경되야 한다.") {
                productUpdateResponse.name shouldBe "슈크림 메가 라떼"
            }

            Then("기존 초성으로 조회 시 조회되면 안된다.") {
                val productSearchResponses = productService.search("ㅇㅁㄹㅋㄴ", seller)
                productSearchResponses.isEmpty() shouldBe true
            }

        }

        When("유통기한을 금일 이전일로 변경하면") {

            val productUpdateRequest = createProductNameUpdateRequest(LocalDate.of(2024, 2, 1), productCreateResponse)
            val productUpdateResponse = productService.update(productUpdateRequest, seller)

            Then("상품상태가 '판매중지' 상태여야 한다.") {
                productUpdateResponse.status shouldBe ProductStatus.PAUSE
            }
        }

        When("상품을 삭제하면") {

            val productDeleteRequest = createProductDeleteRequest(productCreateResponse.id)
            productService.delete(productDeleteRequest, seller)

            Then("상품 조회시 조회되지 않아야 한다.") {
                shouldThrow<ProductNotFoundException> {
                    productService.search(productCreateResponse.id, seller)
                }
            }
        }
    }

    Given("유통기한이 지난 상품 생성 시") {

        val seller = sellerRepository.save(createSeller())
        val productCreateRequest = createProductCreateRequest(expirationDate = LocalDate.of(2024, 2, 1))
        val productCreateResponse = productService.create(productCreateRequest, seller)

        When("상품상태를 조회하면") {

            val status = productCreateResponse.status

            Then("'판매중지' 상태여야 한다.") {
                status shouldBe ProductStatus.PAUSE
            }
        }
    }

    Given("여러 상품 생성 시") {

        val seller = sellerRepository.save(createSeller())
        productService.create(createProductCreateRequest(name = "아이스 아메리카노1"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노2"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노3"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노4"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노5"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노6"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노7"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노8"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노9"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노10"), seller)
        productService.create(createProductCreateRequest(name = "아이스 아메리카노11"), seller)

        When("처음 커서기반 조회를 하면") {
            val productSearchCursorRequest = ProductSearchCursorRequest()
            val productSearchCursorResponse = productService.search(productSearchCursorRequest, seller)

            Then("다음 조회 키값과 기본 10개에 상품정보를 리턴한다.") {
                productSearchCursorResponse.nextProductSearchCursorRequest.key.shouldNotBeNull()
                productSearchCursorResponse.products.size shouldBe 10
            }
        }
    }

    Given("상품 생성 후") {

        val seller = sellerRepository.save(createSeller())
        val productCreateRequest = createProductCreateRequest(name = "아이스 아메리카노", barcode = "barcode")
        val productCreateResponse = productService.create(productCreateRequest, seller)

        When("동일한 바코드에 상품을 추가하면") {

            val sameBarcodeProductCreateRequest = createProductCreateRequest(name = "아이스 아메리카노2", barcode = "barcode")

            Then("생성에 실패한다.") {
                shouldThrow<DataIntegrityViolationException> {
                    productService.create(sameBarcodeProductCreateRequest, seller)
                }
            }
        }
    }

})
