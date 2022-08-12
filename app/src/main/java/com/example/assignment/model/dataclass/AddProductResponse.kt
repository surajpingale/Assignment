package com.example.assignment.model.dataclass

/**
 * response from api after adding product
 */
data class AddProductResponse(
    val message: String,
    val product_details: Product,
    val product_id: Int,
    val success: Boolean
)