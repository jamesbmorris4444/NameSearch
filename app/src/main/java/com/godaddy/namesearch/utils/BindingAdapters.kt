package com.godaddy.namesearch.utils

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel

@BindingAdapter("background_color")
fun setBackgroundColor(view: Button, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}

@BindingAdapter("layout_height_dynamic")
fun setLayoutHeight(v: View, height: Int) {
    v.layoutParams.height = height
    v.requestLayout()
}

@BindingAdapter("recyclerViewViewModel")
fun setRecyclerViewViewModel(recyclerView: RecyclerView, viewModel: RecyclerViewViewModel) {
    viewModel.setupRecyclerView(recyclerView)
}