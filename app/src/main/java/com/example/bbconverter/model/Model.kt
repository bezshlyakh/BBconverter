package com.example.bbconverter.model

import android.graphics.Bitmap
import io.reactivex.rxjava3.core.Flowable
import java.io.ByteArrayOutputStream


class Model(private val bitmap: Bitmap, private val outDir: String) {

    companion object {
        private val TAG = Model::class.simpleName
    }

    fun getBitmap(): Flowable<ByteArray> = Flowable.fromCallable {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        stream.toByteArray()
    }

    fun getMUri(): String {
        return outDir
    }

}