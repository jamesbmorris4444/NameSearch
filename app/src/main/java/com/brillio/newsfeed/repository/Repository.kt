package com.brillio.newsfeed.repository

import android.view.View
import android.widget.ProgressBar
import com.brillio.newsfeed.repository.network.APIClient
import com.brillio.newsfeed.repository.network.APIInterfaceNewsGET
import com.brillio.newsfeed.repository.storage.NewsFeedItem
import com.brillio.newsfeed.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository {

    private val apiInterfaceNewsGet: APIInterfaceNewsGET = APIClient.newsGetClient
    private lateinit var disposable: Disposable

    fun getNewsFeed(key: String, progressBar: ProgressBar, showNews: (newsResponse: NewsFeedItem) -> Unit, showNewsError: (error: String?) -> Unit) {
        disposable = apiInterfaceNewsGet.getNewsFeed(Constants.Q, Constants.FROM, Constants.SORT, key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe({ newsResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                showNews(newsResponse)
            },
            { throwable ->
                progressBar.visibility = View.GONE
                disposable?.dispose()
                showNewsError(throwable.message)
            })
    }

}