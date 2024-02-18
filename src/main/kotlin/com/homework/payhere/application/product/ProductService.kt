package com.homework.payhere.application.product

import com.homework.payhere.application.product.request.ProductCreateRequest
import com.homework.payhere.application.product.request.ProductDeleteRequest
import com.homework.payhere.application.product.request.ProductSearchCursorRequest
import com.homework.payhere.application.product.request.ProductUpdateRequest
import com.homework.payhere.application.product.response.ProductCreateResponse
import com.homework.payhere.application.product.response.ProductSearchCursorResponse
import com.homework.payhere.application.product.response.ProductSearchResponse
import com.homework.payhere.application.product.response.ProductUpdateResponse
import com.homework.payhere.domain.product.Product
import com.homework.payhere.domain.product.ProductNameInitialCharacterRepository
import com.homework.payhere.domain.product.ProductRepository
import com.homework.payhere.domain.seller.Seller
import com.homework.payhere.utils.exception.ProductNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productNameInitialCharacterRepository: ProductNameInitialCharacterRepository
) {

    fun search(productSearchCursorRequest: ProductSearchCursorRequest, seller: Seller):
            ProductSearchCursorResponse<ProductSearchResponse> {

        if (productSearchCursorRequest.hasKey()) {
            val products = productRepository.findAllBySellerIdAndCursor(seller.id!!,
                productSearchCursorRequest.key!!,
                productSearchCursorRequest.size)

            val productSearchResponses = products.map { ProductSearchResponse.toResponse(it) }
            return ProductSearchCursorResponse(productSearchCursorRequest.next(productSearchResponses), productSearchResponses)
        }

        val products = productRepository.findAllBySellerIdAndCursor(seller.id!!, productSearchCursorRequest.size)
        val productSearchResponses = products.map { ProductSearchResponse.toResponse(it) }
        return ProductSearchCursorResponse(productSearchCursorRequest.next(productSearchResponses), productSearchResponses)
    }

    fun search(id: Long, seller: Seller): ProductSearchResponse {
        val product: Product = productRepository.findByIdAndSellerId(id, seller.id!!)
            ?: throw ProductNotFoundException()

        return ProductSearchResponse.toResponse(product)
    }

    fun search(name: String, seller: Seller): List<ProductSearchResponse> {
        val products = productRepository.findAllBySellerIdAndNameContaining(seller.id!!, name)
            .ifEmpty {
                productNameInitialCharacterRepository.findAllBySellerIdAndInitialCharacter(seller.id!!, name).map { it.product }
            }

        return products.map { ProductSearchResponse.toResponse(it) }
    }

    @Transactional
    fun create(productCreateRequest: ProductCreateRequest, seller: Seller): ProductCreateResponse {
        val product = productCreateRequest.toProduct(seller)
        productRepository.save(product)
        return ProductCreateResponse.toResponse(product)
    }

    @Transactional
    fun update(productUpdateRequest: ProductUpdateRequest, seller: Seller): ProductUpdateResponse {
        val product: Product = productRepository.findByIdAndSellerId(productUpdateRequest.id, seller.id!!)
            ?: throw ProductNotFoundException()

        product.update(
            category = productUpdateRequest.category,
            price = productUpdateRequest.price,
            cost = productUpdateRequest.cost,
            name = productUpdateRequest.name,
            description = productUpdateRequest.description,
            barcode = productUpdateRequest.barcode,
            expirationDate = productUpdateRequest.expirationDate,
            size = productUpdateRequest.size,
            status = productUpdateRequest.status
        )
        return ProductUpdateResponse.toResponse(product)
    }

    @Transactional
    fun delete(productDeleteRequest: ProductDeleteRequest, seller: Seller) {
        val product: Product = productRepository.findByIdAndSellerId(productDeleteRequest.id, seller.id!!)
            ?: throw ProductNotFoundException()

        productRepository.delete(product)
    }

}