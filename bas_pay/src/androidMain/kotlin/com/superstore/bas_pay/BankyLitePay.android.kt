package com.superstore.bas_pay

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import com.superstore.bas_pay.models.BankyLitePayResponseModel
import com.ykb.banky.bankysdkmanager.BankyManager
import com.ykb.banky.bankysdkmanager.pay.PaymentResult


@Composable
actual fun bankyLitePay(txnToken: String, channel: String): BankyLitePayResponseModel {
    val context = LocalContext.current
    val activity = context as? Activity
    val intent = Intent()


    println("--------------+++++++_+_+_+_+_+__")
    println("txnToken: $txnToken")
    println("channel: $channel")
    println("--------------+++++++_+_+_+_+_+__")

    var paymentOutcome by remember { mutableStateOf<BankyLitePayResponseModel?>(null) }


    BankyManager.pay(context,txnToken ,channel, object : BankyManager.BankyPaymentCallback {
        override fun onSuccess(result: PaymentResult?) {
            Log.d("pay Success result:", result.toString())
            paymentOutcome = BankyLitePayResponseModel(status = true, result = result.toString(), error = null, code = null)
//            resultCallback?.success(result?.toString())
        }
        override fun onError(code: String, error: String) {
            Log.d("onError [login]", "[code]: $code [error]: $error")
//            resultCallback?.error(code,error,error)
            paymentOutcome = BankyLitePayResponseModel(status = false, result = null, error = error, code = code)
        }
    })
    println("-+++++++_+_+_+_+_+__ $paymentOutcome")
    println("--------------+++++++_+_+_+_+_+__ end BankyManager.pay()")

    if(paymentOutcome == null){
        return BankyLitePayResponseModel(status = false, result = null, error = "Payment not completed", code = "0")
    }
    return paymentOutcome!!
}