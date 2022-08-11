package com.example.assignment.di

import com.example.assignment.model.remote.ApiInterface
import com.example.assignment.model.repository.ProductRepository
import com.example.assignment.utils.Constant
import com.example.assignment.viewmodel.ProductViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    // for singleton retrofit
    single { getRetrofitInstance() }
    // for singleton retrofit ApiInterface::class.java
    single { getApiInterface(get()) }
}

/**
 * for logging response body from api
 */
private val interceptor =
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

/**
 * for retrofit instance
 */
fun getRetrofitInstance(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        .build()
}

/**
 * for retrofit ApiInterface
 */
fun getApiInterface(retrofit: Retrofit): ApiInterface {
    return retrofit.create(ApiInterface::class.java)
}

