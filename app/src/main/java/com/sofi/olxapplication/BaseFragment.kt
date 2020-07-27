package com.sofi.olxapplication

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    lateinit var mDialg:Dialog
    open fun showProgressBar(){

        mDialg = Dialog(activity!!)
        mDialg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialg.setContentView(R.layout.dialog_progressbar)
        mDialg.setCancelable(true)
        mDialg.show()


    }

    open fun hideProgressBar() {
        mDialg.dismiss()

    }
}