package com.godaddy.namesearch.search_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemSearchBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.Domain
import com.godaddy.namesearch.utils.GetFragment


class SearchAdapter(private val getFragment: GetFragment) : RecyclerViewFilterAdapter<Domain, SearchItemViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomainViewHolder {
        val domainListItemBinding: ItemSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_search, parent, false)
        val domainItemViewModel = SearchItemViewModel(getFragment)
        domainListItemBinding.searchItemViewModel = domainItemViewModel
        return DomainViewHolder(domainListItemBinding.root, domainItemViewModel, domainListItemBinding)
    }

    inner class DomainViewHolder internal constructor(itemView: View, viewModel: SearchItemViewModel, viewDataBinding: ItemSearchBinding) :
        ItemViewHolder<Domain, SearchItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Domain, SearchItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position
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