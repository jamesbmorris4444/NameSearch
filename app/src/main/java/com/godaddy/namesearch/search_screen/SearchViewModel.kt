package com.godaddy.namesearch.search_screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.godaddy.namesearch.R
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.NewsArticleItem
import com.godaddy.namesearch.repository.storage.NewsFeedItem
import com.godaddy.namesearch.utils.Constants
import com.godaddy.namesearch.utils.DaggerRepositorySearchDependencyInjector
import com.godaddy.namesearch.utils.RepositorySearchInjectorModule
import com.godaddy.namesearch.utils.SearchCallbacks
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

    override var adapter: SearchAdapter = SearchAdapter(searchCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private var adapterList: MutableList<NewsArticleItem> = mutableListOf()
    private lateinit var newsResponse: NewsFeedItem
    val webViewVisibility = ObservableField(View.GONE)
    val listVisibility = ObservableField(View.VISIBLE)
    var loadingFinished = true
    var redirect = false


    init {
        searchCallbacks.fetchSearchActivity()?.let { activity ->
            DaggerRepositorySearchDependencyInjector.builder()
                .repositorySearchInjectorModule(RepositorySearchInjectorModule(activity))
                .build()
                .inject(this)
        }
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        repository.getNewsFeed(Constants.KEY, progressBar, this::showNews, this::showNewsError)
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

    private fun showNewsError(message: String?) {
        message?.let {
            Log.d("JIMX","msg="+it)
        }
    }

    private fun showNews(newsResponse: NewsFeedItem) {
        this.newsResponse = newsResponse
        for (index in 1 until newsResponse.articles.size) {
            adapterList.add(newsResponse.articles[index])
        }
        adapter.addAll(adapterList)
        Glide.with(searchCallbacks.fetchSearchActivity()).load(newsResponse.articles[0].urlToImage).into(searchCallbacks.fetchSearchRootView().findViewById(R.id.image))
        searchCallbacks.fetchSearchRootView().findViewById<TextView>(R.id.author).text = newsResponse.articles[0].author
        searchCallbacks.fetchSearchRootView().findViewById<TextView>(R.id.title).text = newsResponse.articles[0].title
    }

    fun onBackClicked() {
        val intent = Intent(searchCallbacks.fetchSearchActivity(), SearchNewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        searchCallbacks.fetchSearchActivity().startActivity(intent)
        searchCallbacks.fetchSearchActivity().finish()
        webViewVisibility.set(View.GONE)
        listVisibility.set(View.VISIBLE)
    }

    fun onItemClicked(view: View) {
        Glide.with(searchCallbacks.fetchSearchActivity()).load(newsResponse.articles[view.tag as Int + 1].urlToImage).into(searchCallbacks.fetchSearchRootView().findViewById(R.id.web_image))
        val webView = searchCallbacks.fetchSearchRootView().findViewById(R.id.web_view) as WebView
        setWebViewListener(webView)
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        webView.loadUrl(newsResponse.articles[view.tag as Int + 1].url)
    }

    fun onTopItemClicked() {
        Glide.with(searchCallbacks.fetchSearchActivity()).load(newsResponse.articles[0].urlToImage).into(searchCallbacks.fetchSearchRootView().findViewById(R.id.web_image))
        val webView = searchCallbacks.fetchSearchRootView().findViewById(R.id.web_view) as WebView
        setWebViewListener(webView)
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        webView.loadUrl(newsResponse.articles[0].url)
    }

    private fun setWebViewListener(webView: WebView) {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, urlNewString: String): Boolean {
                if (!loadingFinished) {
                    redirect = true
                }
                loadingFinished = false
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!redirect) {
                    loadingFinished = true
                    val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
                    progressBar.visibility = View.GONE
                    webViewVisibility.set(View.VISIBLE)
                    listVisibility.set(View.GONE)
                } else {
                    redirect = false
                }
            }
        }
    }

    class VerticalDividerItemDecoration(context: Context, @DrawableRes dividerRes: Int) : RecyclerView.ItemDecoration() {
        private val divider: Drawable? = ContextCompat.getDrawable(context, dividerRes)
        override fun onDrawOver(c: Canvas, parent: RecyclerView) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                divider?.let { dividerDrawable ->
                    val bottom = top + divider.intrinsicHeight
                    dividerDrawable.setBounds(left, top, right, bottom)
                    dividerDrawable.draw(c)
                }
            }
        }
    }

}