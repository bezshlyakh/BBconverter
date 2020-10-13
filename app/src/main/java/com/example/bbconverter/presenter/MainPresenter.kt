package com.example.bbconverter.presenter

import android.graphics.Bitmap
import android.util.Log
import com.example.bbconverter.model.Model
import com.example.bbconverter.view.MainView
import moxy.MvpPresenter


class MainPresenter(private val mView : MainView) : MvpPresenter<MainView>() {

    companion object {
        private val TAG = MainPresenter::class.simpleName
    }

    var outDir: String = ""
    lateinit var bitmap: Bitmap
    lateinit var converter: ConverterPresenter

    fun imageClick() {
        mView.selectImageInAlbum()
        Log.v(TAG, "image path from MainPresenter $outDir")
    }

    fun btnClick() {
        val callback = { mView.showCompleteToast() }

        val mModel = Model(bitmap, outDir)
        converter = ConverterPresenter(mModel)
        converter.execute(callback)
        Log.v(TAG, "conversion begin")
    }

}