package com.godaddy.namesearch.search_screen

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.cart_screen.CartNewActivity
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.repository.storage.DomainSearchExactMatchResponse
import com.godaddy.namesearch.repository.storage.DomainSearchRecommendedResponse
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.*
import kotlinx.android.synthetic.main.activity_domain_search_new.view.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val searchCallbacks: SearchCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchCallbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModel(private val searchCallbacks: SearchCallbacks) : RecyclerViewViewModel(searchCallbacks.fetchSearchActivity().application) {

    @Inject
    lateinit var repository: Repository

    var searchTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    val listIsVisible: ObservableField<Int> = ObservableField(View.GONE)
    val listHeight: ObservableField<Int> = ObservableField(0)
    override var adapter: SearchAdapter = SearchAdapter(searchCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = DividerItemDecoration(getApplication<Application>().applicationContext, LinearLayout.VERTICAL)
    private var adapterList: MutableList<Domain> = mutableListOf()

    init {
        searchCallbacks.fetchSearchActivity()?.let { activity ->
            DaggerRepositorySearchDependencyInjector.builder()
                .repositorySearchInjectorModule(RepositorySearchInjectorModule(activity))
                .build()
                .inject(this)
        }
        searchCallbacks.fetchSearchRootView().view_cart_button.isEnabled = false
        searchCallbacks.fetchSearchRootView().search_button.isEnabled = false
    }

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
        listHeight.set(searchCallbacks.fetchSearchActivity().listHeight)
    }

    fun searchTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        searchCallbacks.fetchSearchRootView().search_button.isEnabled = string.isNotEmpty()
    }

    fun onContinueClicked() {
        searchCallbacks.fetchSearchActivity().startActivity(Intent(searchCallbacks.fetchSearchActivity(), CartNewActivity::class.java))
    }

    fun onSearchClicked(view: View) {
        if (searchTextInputEditText.get().isNotEmpty()) {
            searchCallbacks.fetchSearchRootView().search_progress_bar.visibility = View.VISIBLE
            repository.getExactListDomains(searchTextInputEditText.get(), this::showExactList)
        }
        Utils.hideKeyboard(view)
    }

    private fun showExactList(domainsExactListResponse: DomainSearchExactMatchResponse) {
        adapterList.clear()
        adapterList.add(Domain(name = domainsExactListResponse.domain.fqdn, price = domainsExactListResponse.products[0].priceInfo.currentPriceDisplay, productId = 0, selected = false))
        getRecommended()
    }

    private fun getRecommended() {
        if (searchTextInputEditText.get().isNotEmpty()) {
            repository.getSpinsListDomains(searchTextInputEditText.get(), this::showSpinsList)
        }
    }

    private fun showSpinsList(domainsSpinsListResponse: DomainSearchRecommendedResponse) {
        searchCallbacks.fetchSearchRootView().search_progress_bar.visibility = View.GONE
        for (index in domainsSpinsListResponse.domains.indices) {
            if (index >= domainsSpinsListResponse.products.size) {
                adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = "0.00", productId = 0, selected = false))
            } else {
                adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = domainsSpinsListResponse.products[index].priceInfo.currentPriceDisplay, productId = 0, selected = false))
            }
        }
        adapter.addAll(adapterList)
        listIsVisible.set(View.VISIBLE)
        updateButton()
    }

    fun onItemClicked(view: View) {
        val item: Domain = adapterList[view.tag as Int]
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
        searchCallbacks.fetchSearchRootView().view_cart_button.isEnabled = ShoppingCartNew.domains.size > 0
    }

}