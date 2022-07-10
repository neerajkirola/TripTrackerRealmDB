package com.emt.tracker.util

import android.content.Context
import android.widget.Toast


object ToastUtils {
    fun showError(message: String, context: Context) {
        getToast(message, context).show()
    }

    fun showShortMessage(message: String, context: Context) {
        getToast(message, context, Toast.LENGTH_SHORT).show()
    }

    private fun getToast(message: String, context: Context): Toast {
        return getToast(message, context, Toast.LENGTH_LONG)
    }

    private fun getToast(message: String, context: Context, length: Int): Toast {
        return Toast.makeText(context, message, length)
    }
}