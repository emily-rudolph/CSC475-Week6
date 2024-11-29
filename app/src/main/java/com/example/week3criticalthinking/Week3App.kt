package com.example.week3criticalthinking
import android.app.Application
import android.content.Context


class Week3App: Application() {
    companion object {
        private lateinit var instance: Week3App

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}