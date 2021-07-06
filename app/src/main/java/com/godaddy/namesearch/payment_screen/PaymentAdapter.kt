package com.godaddy.namesearch.payment_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemPaymentBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.PaymentMethod
import com.godaddy.namesearch.utils.GetFragment


class PaymentAdapter(private val getFragment: GetFragment) : RecyclerViewFilterAdapter<PaymentMethod, PaymentItemViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val paymentListItemBinding: ItemPaymentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_payment, parent, false)
        val paymentItemViewModel = PaymentItemViewModel(getFragment)
        paymentListItemBinding.paymentItemViewModel = paymentItemViewModel
        return DomainViewHolder(paymentListItemBinding.root, paymentItemViewModel, paymentListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: PaymentItemViewModel, viewDataBinding: ItemPaymentBinding) :
        ItemViewHolder<PaymentMethod, PaymentItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<PaymentMethod, PaymentItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position
    }

    /**
     * Filter code goes below here if Filter is in use
     */

    private var adapterFilter: AdapterFilter? = null

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun itemFilterable(item: PaymentMethod, constraint: String): Boolean {
        return true
    }

}