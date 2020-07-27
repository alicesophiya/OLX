package com.sofi.olxapplication.ui.uploadphoto.adapter

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sofi.olxapplication.R

class UploadImageAdapter(internal var activity: Activity,
 internal var imagesArrayList: ArrayList<String>,
                         internal var itemclick: ItemClickListener
): RecyclerView.Adapter<UploadImageAdapter.ViewHolder>()
    {
        private lateinit var context: Context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            context = parent.context
            val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_upload_image,parent,false)
            return ViewHolder(viewHolder)
        }

        override fun getItemCount() : Int {
            return  imagesArrayList.size+1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         if(position<imagesArrayList.size){
             val  bitmap = BitmapFactory.decodeFile(imagesArrayList[position])
             holder.imageView.setImageBitmap(bitmap)
         }
          holder.itemView.setOnClickListener(View.OnClickListener {
              if (position==imagesArrayList.size){
                  itemclick.OnItemClick()
          }
        })
        }

        fun updateList(temp: ArrayList<String>) {
            imagesArrayList = temp
            notifyDataSetChanged()

        }

        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val imageView = itemView.findViewById<ImageView>(R.id.imageView_upload)


        }



        fun customNotify(selectedImageArrayList: java.util.ArrayList<String>) {

            this.imagesArrayList=selectedImageArrayList
            notifyDataSetChanged()
        }


        interface ItemClickListener{
            fun OnItemClick()
        }

}
