package com.example.assignment.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.databinding.ItemProductBinding
import com.example.assignment.model.dataclass.Product
import com.example.assignment.utils.ProductDiffUtils

class ProductAdapter(
    private val context: Context,
    private var productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(product.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivProduct)

            tvProductName.text = product.product_name
            // price for allowing only 2 decimals
            val price = String.format("%.2f",product.price)
            tvProductPrice.text = context.resources.getString(R.string.rupee, price)
            tvProductType.text = product.product_type
            // for converting tax to int
            val tax = product.tax.toInt()
            tvProductTax.text = context.resources.getString(R.string.tax, tax.toString())

        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProductList(newProductList: List<Product>)
    {
        val diffUtil = ProductDiffUtils(productList, newProductList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        productList = newProductList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ProductViewHolder(itemView: ItemProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemProductBinding

        init {
            binding = itemView
        }
    }
}