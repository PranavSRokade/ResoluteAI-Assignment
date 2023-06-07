package com.spn.resoluteaiassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class Adapter(qrCodeInformationList: ArrayList<String>): RecyclerView.Adapter<Adapter.ViewHolder>(){
    val qrCodeInformationList: ArrayList<String>
    val originalQrCodeInformationList: ArrayList<String>

    init{
        this.qrCodeInformationList = qrCodeInformationList
        originalQrCodeInformationList = qrCodeInformationList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.information.text = qrCodeInformationList[position]
    }

    override fun getItemCount(): Int {
        return qrCodeInformationList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var information: TextView

        init{
            information = itemView.findViewById(R.id.information)
        }
    }
}
