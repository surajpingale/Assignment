package com.example.assignment.di

import com.example.assignment.model.repository.ProductRepository
import com.example.assignment.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * view model with repository
 */
val viewModelModule = module {
    factory { ProductRepository(get()) }
    viewModel{ ProductViewModel(get()) }
}