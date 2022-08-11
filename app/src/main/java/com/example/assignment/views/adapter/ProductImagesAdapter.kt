package com.example.assignment.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.databinding.ItemAddProductImageBinding
import com.example.assignment.model.dataclass.Product
import com.example.assignment.utils.ProductDiffUtils

class ProductImagesAdapter(
    private val context: Context,
    private var imageList: List<String>
) :
    RecyclerView.Adapter<ProductImagesAdapter.ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAddProductImageBinding.inflate(layoutInflater, parent, false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val string = imageList[position]

        Glide.with(context)
            .load(string)
            .into(holder.binding.ivAddImage)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImagesViewHolder(itemView: ItemAddProductImageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemAddProductImageBinding

        init {
            binding = itemView
        }
    }
}