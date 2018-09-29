package com.hitanshudhawan.firebasemlkitexample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.textrecognition.TextRecognitionActivity

class MainActivityAdapter(private val context: Context) : RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        return MainActivityViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_activity, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
        when (position) {

            0 -> {
                holder.image.setImageResource(R.mipmap.text_recognition)
                holder.heading.text = "Text recognition"
                holder.description.text = "Recognize and extract text from images"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, TextRecognitionActivity::class.java))
                }
            }

            1 -> {
                holder.image.setImageResource(R.mipmap.face_detection)
                holder.heading.text = "Face detection"
                holder.description.text = "Detect faces and facial landmarks"
            }

            2 -> {
                holder.image.setImageResource(R.mipmap.barcode_scanning)
                holder.heading.text = "Barcode scanning"
                holder.description.text = "Scan and process barcodes"
            }

            3 -> {
                holder.image.setImageResource(R.mipmap.image_classification)
                holder.heading.text = "Image labeling"
                holder.description.text = "Identify objects, locations, activities, animal species, products, and more"
            }
        }
    }

    override fun getItemCount() = 4

    class MainActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val image = itemView.findViewById<ImageView>(R.id.item_main_activity_image_view)!!
        val heading = itemView.findViewById<TextView>(R.id.item_main_activity_heading_text_view)!!
        val description = itemView.findViewById<TextView>(R.id.item_main_activity_description_text_view)!!
    }
}