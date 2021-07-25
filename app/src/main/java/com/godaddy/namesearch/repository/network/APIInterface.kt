package com.godaddy.namesearch.repository.network

import com.godaddy.namesearch.repository.storage.NewsFeedItem
import com.godaddy.namesearch.utils.Constants.EVERYTHING_PARAM
import com.godaddy.namesearch.utils.Constants.FROM_PARAM
import com.godaddy.namesearch.utils.Constants.KEY_PARAM
import com.godaddy.namesearch.utils.Constants.Q_PARAM
import com.godaddy.namesearch.utils.Constants.SORT_PARAM
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterfaceNewsGET {
    @GET(EVERYTHING_PARAM)
    fun getNewsFeed(
        @Query(Q_PARAM) query: String,
        @Query(FROM_PARAM) from: String,
        @Query(SORT_PARAM) sortBy: String,
        @Query(KEY_PARAM) key: String
    ): Single<NewsFeedItem>
}


