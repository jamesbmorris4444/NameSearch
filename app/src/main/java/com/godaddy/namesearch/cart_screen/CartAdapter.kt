package com.godaddy.namesearch.cart_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemCartNewBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.CartCallbacks
import com.godaddy.namesearch.utils.DaggerRepositoryCartDependencyInjector
import com.godaddy.namesearch.utils.RepositoryCartInjectorModule


class CartAdapter(private val cartCallbacks: CartCallbacks) : RecyclerViewFilterAdapter<Domain, CartItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    init {
        cartCallbacks.fetchCartActivity()?.let { activity ->
            DaggerRepositoryCartDependencyInjector.builder()
                .repositoryCartInjectorModule(RepositoryCartInjectorModule(activity))
                .build()
                .inject(this)
        }
    }

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val cartListItemBinding: ItemCartNewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_cart_new, parent, false)
        val cartItemViewModel = CartItemViewModel(cartCallbacks)
        cartListItemBinding.cartItemViewModel = cartItemViewModel
        return DomainViewHolder(cartListItemBinding.root, cartItemViewModel, cartListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: CartItemViewModel, viewDataBinding: ItemCartNewBinding) :
        ItemViewHolder<Domain, CartItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Domain, CartItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        val buttonView = holder.itemView.findViewById(R.id.remove_button) as ImageButton
        buttonView.tag = position
    }

    override fun itemFilterable(item: Domain, constraint: String): Boolean {
        return true
    }

}