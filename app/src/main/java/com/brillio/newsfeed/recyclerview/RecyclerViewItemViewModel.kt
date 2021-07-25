package com.brillio.newsfeed.recyclerview

import androidx.lifecycle.ViewModel

abstract class RecyclerViewItemViewModel<T> : ViewModel() {

    abstract fun setItem(item: T)

}