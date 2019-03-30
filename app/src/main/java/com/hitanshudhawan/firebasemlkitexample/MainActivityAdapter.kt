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
import com.hitanshudhawan.firebasemlkitexample.barcodescanning.BarcodeScanningActivity
import com.hitanshudhawan.firebasemlkitexample.custommodels.CustomModelsActivity
import com.hitanshudhawan.firebasemlkitexample.facedetection.FaceDetectionActivity
import com.hitanshudhawan.firebasemlkitexample.imagelabeling.ImageLabelingActivity
import com.hitanshudhawan.firebasemlkitexample.landmarkrecognition.LandmarkRecognitionActivity
import com.hitanshudhawan.firebasemlkitexample.languageidentification.LanguageIdentificationActivity
import com.hitanshudhawan.firebasemlkitexample.smartreply.SmartReplyActivity
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
                holder.heading.setText(R.string.text_recognition)
                holder.description.text = "Recognize and extract text from images"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, TextRecognitionActivity::class.java))
                }
            }

            1 -> {
                holder.image.setImageResource(R.mipmap.face_detection)
                holder.heading.setText(R.string.face_detection)
                holder.description.text = "Detect faces and facial landmarks"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, FaceDetectionActivity::class.java))
                }
            }

            2 -> {
                holder.image.setImageResource(R.mipmap.barcode_scanning)
                holder.heading.setText(R.string.barcode_scanning)
                holder.description.text = "Scan and process barcodes"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, BarcodeScanningActivity::class.java))
                }
            }

            3 -> {
                holder.image.setImageResource(R.mipmap.image_classification)
                holder.heading.setText(R.string.image_labeling)
                holder.description.text = "Identify objects, locations, activities, animal species, products, and more"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, ImageLabelingActivity::class.java))
                }
            }

            4 -> {
                holder.image.setImageResource(R.mipmap.landmark_recognition)
                holder.heading.setText(R.string.landmark_recognition)
                holder.description.text = "Identify popular landmarks in an image"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, LandmarkRecognitionActivity::class.java))
                }
            }

            5 -> {
                holder.image.setImageResource(R.mipmap.language_detection)
                holder.heading.setText(R.string.language_identification)
                holder.description.text = "With ML Kit's on-device language identification API, you can determine the language of a string of text"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, LanguageIdentificationActivity::class.java))
                }
            }

            6 -> {
                holder.image.setImageResource(R.mipmap.smart_reply)
                holder.heading.setText(R.string.smart_reply)
                holder.description.text = "With ML Kit's Smart Reply API, you can automatically generate relevant replies to messages"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, SmartReplyActivity::class.java))
                }
            }

            7 -> {
                holder.image.setImageResource(R.mipmap.custom_models)
                holder.heading.setText(R.string.custom_models)
                holder.description.text = "If you're an experienced ML developer and ML Kit's pre-built models don't meet your needs, you can use a custom TensorFlow Lite model with ML Kit"
                holder.view.setOnClickListener {
                    context.startActivity(Intent(context, CustomModelsActivity::class.java))
                }
            }
        }
    }

    override fun getItemCount() = 8

    class MainActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val image = itemView.findViewById<ImageView>(R.id.item_main_activity_image_view)!!
        val heading = itemView.findViewById<TextView>(R.id.item_main_activity_heading_text_view)!!
        val description = itemView.findViewById<TextView>(R.id.item_main_activity_description_text_view)!!
    }
}