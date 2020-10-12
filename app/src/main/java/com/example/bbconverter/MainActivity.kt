package com.example.bbconverter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.bbconverter.presenter.MainPresenter
import com.example.bbconverter.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        private const val GALLERY_REQUEST_CODE = 1000
        private const val KEY_URI_IMG = "uriImg"
    }

    private val mPresenter = MainPresenter(this)
    private var mUri: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            mUri = savedInstanceState.getString(KEY_URI_IMG)!!
            IVGallery.setImageURI(Uri.parse(mUri))
        }
        setListeners()
    }

    private fun setListeners() {
        val imgListener = View.OnClickListener {
            mPresenter.imageClick()
        }
        IVGallery.setOnClickListener(imgListener)

        val btnListener = View.OnClickListener {
            mPresenter.btnClick(it.id)
        }
        convertBtn.setOnClickListener(btnListener)

    }


    @SuppressLint("QueryPermissionsNeeded")
    override fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            IVGallery.setImageURI(data?.data)
            mUri = data?.data.toString()
            mPresenter.setImgUri(mUri)
        }
    }

//    override fun setImage(string: String) {
//        IVGallery.setImageURI(Uri.parse(string))
//        mUri = string
//    }
//
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_URI_IMG, mUri)
    }


}