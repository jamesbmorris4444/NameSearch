package com.godaddy.namesearch.payment_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.PaymentMethod
import com.godaddy.namesearch.utils.GetFragment

class PaymentItemViewModel(private val getFragment: GetFragment) : RecyclerViewItemViewModel<PaymentMethod>() {

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
        (getFragment.getFragment() as PaymentFragment).paymentViewModel.onItemClicked(view)
    }

}