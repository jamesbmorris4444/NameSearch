package com.godaddy.namesearch.search_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemDomainResultNewBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.SearchCallbacks


class SearchAdapter(private val searchCallbacks: SearchCallbacks) : RecyclerViewFilterAdapter<Domain, SearchItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val domainListItemBinding: ItemDomainResultNewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_domain_result_new, parent, false)
        val domainItemViewModel = SearchItemViewModel(searchCallbacks)
        domainListItemBinding.searchItemViewModel = domainItemViewModel
        return DomainViewHolder(domainListItemBinding.root, domainItemViewModel, domainListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: SearchItemViewModel, viewDataBinding: ItemDomainResultNewBinding) :
        ItemViewHolder<Domain, SearchItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Domain, SearchItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position
    }

    override fun itemFilterable(item: Domain, constraint: String): Boolean {
        return true
    }

}