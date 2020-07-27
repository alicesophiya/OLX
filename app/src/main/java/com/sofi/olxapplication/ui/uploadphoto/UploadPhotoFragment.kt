package com.sofi.olxapplication.ui.uploadphoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.sofi.olxapplication.BaseFragment
import com.sofi.olxapplication.MainActivity
import com.sofi.olxapplication.R
import com.sofi.olxapplication.ui.PreviewImageActivity
import com.sofi.olxapplication.ui.uploadphoto.adapter.UploadImageAdapter
import com.sofi.olxapplication.utilities.Constants
import com.sofi.olxapplication.utilities.OnActivityResultData
import com.sofi.olxapplication.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import me.echodev.resizer.Resizer
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class UploadPhotoFragment : BaseFragment(), View.OnClickListener,
    UploadImageAdapter.ItemClickListener {


    private val imageUriList: ArrayList<String> =ArrayList()
    private var count = 0
    private lateinit var uploadTask: UploadTask
    private var imagesAdapter: UploadImageAdapter? = null
    private var selectedImageArrayList: ArrayList<String> = ArrayList()
    private var outputfileuri: String? = null
    internal var dialog: BottomSheetDialog? = null
    internal var selectedImage: File? = null
    internal var TAG = UploadPhotoFragment::class.java.simpleName
    val db = FirebaseFirestore.getInstance()
    internal lateinit var storageRef: StorageReference
    internal lateinit var imageRef: StorageReference
    internal lateinit var storage: FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val RootView = inflater.inflate(R.layout.fragment_upload_photo, container, false)
        return RootView;


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView_upload.layoutManager = GridLayoutManager(context, 3)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        listener()

        registerCallBackPhoto()


    }

    private fun listener() {

        imageViewChoosePhoto.setOnClickListener(this)
        buttonPreview.setOnClickListener(this)
        buttonUpload.setOnClickListener(this)
    }

    private fun registerCallBackPhoto() {
        (activity as MainActivity).getOnActivityResult(object : OnActivityResultData {
            override fun resultData(bundle: Bundle) {
                linearLayoutChoosePhoto.visibility = View.GONE
                recyclerView_upload.visibility = View.VISIBLE
                val mPaths = bundle.getStringArrayList(Constants.IMAGE_PATH)
                selectedImage = compressFile(File(mPaths?.get(0)))
                outputfileuri = selectedImage!!.absolutePath
                selectedImageArrayList.add(selectedImage!!.absolutePath)
                setAdapter()
            }
        })
    }

    private fun compressFile(file: File): File {
        val resizedImage: File = Resizer(activity)
            .setTargetLength(1024)
            .setQuality(80)
            .setOutputFormat("PNG")
            .setOutputFilename(file.name.substring(0, file.name.indexOf(".") + 1))
            .setOutputDirPath(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath)
            .setSourceImage(file)
            .getResizedFile()
        return resizedImage
    }

    private fun setAdapter() {
        if (imagesAdapter != null) {
            imagesAdapter!!.customNotify(selectedImageArrayList)
        } else {
            imagesAdapter = UploadImageAdapter(requireActivity(), selectedImageArrayList, this)
            recyclerView_upload.adapter = imagesAdapter

        }

    }

    private fun saveFileInFirebaseStorage() {
        showProgressBar()
        for (i in 0..selectedImageArrayList.size - 1) {
            val file = File(selectedImageArrayList[i])
            UploadImage(file, file.name, i)
            Log.d("FileName", file.name)
        }

    }

    private fun UploadImage(file: File, name: String, i: Int) {

        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child("images/$name")
        //creating and showing progress dialog

        //starting upload
        uploadTask = imageRef.putFile(Uri.fromFile(file))

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener(
            object : OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {

                    imageRef.downloadUrl.addOnSuccessListener {
                        count++
                        val url = it.toString()
                        Log.d("FirebaseManager", it.toString())
                        imageUriList.add(url)
                        if (count == selectedImageArrayList.size) {
                            postAd()
                        }
                    }
                }
            }
        )

    }

    private fun postAd() {
        showProgressBar()
        val documentId = db.collection(arguments?.getString(Constants.KEY)!!).document().id
        val documentData = hashMapOf(
            Constants.ADDRESS to arguments?.getString(Constants.ADDRESS),
            Constants.BRAND to arguments?.getString(Constants.BRAND),
            Constants.AD_DESCRIPTION to arguments?.getString(Constants.AD_DESCRIPTION),
            Constants.AD_TITLE to arguments?.getString(Constants.AD_TITLE),
            Constants.PHONE to arguments?.getString(Constants.PHONE),
            Constants.PRICE to arguments?.getString(Constants.PRICE),
            Constants.TYPE to arguments?.getString(Constants.KEY),
            Constants.YEAR to arguments?.getString(Constants.YEAR),
            Constants.Id to documentId,
        Constants.USER_ID to SharedPref(requireActivity()).getString(Constants.USER_ID),
        Constants.CREATED_DATE to Date(),
            "images" to imageUriList
        )
        db.collection(arguments?.getString(Constants.KEY)!!)
            .add(documentData)
            .addOnSuccessListener {
                UpdateDocumentId(it.id)
            }
    }

    private fun UpdateDocumentId(id: String) {

        val docData = mapOf(
            Constants.Id to id
        )
        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(id)
            .update(docData).addOnSuccessListener {
                hideProgressBar()
            Toast.makeText(requireActivity(),"Ad posted Succesfully",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_upload_photo)
            }
    }


    private fun showBottomSheetDialog() {

        val LayoutInflater =
            requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = LayoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        dialog = BottomSheetDialog(requireActivity())
        dialog?.setContentView(view)
        dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        val textViewGallery = dialog!!.findViewById<TextView>(R.id.textViewPhoto)
        val textViewCamera = dialog!!.findViewById<TextView>(R.id.textViewCamera)
        val textViewCancel = dialog!!.findViewById<TextView>(R.id.textViewCancel)

        textViewCamera?.setOnClickListener {
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.CAMERA)
        }
        textViewGallery?.setOnClickListener {
            dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.GALLERY)
        }
        textViewCancel?.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog?.show()
        val Ip = WindowManager.LayoutParams()
        val window = dialog?.window
        Ip.copyFrom(window!!.attributes)
        Ip.width = WindowManager.LayoutParams.MATCH_PARENT
        Ip.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = Ip
    }

    private fun chooseImage(mode: ImagePicker.Mode) {
        ImagePicker.Builder(requireActivity())
            .mode(mode)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(ImagePicker.Directory.DEFAULT)
            .extension(ImagePicker.Extension.PNG)
            .scale(600, 600)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageViewChoosePhoto -> {
                showBottomSheetDialog()
            }
            R.id.buttonPreview -> {
                if (selectedImage!=null){
                    startActivity(Intent(activity,PreviewImageActivity::class.java).putExtra("imageUri",outputfileuri))
                }
            }
            R.id.buttonUpload -> {
                if(selectedImage==null||!selectedImage!!.exists())
                    Toast.makeText(requireActivity(),"Please Select the Photo",Toast.LENGTH_SHORT).show()
                else
                    saveFileInFirebaseStorage()
            }
        }
    }

    override fun OnItemClick() {
        showBottomSheetDialog()
    }


}

