package com.godaddy.namesearch.search_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.SearchCallbacks

class SearchItemViewModel(private val searchCallbacks: SearchCallbacks) : RecyclerViewItemViewModel<Domain>() {

    val name: ObservableField<String> = ObservableField("")
    val price: ObservableField<String> = ObservableField("")

    override fun setItem(item: Domain) {
        name.set(item.name)
        price.set(item.price)
    }

    fun onItemClicked(view: View) {
        searchCallbacks.fetchSearchActivity().searchViewModel.onItemClicked(view)
    }

}
