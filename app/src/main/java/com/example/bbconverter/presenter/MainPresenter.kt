package com.example.bbconverter.presenter

import com.example.bbconverter.model.Model
import com.example.bbconverter.view.MainView
import moxy.MvpPresenter


class MainPresenter(view : MainView) : MvpPresenter<MainView>() {

    var mModel = Model()
    var mView = view
    var mImgUri: String = ""

    fun imageClick() {
        mView.selectImageInAlbum()
    }

    fun setImgUri(string: String){
        mImgUri = string
    }

    fun btnClick(id: Int) {
        TODO("Not yet implemented")
    }



}