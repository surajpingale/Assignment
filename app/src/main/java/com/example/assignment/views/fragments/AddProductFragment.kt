package com.example.assignment.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.databinding.FragmentAddProductBinding
import com.example.assignment.di.ProductComponent
import com.example.assignment.utils.Constant
import com.example.assignment.utils.Permission
import com.example.assignment.utils.Response
import com.example.assignment.views.adapter.ProductImagesAdapter


class AddProductFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddProductBinding? = null

    private val binding get() = _binding!!

    // for injecting view model from koin
    private val viewModel = ProductComponent().productViewModel

    // for product type
    private var productType = "Product"

    // product images list
    private lateinit var imagesList: ArrayList<String>

    // for loading image from gallery
    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {
            // for knowing extension of image
            val imageExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(requireContext().contentResolver.getType(uri))

            // allowing only jpeg and png images
            if (imageExtension.equals(Constant.IMAGE_JPEG) || imageExtension.equals(Constant.IMAGE_PNG)) {
                imagesList.add(uri.toString())
                val recyclerView = binding.included.rvProductImages
                val imagesAdapter = ProductImagesAdapter(requireActivity(), imagesList)

                // product images recycler view
                recyclerView.apply {
                    setHasFixedSize(true)
                    // horizontal recycler view for product images
                    layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    adapter = imagesAdapter
                }
            } else {
                showToast("You are selecting $imageExtension.\nPlease select jpeg or png.")
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)

        initViewsAndListeners()

        return binding.root
    }

    /**
     * fun for initialising views
     */
    private fun initViewsAndListeners() {
        imagesList = ArrayList()
        checkRadioGroup()

        binding.btnAddProduct.setOnClickListener(this)
        binding.included.ivAddProductImage.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            binding.btnAddProduct.id -> {
                if (validateEntries().first) {
                    addProduct()
                } else {
                    showToast(validateEntries().second)
                }
            }

            binding.included.ivAddProductImage.id -> {
                addImage()
            }
        }
    }

    /**
     * fun for checking permission and launching image picker
     */
    private fun addImage() {
        if (Permission.checkStoragePermission(requireActivity())) {
            imagePicker.launch("image/*")
        } else {
            Permission.requestStoragePermission(requireActivity())
        }
    }

    /**
     * fun for adding product to api
     */
    private fun addProduct() {
        val productName = binding.included.etProductName.text.toString().trim { it <= ' ' }
        val sellingPrice = binding.included.etSellingPrice.text.toString().trim { it <= ' ' }
        val taxRate = binding.included.etTaxRate.text.toString().trim { it <= ' ' }

        viewModel.addProductResponse(
            productName, productType,
            sellingPrice.toDouble(), taxRate.toDouble()
        )
        viewModel.addProduct.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Success -> {
                    showToast("Product added successfully. ${state.data!!.product_id}")
                    activity?.onBackPressed()
                }
                is Response.Error -> {
                    showToast("Error in adding product.")
                }
            }
        }
    }

    /**
     * fun for check radio change listener
     */
    private fun checkRadioGroup() {
        binding.included.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                binding.included.rbProduct.id -> {
                    productType = "Product"
                }
                binding.included.rbService.id -> {
                    productType = "Service"
                }
            }
        }
    }

    /**
     * fun for validating user entries
     */
    private fun validateEntries(): Pair<Boolean, String> {
        val productName = binding.included.etProductName.text.toString().trim { it <= ' ' }
        val sellingPrice = binding.included.etSellingPrice.text.toString().trim { it <= ' ' }
        val taxRate = binding.included.etTaxRate.text.toString().trim { it <= ' ' }

        return viewModel.validateUserInput(productName, sellingPrice, taxRate)
    }

    /**
     * fun for showing toast
     */
    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}