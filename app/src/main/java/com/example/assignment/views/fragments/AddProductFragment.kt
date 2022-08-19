package com.example.assignment.views.fragments

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import java.io.File
import java.io.FileOutputStream


class AddProductFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddProductBinding? = null

    private val binding get() = _binding!!

    // for injecting view model from koin
    private val viewModel = ProductComponent().productViewModel

    // for product type
    private var productType = "Product"

    // product images list
    private lateinit var imagesList: ArrayList<String>

    // hashmap for @key = file name and @value = file
    private lateinit var hashMap: HashMap<String, File>

    // file name from content resolver
    private var fileName = ""

    private lateinit var imageUri: Uri

    // for loading image from gallery
    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {
            imageUri = uri

            // for knowing extension of image
            val imageExtension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(requireContext().contentResolver.getType(uri))

            // This will give image file name from uri
            uri.let { returnUri ->
                requireContext().contentResolver.query(
                    returnUri, null, null,
                    null, null
                )
            }?.let { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileName = cursor.getString(nameIndex)
                cursor.close()
            }

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
        hashMap = HashMap()
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

        val file = File(requireActivity().cacheDir, fileName)
        file.createNewFile()
        val fileOutputStream = FileOutputStream(file)
        val byteArray = viewModel.getByteArray(requireContext(), imageUri)
        fileOutputStream.write(byteArray)
        fileOutputStream.close()

        hashMap[file.name] = file

        viewModel.addProductResponse(
            productName, productType,
            sellingPrice.toDouble(), taxRate.toDouble(),
            hashMap
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