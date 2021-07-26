package com.brillio.newsfeed.search_screen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brillio.newsfeed.R
import com.brillio.newsfeed.recyclerview.RecyclerViewViewModel
import com.brillio.newsfeed.repository.Repository
import com.brillio.newsfeed.repository.storage.NewsArticleItem
import com.brillio.newsfeed.repository.storage.NewsFeedItem
import com.brillio.newsfeed.utils.DaggerRepositorySearchDependencyInjector
import com.brillio.newsfeed.utils.RepositorySearchInjectorModule
import com.brillio.newsfeed.utils.SearchCallbacks
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
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
    private var loadingFinished = true
    private var redirect = false

    init {
        searchCallbacks.fetchSearchActivity()?.let { activity ->
            DaggerRepositorySearchDependencyInjector.builder()
                .repositorySearchInjectorModule(RepositorySearchInjectorModule(activity))
                .build()
                .inject(this)
        }
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        repository.getNewsFeed(getTopic(), progressBar, this::showNews, this::showNewsError)
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

    private fun setTopic(topic: String) {
        val sharedPreferences: SharedPreferences = searchCallbacks.fetchSearchActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("topic", topic)
        editor.apply()
    }

    private fun getTopic(): String {
        val sharedPreferences: SharedPreferences = searchCallbacks.fetchSearchActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        sharedPreferences.getString("topic", "weather")?.let {
            return it
        } ?: run {
            return "weather"
        }
    }

    private fun hideKeyboard() {
        val view: RelativeLayout = searchCallbacks.fetchSearchRootView().findViewById(R.id.container)
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    var topicEditText: ObservableField<String> = ObservableField("")
    fun topicEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        if (string.indexOf(",,") >= 0) {
            setTopic(string.toString().substring(0, string.length - 2))
            onNewTopic()
        }
    }

    private fun onNewTopic() {
        hideKeyboard()
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        repository.getNewsFeed(getTopic(), progressBar, this::showNews, this::showNewsError)
    }

    @SuppressLint("LogNotTimber")
    private fun showNewsError(message: String?) {
        message?.let {
            Log.d("ERROR","Error msg=$it")
        }
    }

    private fun showNews(newsResponse: NewsFeedItem) {
        topicEditText.set("")
        searchCallbacks.fetchSearchRootView().findViewById<TextInputEditText>(R.id.topic).clearFocus()
        adapterList.clear()
        this.newsResponse = newsResponse
        for (index in 1 until newsResponse.articles.size) {
            adapterList.add(newsResponse.articles[index])
        }
        adapter.addAll(adapterList)
        if (newsResponse.articles.isNotEmpty()) {
            Glide.with(searchCallbacks.fetchSearchActivity()).load(newsResponse.articles[0].urlToImage).into(searchCallbacks.fetchSearchRootView().findViewById(R.id.image))
            searchCallbacks.fetchSearchRootView().findViewById<TextView>(R.id.author).text = newsResponse.articles[0].author
            searchCallbacks.fetchSearchRootView().findViewById<TextView>(R.id.title).text = newsResponse.articles[0].title
        }
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
        handleClick(view.tag as Int + 1)
    }

    fun onTopItemClicked() {
        handleClick(0)
    }

    private fun handleClick(index: Int) {
        Glide.with(searchCallbacks.fetchSearchActivity()).load(newsResponse.articles[index].urlToImage).into(searchCallbacks.fetchSearchRootView().findViewById(R.id.web_image))
        val webView = searchCallbacks.fetchSearchRootView().findViewById(R.id.web_view) as WebView
        setWebViewListener(webView)
        val progressBar: ProgressBar = searchCallbacks.fetchSearchActivity().findViewById(R.id.search_progress_bar)
        progressBar.visibility = View.VISIBLE
        webView.loadUrl(newsResponse.articles[index].url)
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