package com.godaddy.namesearch.search_screen

import android.graphics.Color
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.repository.storage.DomainSearchExactMatchResponse
import com.godaddy.namesearch.repository.storage.DomainSearchRecommendedResponse
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.GetFragment
import com.godaddy.namesearch.utils.NonNullObservableField
import com.godaddy.namesearch.utils.Utils


@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val getFragment: GetFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(getFragment) as T
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModel(private val getFragment: GetFragment) : AndroidViewModel(getFragment.getNonNullActivity().application) {

    var searchTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    val listIsVisible: ObservableField<Int> = ObservableField(View.GONE)
    val listHeight: ObservableField<Int> = ObservableField(0)
    val isSearchEnabled: ObservableField<Boolean> = ObservableField(false)
    val isContinueEnabled: ObservableField<Boolean> = ObservableField(false)

    fun initialize(height: Int) {
        listHeight.set(height)
    }

    fun searchTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        isSearchEnabled.set(string.isNotEmpty())
    }

    fun onContinueClicked() {
        getFragment.getNonNullActivity().loadCartFragment()
    }

    fun onSearchClicked(view: View) {
        if (searchTextInputEditText.get().isNotEmpty()) {
            getFragment.getNonNullActivity().progressBarVisibility.set(View.VISIBLE)
            Repository.getExactListDomains(searchTextInputEditText.get(), this::showExactList)
        }
        Utils.hideKeyboard(view)
    }

    private fun showExactList(domainsExactListResponse: DomainSearchExactMatchResponse) {
        (getFragment.getFragment() as SearchFragment).adapterList.clear()
        (getFragment.getFragment() as SearchFragment).adapterList.add(Domain(name = domainsExactListResponse.domain.fqdn, price = domainsExactListResponse.products[0].priceInfo.currentPriceDisplay, productId = 0, selected = false))
        getRecommended()
    }

    private fun getRecommended() {
        if (searchTextInputEditText.get().isNotEmpty()) {
            Repository.getSpinsListDomains(searchTextInputEditText.get(), this::showSpinsList)
        }
    }

    private fun showSpinsList(domainsSpinsListResponse: DomainSearchRecommendedResponse) {
        getFragment.getNonNullActivity().progressBarVisibility.set(View.GONE)
        for (index in domainsSpinsListResponse.domains.indices) {
            if (index >= domainsSpinsListResponse.products.size) {
                (getFragment.getFragment() as SearchFragment).adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = "0.00", productId = 0, selected = false))
            } else {
                (getFragment.getFragment() as SearchFragment).adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = domainsSpinsListResponse.products[index].priceInfo.currentPriceDisplay, productId = 0, selected = false))
            }
        }
        //adapter.addAll(adapterList)
        listIsVisible.set(View.VISIBLE)
        updateButton()
    }

    fun onItemClicked(view: View) {
        val item: Domain = (getFragment.getFragment() as SearchFragment).adapterList[view.tag as Int]
        ShoppingCartNew.domains = ShoppingCartNew.domains.toMutableList().also {
            if (ShoppingCartNew.domains.contains(item)) {
                it.remove(item)
                view.setBackgroundColor(Color.TRANSPARENT)
            } else {
                item.apply { it.add(item) }
                view.setBackgroundColor(Color.LTGRAY)
            }
        }
        updateButton()
    }

    private fun updateButton() {
        isContinueEnabled.set(ShoppingCartNew.domains.size > 0)
    }

}