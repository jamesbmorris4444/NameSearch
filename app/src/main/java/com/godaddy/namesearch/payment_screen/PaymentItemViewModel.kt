package com.godaddy.namesearch.payment_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.PaymentMethod
import com.godaddy.namesearch.utils.PaymentCallbacks

class PaymentItemViewModel(private val paymentCallbacks: PaymentCallbacks) : RecyclerViewItemViewModel<PaymentMethod>() {

    val name: ObservableField<String> = ObservableField("")
    val lastFour: ObservableField<String> = ObservableField("")

    override fun setItem(item: PaymentMethod) {
        name.set(item.name)
        if (item.lastFour == null) {
            lastFour.set(item.displayFormattedEmail)
        } else {
            lastFour.set(item.lastFour)
        }
    }

    fun onItemClicked(view: View) {
        paymentCallbacks.fetchPaymentActivity().paymentViewModel.onItemClicked(view)
    }

}