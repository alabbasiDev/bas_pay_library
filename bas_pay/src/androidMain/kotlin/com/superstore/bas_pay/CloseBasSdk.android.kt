package com.superstore.bas_pay

import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import androidx.compose.runtime.Composable
import android.content.Intent

@Composable
actual fun closeBasSdk(result: String): Boolean {
    val context = LocalContext.current
    val activity = context as? Activity
    val resultIntent = Intent()

    resultIntent.putExtra("result", result)
    activity?.setResult(Activity.RESULT_OK, resultIntent)
    activity?.finish()
    return true
}