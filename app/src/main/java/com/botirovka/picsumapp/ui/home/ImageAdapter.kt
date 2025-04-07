package com.botirovka.picsumapp.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.botirovka.picsumapp.R
import com.botirovka.picsumapp.domain.models.ImageBitmap


class ImageAdapter(
    context: Context,
    private val images: List<ImageBitmap>,
    private val onItemClick: (ImageBitmap) -> Unit,
    private val onShareClick: (ImageBitmap) -> Unit)
    : ArrayAdapter<ImageBitmap>(context, 0  , images) {

    private class ViewHolder {
        lateinit var ivImage: ImageView
        lateinit var tvImageId: TextView
        lateinit var btnShare: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            Log.d("mydebug", "getView: create new view ")
            view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
            holder = ViewHolder()
            holder.ivImage = view.findViewById(R.id.ivImage)
            holder.tvImageId = view.findViewById(R.id.tvImageId)
            holder.btnShare = view.findViewById(R.id.ivShareImageLink)
            view.tag = holder
        } else {
            Log.d("mydebug", "getView: using convertView ")
            view = convertView
            holder = view.tag as ViewHolder
        }

        val itemImage = images[position]

        holder.ivImage.setImageBitmap(itemImage.bitmap)

        holder.tvImageId.text = "ID ${itemImage.id}"

        holder.ivImage.setOnClickListener {
            onItemClick(itemImage)
        }

        holder.btnShare.setOnClickListener {
            onShareClick(itemImage)
        }

        return view
    }

    override fun getItemId(position: Int): Long {
        return images[position].id.toLong()
    }
    override fun hasStableIds(): Boolean {
        return true
    }
}