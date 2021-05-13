package com.godaddy.namesearch.search_screen

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.R
import com.godaddy.namesearch.cart_screen.CartNewActivity
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.repository.storage.DomainSearchExactMatchResponse
import com.godaddy.namesearch.repository.storage.DomainSearchRecommendedResponse
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.DaggerRepositorySearchDependencyInjector
import com.godaddy.namesearch.utils.RepositorySearchInjectorModule
import com.godaddy.namesearch.utils.SearchCallbacks
import com.godaddy.namesearch.utils.Utils
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

    var searchTextInputEditText: ObservableField<String> = ObservableField("")
    fun searchTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) { }
    val listIsVisible: ObservableField<Int> = ObservableField(View.GONE)
    override var adapter: SearchAdapter = SearchAdapter(searchCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private var adapterList: MutableList<Domain> = mutableListOf()

    init {
        searchCallbacks.fetchSearchActivity()?.let { activity ->
            DaggerRepositorySearchDependencyInjector.builder()
                .repositorySearchInjectorModule(RepositorySearchInjectorModule(activity))
                .build()
                .inject(this)
        }
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

    fun onContinueClicked() {
        searchCallbacks.fetchSearchActivity().startActivity(Intent(searchCallbacks.fetchSearchActivity(), CartNewActivity::class.java))
    }

    fun onSearchClicked(view: View) {
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        searchTextInputEditText.get()?.let { query ->
            repository.getExactListDomains(query, progressBar, this::showExactList)
        }
        Utils.hideKeyboard(view)
    }

    private fun showExactList(domainsExactListResponse: DomainSearchExactMatchResponse) {
        adapterList.add(Domain(name = domainsExactListResponse.domain.fqdn, price = domainsExactListResponse.products[0].priceInfo.currentPriceDisplay, productId = 0, selected = false))
        getRecommended()
    }

    private fun getRecommended() {
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        searchTextInputEditText.get()?.let { query ->
            repository.getSpinsListDomains(query, progressBar, this::showSpinsList)
        }
    }

    private fun showSpinsList(domainsSpinsListResponse: DomainSearchRecommendedResponse) {
        for (index in domainsSpinsListResponse.domains.indices) {
            if (index >= domainsSpinsListResponse.products.size) {
                adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = "0.00", productId = 0, selected = false))
            } else {
                adapterList.add(Domain(name = domainsSpinsListResponse.domains[index].fqdn, price = domainsSpinsListResponse.products[index].priceInfo.currentPriceDisplay, productId = 0, selected = false))
            }
        }
        adapter.addAll(adapterList)
        listIsVisible.set(View.VISIBLE)
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
    }

}