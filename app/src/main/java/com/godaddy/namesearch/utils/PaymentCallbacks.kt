package com.godaddy.namesearch.utils

import android.view.View
import com.godaddy.namesearch.payment_screen.PaymentNewActivity

interface PaymentCallbacks {
    fun fetchPaymentActivity(): PaymentNewActivity
    fun fetchPaymentRootView(): View
}