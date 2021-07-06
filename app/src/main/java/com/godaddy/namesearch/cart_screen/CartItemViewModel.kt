package com.godaddy.namesearch.cart_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.GetFragment

class CartItemViewModel(private val getFragment: GetFragment) : RecyclerViewItemViewModel<Domain>() {

    val name: ObservableField<String> = ObservableField("")
    val price: ObservableField<String> = ObservableField("")

    override fun setItem(item: Domain) {
        name.set(item.name)
        price.set(item.price)
    }

    fun onRemoveClicked(view: View) {
        (getFragment.getFragment() as CartFragment).cartViewModel.onRemoveClicked(view)
    }

}