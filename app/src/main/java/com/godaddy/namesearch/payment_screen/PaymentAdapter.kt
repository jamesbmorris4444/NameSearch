package com.godaddy.namesearch.payment_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemPaymentNewBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.PaymentMethod
import com.godaddy.namesearch.utils.PaymentCallbacks


class PaymentAdapter(private val paymentCallbacks: PaymentCallbacks) : RecyclerViewFilterAdapter<PaymentMethod, PaymentItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val paymentListItemBinding: ItemPaymentNewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_payment_new, parent, false)
        val paymentItemViewModel = PaymentItemViewModel(paymentCallbacks)
        paymentListItemBinding.paymentItemViewModel = paymentItemViewModel
        return DomainViewHolder(paymentListItemBinding.root, paymentItemViewModel, paymentListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: PaymentItemViewModel, viewDataBinding: ItemPaymentNewBinding) :
        ItemViewHolder<PaymentMethod, PaymentItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<PaymentMethod, PaymentItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position
    }

    override fun itemFilterable(item: PaymentMethod, constraint: String): Boolean {
        return true
    }

}