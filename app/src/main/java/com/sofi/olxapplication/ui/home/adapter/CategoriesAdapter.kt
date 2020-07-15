package com.sofi.olxapplication.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.CategoriesModel
import de.hdodenhof.circleimageview.CircleImageView

class CategoriesAdapter(var categoriesList:MutableList<CategoriesModel>,
                     var itemClickListener: IemClickListener
)
                        : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>()
{
       private lateinit var context:Context
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapter.ViewHolder {
       context = parent.context
              val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_categories,parent,false)
              return ViewHolder(viewHolder)
       }

       override fun getItemCount() : Int {
            return  categoriesList.size
       }

       override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          holder.TextViewTitle.setText(categoriesList.get(position).key)
           val into = Glide.with(context)
               .load(categoriesList.get(position).image)
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
              val imageView = itemView.findViewById<CircleImageView>(R.id.IVIcon)


       }
    interface IemClickListener{
        fun OnItemClick(position: Int)
    }
}