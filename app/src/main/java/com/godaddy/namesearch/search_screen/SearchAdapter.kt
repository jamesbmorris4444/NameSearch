package com.godaddy.namesearch.search_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ItemNewsFeedResultNewBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFilterAdapter
import com.godaddy.namesearch.repository.storage.NewsArticleItem
import com.godaddy.namesearch.utils.DaggerRepositorySearchDependencyInjector
import com.godaddy.namesearch.utils.RepositorySearchInjectorModule
import com.godaddy.namesearch.utils.SearchCallbacks


class SearchAdapter(private val searchCallbacks: SearchCallbacks) : RecyclerViewFilterAdapter<NewsArticleItem, SearchItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    init {
        DaggerRepositorySearchDependencyInjector.builder()
            .repositorySearchInjectorModule(RepositorySearchInjectorModule(searchCallbacks.fetchSearchActivity()))
            .build()
            .inject(this)
    }

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val domainListItemBinding: ItemNewsFeedResultNewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_news_feed_result_new, parent, false)
        val domainItemViewModel = SearchItemViewModel(searchCallbacks)
        domainListItemBinding.searchItemViewModel = domainItemViewModel
        return NewsFeedViewHolder(domainListItemBinding.root, domainItemViewModel, domainListItemBinding)
    }

    inner class NewsFeedViewHolder internal constructor(itemView: View, viewModel: SearchItemViewModel, viewDataBinding: ItemNewsFeedResultNewBinding) :
        ItemViewHolder<NewsArticleItem, SearchItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<NewsArticleItem, SearchItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.tag = position
        Glide.with(searchCallbacks.fetchSearchActivity()).load(itemList[position].urlToImage).into(holder.itemView.findViewById(R.id.image))
    }

    override fun itemFilterable(item: NewsArticleItem, constraint: String): Boolean {
        return true
    }

}