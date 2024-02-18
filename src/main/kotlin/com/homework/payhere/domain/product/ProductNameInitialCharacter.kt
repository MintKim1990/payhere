package com.homework.payhere.domain.product

import jakarta.persistence.*

@Table(
    indexes = [
        Index(name = "product_name_initial_character_index_seller_id_initial_character", columnList = "sellerId, initial_character"),
        Index(name = "product_name_initial_character_index_product_id", columnList = "productId"),
    ]
)
@Entity
class ProductNameInitialCharacter(

    @Column(name = "sellerId", nullable = false)
    var sellerId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false, foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    var product: Product,

    @Column(name = "initial_character", nullable = false)
    var initialCharacter: String,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}