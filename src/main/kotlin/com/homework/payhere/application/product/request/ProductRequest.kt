package com.homework.payhere.application.product.request

import com.homework.payhere.application.product.response.ProductSearchResponse
import com.homework.payhere.domain.product.Product
import com.homework.payhere.domain.product.ProductStatus
import com.homework.payhere.domain.product.Size
import com.homework.payhere.domain.seller.Seller
import jakarta.persistence.Column
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class ProductCreateRequest(

    @field:NotBlank(message = "카테고리는 필수입니다.")
    val category: String,

    @field:NotNull(message = "가격은 필수입니다.")
    val price: Int,

    @field:NotNull(message = "원가은 필수입니다.")
    val cost: Int,

    @field:NotBlank(message = "상품명 필수입니다.")
    val name: String,

    @field:NotBlank(message = "상품설명은 필수입니다.")
    val description: String,

    @field:NotBlank(message = "바코드정보는 필수입니다.")
    val barcode: String,

    @field:NotNull(message = "유통기한은 필수입니다.")
    var expirationDate: LocalDate,

    @field:NotNull(message = "사이즈는 필수입니다.")
    var size: Size,

    ) {
    fun toProduct(seller: Seller): Product {
        return Product(
            category = category,
            price = price,
            cost = cost,
            name = name,
            description = description,
            barcode = barcode,
            expirationDate = expirationDate,
            size = size,
            sellerId = seller.id!!
        )
    }
}

data class ProductUpdateRequest(

    @field:NotNull(message = "상품정보는 필수입니다.")
    val id: Long,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    val category: String,

    @field:NotNull(message = "가격은 필수입니다.")
    val price: Int,

    @field:NotNull(message = "원가은 필수입니다.")
    val cost: Int,

    @field:NotBlank(message = "상품명 필수입니다.")
    val name: String,

    @field:NotBlank(message = "상품설명은 필수입니다.")
    val description: String,

    @field:NotBlank(message = "바코드정보는 필수입니다.")
    val barcode: String,

    @field:NotNull(message = "유통기한은 필수입니다.")
    var expirationDate: LocalDate,

    @field:NotNull(message = "사이즈는 필수입니다.")
    var size: Size,

    var status: ProductStatus = ProductStatus.SALE,

)

data class ProductDeleteRequest(

    @field:NotNull(message = "상품정보는 필수입니다.")
    val id: Long,

)

data class ProductSearchCursorRequest(
    val key: Long? = null,
    val size: Int = 10
) {

    fun hasKey() = key != null

    fun next(products: List<ProductSearchResponse>): ProductSearchCursorRequest {
        val nextKey = products.minOfOrNull { it.id!! } ?: -1
        return ProductSearchCursorRequest(nextKey, size)
    }
}