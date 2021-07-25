package com.godaddy.namesearch.repository

import android.view.View
import android.widget.ProgressBar
import com.godaddy.namesearch.repository.network.APIClient
import com.godaddy.namesearch.repository.network.APIInterfaceNewsGET
import com.godaddy.namesearch.repository.storage.NewsFeedItem
import com.godaddy.namesearch.utils.Constants
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