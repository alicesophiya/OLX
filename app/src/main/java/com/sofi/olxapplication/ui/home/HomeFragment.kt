package com.sofi.olxapplication.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sofi.olxapplication.R
import com.sofi.olxapplication.model.CategoriesModel
import com.sofi.olxapplication.ui.home.adapter.CategoriesAdapter
import com.sofi.olxapplication.utilities.Constants
import com.sofi.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), CategoriesAdapter.IemClickListener {

    private lateinit var categoriesAdapter: CategoriesAdapter
    val db= FirebaseFirestore.getInstance()
private lateinit var categoriesModel: MutableList<CategoriesModel>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvCityName.text = SharedPref(requireActivity()).getString(Constants.CITY_NAME)

        getCategoryList()

        textListener()

    }

    private fun textListener() {
        edSearch.addTextChangedListener(object :TextWatcher{
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
        var temp: MutableList<CategoriesModel> = ArrayList()
        for (data in categoriesModel){
            if (data.key.contains(s.capitalize())||data.key.contains(s)){
                temp.add(data)
            }

        }
        categoriesAdapter.updateList(temp)
    }


    private fun getCategoryList() {
        db.collection("Categories").get().addOnSuccessListener {
           categoriesModel = it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {

        rv_categories.layoutManager = GridLayoutManager(context,3)
        categoriesAdapter = CategoriesAdapter(categoriesModel,this)
        rv_categories.adapter = categoriesAdapter

    }

    override fun OnItemClick(position: Int) {
        Toast.makeText(context,"Hey"+position,Toast.LENGTH_SHORT).show()
    }
}