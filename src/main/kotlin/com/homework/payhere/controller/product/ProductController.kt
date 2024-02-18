package com.homework.payhere.controller.product

import com.homework.payhere.application.product.ProductService
import com.homework.payhere.application.product.request.ProductCreateRequest
import com.homework.payhere.application.product.request.ProductDeleteRequest
import com.homework.payhere.application.product.request.ProductSearchCursorRequest
import com.homework.payhere.application.product.request.ProductUpdateRequest
import com.homework.payhere.application.product.response.ProductCreateResponse
import com.homework.payhere.application.product.response.ProductSearchCursorResponse
import com.homework.payhere.application.product.response.ProductSearchResponse
import com.homework.payhere.application.product.response.ProductUpdateResponse
import com.homework.payhere.controller.Response
import com.homework.payhere.domain.product.Product
import com.homework.payhere.domain.seller.Seller
import com.homework.payhere.utils.security.LoginSeller
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun search(
        @RequestBody @Validated productSearchCursorRequest: ProductSearchCursorRequest,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<ProductSearchCursorResponse<ProductSearchResponse>>> {
        val productSearchCursorResponse = productService.search(productSearchCursorRequest, seller)
        return ResponseEntity.ok(Response(productSearchCursorResponse))
    }

    @GetMapping("/{id}")
    fun search(
        @PathVariable id: Long,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<ProductSearchResponse>> {
        val productSearchResponse = productService.search(id, seller)
        return ResponseEntity.ok(Response(productSearchResponse))
    }

    @GetMapping("/search")
    fun search(
        @RequestParam name: String,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<List<ProductSearchResponse>>> {
        val productSearchResponse = productService.search(name, seller)
        return ResponseEntity.ok(Response(productSearchResponse))
    }

    @PostMapping
    fun create(
        @RequestBody @Validated productCreateRequest: ProductCreateRequest,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<ProductCreateResponse>> {
        val productCreateResponse = productService.create(productCreateRequest, seller)
        return ResponseEntity.ok(Response(productCreateResponse))
    }

    @PutMapping
    fun update(
        @RequestBody @Validated productUpdateRequest: ProductUpdateRequest,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<ProductUpdateResponse>> {
        val productUpdateResponse = productService.update(productUpdateRequest, seller)
        return ResponseEntity.ok(Response(productUpdateResponse))
    }

    @DeleteMapping
    fun delete(
        @RequestBody @Validated productDeleteRequest: ProductDeleteRequest,
        @LoginSeller seller: Seller
    ): ResponseEntity<Response<Unit>> {
        productService.delete(productDeleteRequest, seller)
        return ResponseEntity.ok(Response())
    }

}