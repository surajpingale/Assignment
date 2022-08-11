package com.example.assignment.application

import android.app.Application
import com.example.assignment.di.networkModule
import com.example.assignment.di.viewModelModule
import org.koin.core.context.startKoin

class ProductApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // for starting koin
        startKoin {
            modules(
                networkModule, viewModelModule
            )
        }
    }
}