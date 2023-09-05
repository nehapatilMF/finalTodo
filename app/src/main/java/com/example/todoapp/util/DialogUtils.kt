package com.example.todoapp.util

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun showAutoDismissAlertDialog(context: Context, message: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok"){dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()

        val handler = Handler()
        handler.postDelayed({
            alertDialog.dismiss()
        }, 2500) // 3 seconds (3000 milliseconds)
    }
}