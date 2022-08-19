package com.example.assignment.model.remote

import com.example.assignment.model.dataclass.AddProductResponse
import com.example.assignment.model.dataclass.Product
import com.example.assignment.utils.Constant
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiInterface {

    // for getting all products list from api
    @GET(Constant.GET_ALL_PRODUCTS_END_POINT)
    suspend fun getAllProducts(): Response<List<Product>>

    // for post product to api body type - form-data with image
    @Multipart
    @POST(Constant.POST_PRODUCT_END_POINT)
    suspend fun addProductMultipart(
        @PartMap productName: HashMap<String, RequestBody>,
        @Part productImages: MultipartBody.Part
    ): Response<AddProductResponse>

}