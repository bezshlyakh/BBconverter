package com.example.bbconverter.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.bbconverter.model.Model
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream as OutputStream1

class ConverterPresenter(private val model: Model) {

    companion object {
        private val TAG = ConverterPresenter::class.simpleName
    }

    val disposables = CompositeDisposable()

    private fun convertJPGtoPNG(callback: () -> Unit) {
        val disposable =
            model.getBitmap()
                .map { convertByteArrAndSave(it as ByteArray) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { s: Any ->
                        callback()
                        Log.i(TAG, s.toString())
                    },
                    { e -> Log.d(TAG, "Error: conversion failed: $e") },
                    { Log.v(TAG, "Conversion completed") }
                )
        disposables.add(disposable)
    }

    private fun convertByteArrAndSave(bitmap: ByteArray) {
        val file = File(model.getMUri() + File.separator + "temporary_file.png")
        Log.v(TAG, "file path ${file.absolutePath}")
        val stream: OutputStream1 = FileOutputStream(file)
        val btm = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
        btm.compress(Bitmap.CompressFormat.PNG, 0, stream)
        stream.flush()
        stream.close()
    }

    fun execute(callback: () -> Unit) {
        convertJPGtoPNG(callback)
    }
}