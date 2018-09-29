package com.hitanshudhawan.firebasemlkitexample.imagelabeling

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.R

class ImageLabelingAdapter(private val context: Context, private val imageLabelingModels: ArrayList<ImageLabelingModel>) : RecyclerView.Adapter<ImageLabelingAdapter.ImageLabelingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLabelingViewHolder {
        return ImageLabelingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_labeling, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ImageLabelingViewHolder, position: Int) {
        holder.text1.text = imageLabelingModels[position].text
        holder.text2.text = imageLabelingModels[position].confidence.toString()
    }

    override fun getItemCount() = imageLabelingModels.size

    class ImageLabelingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_image_labeling_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_image_labeling_text_view2)!!
    }
}
