package com.botirovka.picsumapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.botirovka.picsumapp.databinding.ItemImageBinding
import com.botirovka.picsumapp.domain.models.ImageBitmap


class ImageAdapter(private val onItemClick: (ImageBitmap) -> Unit,
                   private val onShareClick: (ImageBitmap) -> Unit)
    : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var imageList: List<ImageBitmap> = emptyList()

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: ImageBitmap) {
                binding.ivMore.visibility = View.VISIBLE
                binding.ivImage.setImageBitmap(image.bitmap)
                binding.tvImageId.text = "Id: ${image.id}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val binding = holder.binding
        val imageItem = imageList[position]
        holder.bind(imageItem)
        holder.itemView.setOnClickListener {
            onItemClick(imageItem)
        }
        binding.ivMore.setOnClickListener {
            onShareClick(imageItem)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun submitList(newItems: List<ImageBitmap>) {
        imageList = newItems
        notifyDataSetChanged()
    }
}