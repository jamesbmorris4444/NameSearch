package com.godaddy.namesearch.cart_screen

import android.app.Application
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.activity.MainActivity
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.GetFragment


@Suppress("UNCHECKED_CAST")
class CartViewModelFactory(private val getFragment: GetFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(getFragment) as T
    }
}

@Suppress("UNCHECKED_CAST")
class CartViewModel(private val getFragment: GetFragment) : RecyclerViewViewModel(getFragment.getFragment().requireActivity().application) {

    override var adapter: CartAdapter = CartAdapter(getFragment)
    override val itemDecorator: RecyclerView.ItemDecoration? = DividerItemDecoration(getApplication<Application>().applicationContext, LinearLayout.VERTICAL)
    val isSelectEnabled: ObservableField<Boolean> = ObservableField(false)

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    fun initialize() {
        adapter.addAll(ShoppingCartNew.domains)
        updateButton()
    }

    fun onPayNowClicked() {
        (getFragment.getFragment().requireActivity() as MainActivity).loadPaymentFragment()
    }

    fun onRemoveClicked(view: View) {
        adapter.remove(view.tag as Int)
        adapter.notifyDataSetChanged()
        ShoppingCartNew.domains.removeAt(view.tag as Int)
        updateButton()
    }

    private fun updateButton() {
        if (ShoppingCartNew.domains.size == 0) {
            isSelectEnabled.set(false)
        } else {
            isSelectEnabled.set(true)
        }
    }

}