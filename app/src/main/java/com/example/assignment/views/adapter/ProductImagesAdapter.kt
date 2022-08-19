package com.example.assignment.views.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.databinding.ItemAddProductImageBinding
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths


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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val string = imageList[position]

        val substring = string.substring(9)
        val filePath = "content:/$substring"

        val file = File(filePath)

        Log.d("product","sub - ${Paths.get(filePath).toFile().isFile}\n - ${file.absolutePath}")

        Glide.with(context)
            .load(filePath)
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