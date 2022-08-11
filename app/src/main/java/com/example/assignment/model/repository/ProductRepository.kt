package com.example.assignment.model.repository

import com.example.assignment.model.dataclass.AddProductResponse
import com.example.assignment.model.dataclass.Product
import com.example.assignment.model.remote.ApiInterface
import retrofit2.Response

class ProductRepository constructor(private val apiInterface: ApiInterface) {

    /**
     * suspend fun for getting products list from api
     */
    suspend fun getProducts(): Response<List<Product>> {
        return apiInterface.getAllProducts()
    }

    /**
     * suspend fun for add product to api
     */
     suspend fun addProduct(productName: String, productType:String, price:Double,
                            taxRate: Double, productImage:String): Response<AddProductResponse> {
        return apiInterface.addProduct(productName, productType, price, taxRate, productImage)
    }

}