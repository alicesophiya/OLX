package com.sofi.olxapplication.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.R
import com.sofi.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_include_details,container,false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
//        if(arguments?.getString(Constants.KEY)!!.equals(Constants.CAR))
//        {
//            //compplete the rest of the code and tell me
//            //otherwise will look through iit tommorow the preview is not working kk
//
//
//    }
    }

    private fun listener() {


        textViewNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id){
            R.id.textViewNext->{
                sendData()
            }

        }
    }

    private fun sendData() {
        if (edBrand.text?.isEmpty()!!)
            edBrand.setError(getString(R.string.enter_brand_name))
      else if (edBrand.text?.isEmpty()!!)
            edBrand.setError(getString(R.string.enter_Phone_number))
        else {
            val bundle = Bundle()
            bundle.putString(Constants.BRAND,edBrand.text.toString())
            bundle.putString(Constants.YEAR,edYear.text.toString())
            bundle.putString(Constants.AD_TITLE,edTitle.text.toString())
            bundle.putString(Constants.AD_DESCRIPTION,edDescribe.text.toString())
            bundle.putString(Constants.ADDRESS,edPostalAddress.text.toString())
            bundle.putString(Constants.PRICE,edPrice.text.toString())
            bundle.putString(Constants.KM_DRIVEN,edKmDriven.text.toString())
            bundle.putString(Constants.KEY,arguments?.getString(Constants.KEY))
            findNavController().navigate(R.id.action_details_photo_myads,bundle)

        }

}}