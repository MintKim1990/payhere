package com.homework.payhere.application.product.response

import com.homework.payhere.application.product.request.ProductSearchCursorRequest
import com.homework.payhere.domain.product.Product
import com.homework.payhere.domain.product.ProductStatus
import com.homework.payhere.domain.product.Size
import java.time.LocalDate

data class ProductCreateResponse(
    val id: Long,
    val category: String,
    val price: Int,
    val cost: Int,
    val name: String,
    val description: String,
    val barcode: String,
    var expirationDate: LocalDate,
    var size: Size,
    val status : ProductStatus,
) {
    companion object {
        fun toResponse(product: Product): ProductCreateResponse {
            return ProductCreateResponse(
                id = product.id!!,
                category = product.category,
                price = product.price,
                cost = product.cost,
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                expirationDate = product.expirationDate,
                size = product.size,
                status = product.status
            )
        }
    }
}

data class ProductUpdateResponse(
    val id: Long,
    val category: String,
    val price: Int,
    val cost: Int,
    val name: String,
    val description: String?,
    val barcode: String?,
    var expirationDate: LocalDate?,
    var size: Size,
    val status: ProductStatus,
) {
    companion object {
        fun toResponse(product: Product): ProductUpdateResponse {
            return ProductUpdateResponse(
                id = product.id!!,
                category = product.category,
                price = product.price,
                cost = product.cost,
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                expirationDate = product.expirationDate,
                size = product.size,
                status = product.status
            )
        }
    }
}

data class ProductSearchCursorResponse<T>(
    val nextProductSearchCursorRequest: ProductSearchCursorRequest,
    val products: List<T>
)

data class ProductSearchResponse(
    val id: Long,
    val category: String,
    val price: Int,
    val cost: Int,
    val name: String,
    val description: String,
    val barcode: String,
    val expirationDate: LocalDate,
    val size: Size,
    val status: ProductStatus,
) {
    companion object {
        fun toResponse(product: Product): ProductSearchResponse {
            return ProductSearchResponse(
                id = product.id!!,
                category = product.category,
                price = product.price,
                cost = product.cost,
                name = product.name,
                description = product.description,
                barcode = product.barcode,
                expirationDate = product.expirationDate,
                size = product.size,
                status = product.status
            )
        }
    }
}