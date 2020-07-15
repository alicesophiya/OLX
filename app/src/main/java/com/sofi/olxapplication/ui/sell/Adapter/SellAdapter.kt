package com.sofi.olxapplication.ui.sell.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.CategoriesModel

class SellAdapter(var categoriesList:MutableList<CategoriesModel>,
                  var itemClickListener: IemClickListener
)
                        : RecyclerView.Adapter<SellAdapter.ViewHolder>()
{
       private lateinit var context:Context
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellAdapter.ViewHolder {
       context = parent.context

              val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_sell,parent,false)
              return ViewHolder(viewHolder)
       }

       override fun getItemCount() : Int {
            return  categoriesList.size
       }

       override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          holder.TextViewTitle.text = categoriesList.get(position).key
           Glide.with(context)
               .load(categoriesList.get(position).image_bw)
               .into(holder.imageView)

           holder.itemView.setOnClickListener(View.OnClickListener{
                   itemClickListener.OnItemClick(position)
                    })
                 }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val TextViewTitle = itemView.findViewById<TextView>(R.id.tvTitle)
              val imageView = itemView.findViewById<ImageView>(R.id.IVIcon)


       }
    interface IemClickListener{
        fun OnItemClick(position: Int)
    }
}