package com.hitanshudhawan.firebasemlkitexample.facedetection

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.R

class FaceDetectionAdapter(private val context: Context, private val faceDetectionModels: List<FaceDetectionModel>) : RecyclerView.Adapter<FaceDetectionAdapter.FaceDetectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaceDetectionViewHolder {
        return FaceDetectionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_face_detection, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FaceDetectionViewHolder, position: Int) {
        holder.text1.text = "Face${faceDetectionModels[position].id}"
        holder.text2.text = faceDetectionModels[position].text
    }

    override fun getItemCount() = faceDetectionModels.size

    class FaceDetectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_face_detection_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_face_detection_text_view2)!!
    }

}
