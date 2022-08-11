package com.example.assignment.model.dataclass

data class Product(
    var productId: Int = 0,
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)