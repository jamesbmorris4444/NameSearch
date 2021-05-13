package com.godaddy.namesearch.cart_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.CartCallbacks

class CartItemViewModel(private val cartCallbacks: CartCallbacks) : RecyclerViewItemViewModel<Domain>() {

    val name: ObservableField<String> = ObservableField("")
    val price: ObservableField<String> = ObservableField("")

    override fun setItem(item: Domain) {
        name.set(item.name)
        price.set(item.price)
    }

    fun onRemoveClicked(view: View) {
        cartCallbacks.fetchCartActivity().cartViewModel.onRemoveClicked(view)
    }

}