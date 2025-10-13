package com.superstore.bas_pay

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext

import com.superstore.bas_pay.models.BankyLitePayResponseModel
import com.ykb.banky.bankysdkmanager.BankyManager
import com.ykb.banky.bankysdkmanager.pay.PaymentResult
import kotlinx.coroutines.flow.filterNotNull
import org.json.JSONObject


@Composable
actual fun BankyLitePay(txnToken: String, channel: String, onPaymentComplete: (BankyLitePayResponseModel) -> Unit) {
    val context = LocalContext.current
    var paymentOutcome by remember { mutableStateOf<BankyLitePayResponseModel?>(null) }
//    var hasPaymentAttempted by remember { mutableStateOf(false) }


//    LaunchedEffect(txnToken, channel, onPaymentComplete) {
//        if (!hasPaymentAttempted) {
            BankyManager.pay(context,txnToken ,channel, object : BankyManager.BankyPaymentCallback {
                override fun onSuccess(paymentResult: PaymentResult?) {
                    Log.d("pay Success result:", paymentResult.toString())
                    val outcome = BankyLitePayResponseModel(
                        status = true,
                        result = paymentResult!!.toJson().getString("externalReferenceId"),
//                        result = {
//                            val data = mutableMapOf<String, String>()
//                            data["status"] = result?.status.toString()
//                            data["result"] = result?.result.toString()
//                            data["error"] = result?.error.toString()
//                            data["code"] = result?.code.toString()
//                            data
//                        }(), // Handle nullable PaymentResult
                        error = null,
                        code = null
                    )
                    paymentOutcome = outcome
                    onPaymentComplete(outcome)
                }
                override fun onError(code: String, error: String) {
                    Log.d("BankyPay", "onError: [code]: $code [error]: $error")
                    val outcome = BankyLitePayResponseModel(
                        status = false,
                        result = null,
                        error = error,
                        code = code
                    )
                    paymentOutcome = outcome
                    onPaymentComplete(outcome)
                }
            })
//        }
//    }

}