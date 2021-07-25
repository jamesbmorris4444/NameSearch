package com.godaddy.namesearch.search_screen

import android.view.View
import androidx.databinding.ObservableField
import com.godaddy.namesearch.recyclerview.RecyclerViewItemViewModel
import com.godaddy.namesearch.repository.storage.NewsArticleItem
import com.godaddy.namesearch.utils.SearchCallbacks

class SearchItemViewModel(private val searchCallbacks: SearchCallbacks) : RecyclerViewItemViewModel<NewsArticleItem>() {

    val name: ObservableField<String> = ObservableField("")
    val author: ObservableField<String> = ObservableField("")
    val title: ObservableField<String> = ObservableField("")
    val url: ObservableField<String> = ObservableField("")
    val urlToImage: ObservableField<String> = ObservableField("")
    val publishedAt: ObservableField<String> = ObservableField("")
    val content: ObservableField<String> = ObservableField("")

    override fun setItem(item: NewsArticleItem) {
        name.set(item.source.name)
        author.set(item.author)
        title.set(item.title)
        url.set(item.url)
        urlToImage.set(item.urlToImage)
        publishedAt.set(item.publishedAt)
        content.set(item.content)
    }

    fun onItemClicked(view: View) {
        searchCallbacks.fetchSearchActivity().searchViewModel.onItemClicked(view)
    }

}
