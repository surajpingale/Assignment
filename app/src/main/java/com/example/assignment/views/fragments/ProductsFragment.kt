package com.example.assignment.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.R
import com.example.assignment.databinding.FragmentProductsBinding
import com.example.assignment.di.ProductComponent
import com.example.assignment.model.dataclass.Product
import com.example.assignment.utils.Response
import com.example.assignment.views.adapter.ProductAdapter


class ProductsFragment : Fragment() {

    // creating binding reference null
    private var _binding: FragmentProductsBinding? = null

    private val binding get() = _binding!!

    // for injecting view model from koin
    private val viewModel = ProductComponent().productViewModel

    private lateinit var productList: List<Product>
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun updateRecyclerView() {
        productList = ArrayList()

        val recyclerView = binding.rvProduct

        productAdapter = ProductAdapter(requireActivity(), productList)

        // inflating recycler view
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productAdapter
        }
    }

    private fun initViews() {
        updateRecyclerView()
        showProducts()
        binding.fabAddNewProduct.setOnClickListener {
            findNavController().navigate(
                ProductsFragmentDirections.fragProductsToAddProducts()
            )
        }
    }

    private fun showProducts() {
        viewModel.getAllProducts()
        viewModel.products.observe(viewLifecycleOwner) { state ->

            when (state) {
                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvProduct.visibility = View.GONE
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvProduct.visibility = View.VISIBLE
                    // updating recycler view adapter
                    productAdapter.updateProductList(state.data!!)
                }
                is Response.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvProduct.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * setting binding reference again null
     * to avoid memory leaks
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}