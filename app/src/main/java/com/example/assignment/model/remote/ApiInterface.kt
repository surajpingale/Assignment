package com.example.assignment.model.remote

import com.example.assignment.model.dataclass.AddProductResponse
import com.example.assignment.model.dataclass.Product
import com.example.assignment.utils.Constant
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    // for getting all products list from api
    @GET(Constant.GET_ALL_PRODUCTS_END_POINT)
    suspend fun getAllProducts(): Response<List<Product>>

    // for post product to api body type - form-data
    @FormUrlEncoded
    @POST(Constant.POST_PRODUCT_END_POINT)
    suspend fun addProduct(
        @Field(Constant.PRODUCT_NAME) productName: String,
        @Field(Constant.PRODUCT_TYPE) productType: String,
        @Field(Constant.PRODUCT_PRICE) productPrice: Double,
        @Field(Constant.PRODUCT_TAX) productTax: Double,
        @Field(Constant.PRODUCT_IMAGES) productImages: String,
    ): Response<AddProductResponse>
}