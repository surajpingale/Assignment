package com.example.assignment.model.repository

import android.util.Log
import androidx.core.net.toUri
import com.example.assignment.model.dataclass.AddProductResponse
import com.example.assignment.model.dataclass.Product
import com.example.assignment.model.remote.ApiInterface
import com.example.assignment.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

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
    suspend fun addProduct(
        productName: String, productType: String, price: Double,
        taxRate: Double, productImage: String
    ): Response<AddProductResponse> {
        return apiInterface.addProduct(productName, productType, price, taxRate, productImage)
    }

    suspend fun addProduct2(
        productName: String, productType: String, price: Double,
        taxRate: Double, imagesList: HashMap<String, File>
    ): Response<AddProductResponse> {
        val name = MultipartBody.Part.createFormData(Constant.TEXT_PLAIN, productName).body
        val type = MultipartBody.Part.createFormData(Constant.TEXT_PLAIN, productType).body
        val priceM = MultipartBody.Part.createFormData(Constant.TEXT_PLAIN, price.toString()).body
        val tax = MultipartBody.Part.createFormData(Constant.TEXT_PLAIN, taxRate.toString()).body

        val multipartFileList = ArrayList<MultipartBody.Part>()

        for ((key, value) in imagesList) {
            val file = File(value.toUri().path!!)
//            val filename =

            Log.d("product", "file name - $key - $value")

            val fileToApi = MultipartBody.Part.createFormData(
                "files[]", value.name,
                value.asRequestBody()
            )

            multipartFileList.add(fileToApi)
        }


        val fileList = ArrayList<File>()

        for ((key, value) in imagesList) {
            fileList.add(value)
        }

        val partMap = HashMap<String, RequestBody>()
        partMap[Constant.PRODUCT_NAME] = name
        partMap[Constant.PRODUCT_TYPE] =type
        partMap[Constant.PRODUCT_PRICE] = priceM
        partMap[Constant.PRODUCT_TAX] = tax
//        partMap[Constant.PRODUCT_IMAGES] = fileList.toTypedArray()


        return apiInterface.addProductMultipart(
            partMap, multipartFileList[0]
        )
    }


}