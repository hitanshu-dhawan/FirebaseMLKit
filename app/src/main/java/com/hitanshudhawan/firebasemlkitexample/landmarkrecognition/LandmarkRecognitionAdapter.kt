package com.hitanshudhawan.firebasemlkitexample.landmarkrecognition

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.R

class LandmarkRecognitionAdapter(private val context: Context, private val landmarkRecognitionModels: ArrayList<LandmarkRecognitionModel>) : RecyclerView.Adapter<LandmarkRecognitionAdapter.LandmarkRecognitionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkRecognitionViewHolder {
        return LandmarkRecognitionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_landmark_recognition, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LandmarkRecognitionViewHolder, position: Int) {
        holder.text1.text = landmarkRecognitionModels[position].text
        holder.text2.text = landmarkRecognitionModels[position].confidence.toString()
    }

    override fun getItemCount() = landmarkRecognitionModels.size

    class LandmarkRecognitionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_landmark_recognition_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_landmark_recognition_text_view2)!!
    }
}