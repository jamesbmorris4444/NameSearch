package com.godaddy.namesearch.cart_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemCartBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.GetFragment
import kotlinx.android.synthetic.main.item_cart.view.*


class CartAdapter(private val getFragment: GetFragment) : RecyclerViewFilterAdapter<Domain, CartItemViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val cartListItemBinding: ItemCartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_cart, parent, false)
        val cartItemViewModel = CartItemViewModel(getFragment)
        cartListItemBinding.cartItemViewModel = cartItemViewModel
        return DomainViewHolder(cartListItemBinding.root, cartItemViewModel, cartListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: CartItemViewModel, viewDataBinding: ItemCartBinding) :
        ItemViewHolder<Domain, CartItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Domain, CartItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.remove_button.tag = position
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

    override fun itemFilterable(item: Domain, constraint: String): Boolean {
        return true
    }

}