package com.sofi.olxapplication.ui.myAds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.DataItemModel
import com.sofi.olxapplication.ui.myAds.adapter.MyAdsAdapter
import com.sofi.olxapplication.utilities.Constants
import com.sofi.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_myads.*

class MyAdsFragment : BaseFragment(),
    MyAdsAdapter.ItemClickListener {

    private var documentDataList: MutableList<DataItemModel> = ArrayList()
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val db = FirebaseFirestore.getInstance()
    var myAdsAdapter: MyAdsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_myads, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMyAds()
        rv_ads.layoutManager = LinearLayoutManager(context)

    }

    private fun getMyAds() {
        showProgressBar()
        db.collection(Constants.CATEGORIES)
            .get().addOnSuccessListener {
                for (i in it.documents) {
                    getDataFromKeys(i.getString(Constants.KEY)!!)
                }
            }

    }

    private fun getDataFromKeys(key: String) {
        db.collection(key)
            .whereEqualTo("user_id", SharedPref(requireActivity()).getString(Constants.USER_ID))
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObjects(DataItemModel::class.java)
                documentDataList.addAll(dataItemModel)
                setAdapter()
            }
    }

    private fun setAdapter() {
        myAdsAdapter =  MyAdsAdapter(documentDataList, this)
        if (rv_ads != null)
            rv_ads.adapter = myAdsAdapter
    }

    override fun OnItemClick(position: Int) {

    var bundle = Bundle()
        bundle.putString(Constants.KEY,documentDataList.get(position).type)
        bundle.putString(Constants.DOCUMENT_ID,documentDataList.get(position).Id)
        findNavController().navigate(R.id.action_my_ads_to_details,bundle)
    }
}
