package com.sofi.olxapplication.ui.browseCategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.DataItemModel
import com.sofi.olxapplication.ui.browseCategory.adapter.BrowseCategoryAdapter
import com.sofi.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_home.*

class BrowseCategoryFragment :BaseFragment(), BrowseCategoryAdapter.ItemClickListener {
    private lateinit var categoriesAdapter: BrowseCategoryAdapter
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val db =FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_browse,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_categories.layoutManager = LinearLayoutManager(context)
        getList()
        textListener()
    }

    private fun getList() {

        showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObjects(DataItemModel::class.java)
                setAdapter()
            }
    }

    private fun setAdapter() {

     categoriesAdapter = BrowseCategoryAdapter(dataItemModel,this)
        rv_categories.adapter=categoriesAdapter
    }

    override fun OnItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.DOCUMENT_ID,dataItemModel.get(position).Id)
        bundle.putString(Constants.KEY,dataItemModel.get(position).type)
        findNavController().navigate(R.id.action_browse_to_details,bundle)

    }
    private fun textListener() {
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filterList(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun filterList(s: String) {
        var temp: MutableList<DataItemModel> = ArrayList()
        for (data in dataItemModel){
            if (data.brand.contains(s.capitalize())||data.brand.contains(s)
                ||data.Title.contains(s.capitalize())||data.Title.contains(s)
            ){
                temp.add(data)
            }

        }
        categoriesAdapter.updateList(temp)
    }




}