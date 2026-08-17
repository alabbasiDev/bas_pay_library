package com.superstore.bas_pay

import androidx.compose.runtime.Composable
import com.superstore.bas_pay.models.DownloadInvoiceModel
import com.superstore.bas_pay.models.DownloadInvoiceResult

@Composable
expect fun ShareInvoicePdf(
    request: DownloadInvoiceModel,
    onComplete: (DownloadInvoiceResult) -> Unit,
)
