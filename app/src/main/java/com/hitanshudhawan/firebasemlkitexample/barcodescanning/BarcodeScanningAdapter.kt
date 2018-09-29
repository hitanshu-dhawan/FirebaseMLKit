package com.hitanshudhawan.firebasemlkitexample.barcodescanning

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hitanshudhawan.firebasemlkitexample.R

class BarcodeScanningAdapter(private val context: Context, private val barcodeScanningModels: List<BarcodeScanningModel>) : RecyclerView.Adapter<BarcodeScanningAdapter.BarcodeScanningViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeScanningViewHolder {
        return BarcodeScanningViewHolder(LayoutInflater.from(context).inflate(R.layout.item_barcode_scanning, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BarcodeScanningViewHolder, position: Int) {
        holder.text1.text = barcodeScanningModels[position].id.toString()
        holder.text2.text = barcodeScanningModels[position].text
    }

    override fun getItemCount() = barcodeScanningModels.size

    class BarcodeScanningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        val text1 = itemView.findViewById<TextView>(R.id.item_barcode_scanning_text_view1)!!
        val text2 = itemView.findViewById<TextView>(R.id.item_barcode_scanning_text_view2)!!
    }
}
