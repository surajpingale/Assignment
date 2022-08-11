package com.example.assignment.di

import com.example.assignment.viewmodel.ProductViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * component for injecting view model
 */
class ProductComponent : KoinComponent {

    val productViewModel : ProductViewModel by inject()
}