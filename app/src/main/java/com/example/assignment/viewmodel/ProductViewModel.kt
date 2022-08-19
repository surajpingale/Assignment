package com.example.assignment.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.model.dataclass.AddProductResponse
import com.example.assignment.model.dataclass.Product
import com.example.assignment.model.repository.ProductRepository
import com.example.assignment.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class ProductViewModel constructor(
    private val repository: ProductRepository
) : ViewModel() {

    // get products list response mutable data
    private var _products = MutableLiveData<Response<List<Product>>>()

    // get products list response Live data
    val products: LiveData<Response<List<Product>>>
        get() = _products

    // add product response mutable data
    private var _addProduct = MutableLiveData<Response<AddProductResponse>>()

    // add product response Live data
    val addProduct: LiveData<Response<AddProductResponse>>
        get() = _addProduct


    /**
     * fun for get all products from api
     */
    fun getAllProducts() {
        _products.postValue(Response.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getProducts()
            if (result.isSuccessful && result.body() != null) {
                _products.postValue(Response.Success(result.body()!!))
            } else {
                _products.postValue(Response.Error(result.errorBody().toString()))
            }
        }
    }

    /**
     * fun for add product to api
     */
    fun addProductResponse(
        productName: String, productType: String, price: Double,
        taxRate: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addProduct(
                productName,
                productType, price, taxRate, ""
            )
            if (result.code() == 200 && result.body() != null) {
                _addProduct.postValue(Response.Success(result.body()!!))
            } else {
                _addProduct.postValue(Response.Error(result.errorBody().toString()))
            }
        }
    }

    fun addProductResponse2(
        productName: String, productType: String, price: Double,
        taxRate: Double,
        imagesList: HashMap<String, File>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileList: ArrayList<File> = ArrayList()
//            for (i in imagesList)
//            {
//                Log.d("product", "before convert - ${i}")
//                val file = File(i)
//                fileList.add(file)
//                Log.d("product", "file name - ${file.name}")
//            }
            try {
                val result = repository.addProduct2(
                    productName,
                    productType, price, taxRate, imagesList
                )
                if (result.code() == 200 && result.body() != null) {
                    _addProduct.postValue(Response.Success(result.body()!!))
                } else {
                    _addProduct.postValue(Response.Error(result.errorBody().toString()))
                }
            } catch (e: Exception) {
                Log.d("product", "error - ${e.message}")
            }
        }
    }


    /**
     * fun for validation user entries
     */
    fun validateUserInput(
        productName: String, sellingPrice: String, taxRate: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(sellingPrice)
            || TextUtils.isEmpty(taxRate)
        ) {
            result = Pair(false, "Something empty")
        }
        return result
    }

}