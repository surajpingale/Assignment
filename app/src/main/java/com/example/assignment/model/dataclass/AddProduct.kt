package com.example.assignment.model.dataclass


data class AddProduct(
    var image: String = "",
    var price: Double = 0.0,
    var product_name: String = "",
    var product_type: String = "",
    var tax: Double = 0.0
)