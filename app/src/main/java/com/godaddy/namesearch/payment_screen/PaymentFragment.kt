package com.godaddy.namesearch.payment_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.FragmentPaymentBinding
import com.godaddy.namesearch.utils.BaseFragment


class PaymentFragment : BaseFragment() {

    lateinit var paymentViewModel: PaymentViewModel
    var selectedPaymentMethodView: View? = null

    companion object {
        fun newInstance(): PaymentFragment { return PaymentFragment() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_payment, container, false) as FragmentPaymentBinding
        binding.lifecycleOwner = this
        paymentViewModel = ViewModelProvider(this, PaymentViewModelFactory(this)).get(PaymentViewModel::class.java)
        (binding as FragmentPaymentBinding).paymentViewModel = paymentViewModel
        paymentViewModel.initialize()
        return binding.root
    }
}