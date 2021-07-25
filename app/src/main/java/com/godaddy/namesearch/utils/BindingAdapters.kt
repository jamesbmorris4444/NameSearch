package com.godaddy.namesearch.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel?) {
    viewModel?.setupRecyclerView(recyclerView)
}