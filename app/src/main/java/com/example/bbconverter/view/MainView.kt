package com.example.bbconverter.view

import android.net.Uri
import moxy.MvpView

interface MainView : MvpView {

    fun selectImageInAlbum()

    fun showCompleteToast()

}