package com.superstore.bas_pay

import android.util.Log

actual fun myLogger(text: String?) {
    if(text!= null){
        Log.d("SDK MyLogger", text)
    }
}