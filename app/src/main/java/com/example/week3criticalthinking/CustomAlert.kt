package com.example.week3criticalthinking

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class CustomAlert(context: Context, message: String) {
    init {

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setTitle("Alert !")
        builder.setCancelable(true)
        builder.setPositiveButton("I Understand!") { dialog, which ->
            dialog.dismiss()
        }

        builder.create()
        builder.show()
    }
}