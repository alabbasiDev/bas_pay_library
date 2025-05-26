package com.superstore.bas_pay

import androidx.compose.runtime.Composable
import com.superstore.bas_pay.models.BankyLitePayResponseModel

@Composable
expect fun BankyLitePay(txnToken: String, channel: String, onPaymentComplete: (BankyLitePayResponseModel) -> Unit )