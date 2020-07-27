package com.sofi.olxapplication.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.DataItemModel
import com.sofi.olxapplication.ui.PreviewImageActivity
import com.sofi.olxapplication.ui.details.adapter.DetailImagesAdapater
import com.sofi.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment :BaseFragment(), DetailImagesAdapater.onItemClick {
    private lateinit var dataItemModel: DataItemModel
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_details,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments?.getString(Constants.KEY).equals(Constants.CAR)){
            KmDrivenLL.visibility=View.VISIBLE}
        getItemDetails()

        clickListener()

    }

    private fun clickListener() {

        tvCall.setOnClickListener(View.OnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" +dataItemModel.phone)
            startActivity(dialIntent)
        })
    }

    private fun getItemDetails() {

     showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(arguments?.getString(Constants.DOCUMENT_ID)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObject(DataItemModel::class.java)!!
                setData()
                setpagerAdapter()
            }
    }

    private fun setpagerAdapter() {
        val detailImagesAdapater = DetailImagesAdapater(requireContext(),dataItemModel.images,this)
        viewPager.adapter =  detailImagesAdapater
        viewPager.offscreenPageLimit =1

    }

    private fun setData() {
        tvprice.text = Constants.CURRENCY_SYMBOL +dataItemModel.price
        tvTitle.text = dataItemModel.Title
        tvAddress.text = dataItemModel.address
        tvBrand.text = dataItemModel.brand
        tvAddress.text = dataItemModel.description
        tvPhone.text = dataItemModel.phone
        tvYear.text = dataItemModel.year
        val dateFormat = SimpleDateFormat(
            "dd MMM", Locale.getDefault()
        )
        tvDate_.text = dateFormat.format(dataItemModel.createdDate)
        if(dataItemModel.type==Constants.CAR){
            tvKm_Driven.text = dataItemModel.kmdriven
        }





    }

    override fun onClick(position: Int) {

        startActivity(Intent(activity,PreviewImageActivity::class.java)
            .putExtra("imageUrl",dataItemModel.images.get(position)))
    }
}