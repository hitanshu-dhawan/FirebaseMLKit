package com.hitanshudhawan.firebasemlkitexample.custommodels

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.R

class CustomModelsAdapter(private val context: Context, private val customModelsModels: ArrayList<CustomModelsModel>) : RecyclerView.Adapter<CustomModelsAdapter.CustomModelsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomModelsViewHolder {
        return CustomModelsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_custom_models, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomModelsViewHolder, position: Int) {
        holder.text1.text = customModelsModels[position].text
        holder.text2.text = customModelsModels[position].confidence.toString()
    }

    override fun getItemCount() = customModelsModels.size

    class CustomModelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_custom_models_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_custom_models_text_view2)!!
    }
}