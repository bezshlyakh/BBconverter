package com.example.bbconverter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bbconverter.presenter.MainPresenter
import com.example.bbconverter.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity


class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        private const val GALLERY_REQUEST_CODE = 1000
        const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 124
        private const val KEY_URI_IMG = "uriImg"
        private val TAG = MainActivity::class.simpleName
    }

    private val mPresenter = MainPresenter(this)
    private var mUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable(KEY_URI_IMG)!!
            IVGallery.setImageURI(mUri)
            mPresenter.bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mUri)
            Log.v(TAG, "image path from onCreate $mUri")
        }
        if(checkPermissionWrite()){
            convertBtn.isEnabled = true
        } else {
            requestPermissionWrite()
        }
        setListeners()
    }

    private fun setListeners() {
        val imgListener = View.OnClickListener {
            mPresenter.imageClick()
        }
        IVGallery.setOnClickListener(imgListener)

        val btnListener = View.OnClickListener {

            if(checkPermissionWrite()){
                val internalDir = applicationContext.filesDir
                mPresenter.outDir = internalDir.absolutePath
                mPresenter.btnClick()
            } else {
                requestPermissionWrite()
            }
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

    @SuppressLint("ShowToast")
    override fun showCompleteToast() {
        Toast.makeText(this@MainActivity, "conversion completed", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            IVGallery.setImageURI(data?.data)
            mUri = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mUri)
            mPresenter.bitmap = bitmap
            Log.v(TAG, "image path from onActivityResult $mUri")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mUri.let{outState.putParcelable(KEY_URI_IMG, mUri)}
    }

    private fun checkPermissionWrite(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionWrite() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                convertBtn.isEnabled = true
            }
        }
    }

}