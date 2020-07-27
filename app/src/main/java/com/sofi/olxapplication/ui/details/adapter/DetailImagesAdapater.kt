package com.sofi.olxapplication.ui.details.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.sofi.olxapplication.R

class DetailImagesAdapater(var context: Context,
                                        private val imagesArrayList: ArrayList<String>,
                                         var onItemClickListner: onItemClick
                                         ):PagerAdapter() {
    private var inflater:LayoutInflater? = null
    private var doNotifyDataSetChangedOnce=false

    init{
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        if (doNotifyDataSetChangedOnce){
     doNotifyDataSetChangedOnce = false


     notifyDataSetChanged()
 }
         return imagesArrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = inflater?.inflate(R.layout.adapter_images_detail,container,false)
        val imageView = view?.findViewById<ImageView>(R.id.imageView)


        imageView?.setOnClickListener(View.OnClickListener {
            onItemClickListner.onClick(position)
        })
        Glide.with(context)
            .load(imagesArrayList.get(position))
            .placeholder(R.drawable.big_placeholder)
            .into(imageView!!)

        container.addView(view,0)
        return view
    }

    override fun saveState(): Parcelable? {

        return null
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as View)
    }
   interface onItemClick{
       fun onClick(position: Int)
   }

}