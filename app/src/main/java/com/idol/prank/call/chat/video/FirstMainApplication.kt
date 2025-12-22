package com.idol.prank.call.chat.video

import android.app.Application

import com.idol.prank.call.chat.video.AdsModule.TinyDBs


class FirstMainApplication : Application() {
    lateinit var myTinyDBs: TinyDBs
    override fun onCreate() {
        myTinyDBs = TinyDBs(this)
        super.onCreate()
    }
}