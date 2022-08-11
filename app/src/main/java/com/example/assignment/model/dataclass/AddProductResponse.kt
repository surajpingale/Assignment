package com.example.assignment.model.dataclass


data class AddProductResponse(
    val message: String,
    val product_details: AddProduct,
    val product_id: Int,
    val success: Boolean
)