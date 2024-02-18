package com.homework.payhere.domain.product

import jakarta.persistence.*

@Table(
    indexes = [
        Index(name = "index_seller_initialcharacter", columnList = "sellerId, initial_character")
    ]
)
@Entity
class ProductNameInitialCharacter(

    @Column(name = "sellerId", nullable = false)
    var sellerId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    var product: Product,

    @Column(name = "initial_character", nullable = false)
    var initialCharacter: String,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}