package com.godaddy.namesearch.utils

import android.view.View
import com.godaddy.namesearch.search_screen.SearchNewActivity

interface SearchCallbacks {
    fun fetchSearchActivity(): SearchNewActivity
    fun fetchSearchRootView(): View
}