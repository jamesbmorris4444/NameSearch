package com.brillio.newsfeed.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brillio.newsfeed.recyclerview.RecyclerViewViewModel

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel?) {
    viewModel?.setupRecyclerView(recyclerView)
}