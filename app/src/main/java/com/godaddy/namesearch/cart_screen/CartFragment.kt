package com.godaddy.namesearch.cart_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.FragmentCartBinding
import com.godaddy.namesearch.utils.BaseFragment


class CartFragment : BaseFragment() {

    lateinit var cartViewModel: CartViewModel
    private var position = 0

    companion object {
        fun newInstance(position: Int): CartFragment {
            val fragment = CartFragment()
            fragment.position = position
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_cart, container, false) as FragmentCartBinding
        binding.lifecycleOwner = this
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(this)).get(CartViewModel::class.java)
        (binding as FragmentCartBinding).cartViewModel = cartViewModel
        cartViewModel.initialize()
        return binding.root
    }
}