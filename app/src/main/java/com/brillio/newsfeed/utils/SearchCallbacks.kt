package com.brillio.newsfeed.utils

import android.view.View
import com.brillio.newsfeed.search_screen.SearchNewActivity

interface SearchCallbacks {
    fun fetchSearchActivity(): SearchNewActivity
    fun fetchSearchRootView(): View
}