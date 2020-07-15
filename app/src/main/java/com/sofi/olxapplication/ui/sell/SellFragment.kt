package com.sofi.olxapplication.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.CategoriesModel
import com.sofi.olxapplication.ui.home.adapter.CategoriesAdapter
import com.sofi.olxapplication.ui.sell.Adapter.SellAdapter
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : Fragment(), CategoriesAdapter.IemClickListener, SellAdapter.IemClickListener {
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
        db.collection("Categories").get().addOnSuccessListener {
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


    }
}