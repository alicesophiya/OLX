package com.sofi.olxapplication.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.CategoriesModel
import com.sofi.olxapplication.ui.sell.Adapter.SellAdapter
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment(),SellAdapter.IemClickListener {
    val db= FirebaseFirestore.getInstance()
    private lateinit var categoriesModel: MutableList<CategoriesModel>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCategoryList()
    }

    private fun getCategoryList() {
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
             categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {

        rv_offerings.layoutManager = GridLayoutManager(context,3)
        val sellAdapter = SellAdapter(categoriesModel, this)
        rv_offerings.adapter = sellAdapter

    }

    override fun OnItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("key", categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_sell_to_include_details, bundle)


    }
}