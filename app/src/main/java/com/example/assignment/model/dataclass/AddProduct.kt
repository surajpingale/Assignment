package com.example.assignment.model.dataclass

import java.io.File

/**
 * for all product details
 */
data class AddProduct(
    val files: Array<File> = emptyArray(),
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)