package com.sofi.olxapplication.ui.myAds.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.DataItemModel
import java.text.SimpleDateFormat

class adapter(
    var dataItemModel : MutableList<DataItemModel>,
    var mClickListener : ItemClickListener)

    :RecyclerView.Adapter<adapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter.ViewHolder {
        context = parent.context
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_ads, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dataItemModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.TextViewPrice.setText("Rs." + dataItemModel.get(position).price)
        holder.TextViewBrand.setText( dataItemModel.get(position).brand)
        holder.TextViewAddress.setText(dataItemModel.get(position).address)
        Glide.with(context)
            .load(dataItemModel.get(position).images.get(0))
            .into(holder.imageView)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = sdf.format(dataItemModel[position].createdDate?.time!!)
        holder.TextViewDate.setText(formattedDate)
        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.OnItemClick(position)
        })
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TextViewPrice = itemView.findViewById<TextView>(R.id.tvprice)
        val TextViewBrand = itemView.findViewById<TextView>(R.id.tvBrand)
        val TextViewAddress = itemView.findViewById<TextView>(R.id.tvAddress)
        val TextViewDate = itemView.findViewById<TextView>(R.id.tvDate)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView_myads)


    }

    interface ItemClickListener {
        fun OnItemClick(position: Int)
    }

}