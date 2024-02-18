package com.homework.payhere.domain.product

import com.homework.payhere.domain.BaseEntity
import com.homework.payhere.domain.product.ProductStatus.*
import com.homework.payhere.utils.initial.extractInitialCharacters
import jakarta.persistence.*
import java.time.LocalDate

@Table(
    indexes = [
        Index(name = "index_seller", columnList = "sellerId")
    ]
)
@Entity
class Product(

    @Column(name = "category", nullable = false)
    var category: String,

    @Column(name = "price", nullable = false)
    var price: Int,

    @Column(name = "cost", nullable = false)
    var cost: Int,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "description", nullable = false)
    @Lob
    var description: String,

    @Column(name = "barcode", nullable = false, unique = true)
    var barcode: String,

    @Column(name = "expiration_date", nullable = false)
    var expirationDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    var size: Size,

    @Column(name = "sellerId", nullable = false)
    var sellerId: Long,

    ) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    var initialCharacters: MutableList<ProductNameInitialCharacter> = createProductNameInitialCharacter(name, this, sellerId)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: ProductStatus = SALE
        get() {
            if (isExpirationDateOver()) {
                return PAUSE
            }
            return field
        }

    fun update(
        category: String,
        price: Int,
        cost: Int,
        name: String,
        description: String,
        barcode: String,
        expirationDate: LocalDate,
        size: Size,
        status: ProductStatus,
    ) {
        if (this.name != name) {
            refreshInitialCharacters(name)
        }

        this.category = category
        this.price = price
        this.cost = cost
        this.name = name
        this.description = description
        this.barcode = barcode
        this.expirationDate = expirationDate
        this.size = size
        this.status = status
    }

    private fun createProductNameInitialCharacter(
        name: String,
        product: Product,
        sellerId: Long
    ) = extractInitialCharacters(name).map { ProductNameInitialCharacter(sellerId, product, it) }.toMutableList()

    private fun isExpirationDateOver(): Boolean {
        return LocalDate.now().isAfter(this.expirationDate)
    }

    private fun refreshInitialCharacters(name: String) {
        initialCharacters.clear()
        createProductNameInitialCharacter(name, this, sellerId).forEach {
            initialCharacters.add(it)
        }
    }
}