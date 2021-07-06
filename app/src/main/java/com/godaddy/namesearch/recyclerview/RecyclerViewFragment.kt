package com.godaddy.namesearch.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.utils.BaseFragment

abstract class RecyclerViewFragment : BaseFragment() {

    abstract val adapter: RecyclerView.Adapter<*>

    abstract val itemDecorator: RecyclerView.ItemDecoration?

    abstract fun setLayoutManager(): RecyclerView.LayoutManager

    fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = setLayoutManager()
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.setItemViewCacheSize(50)
        itemDecorator?.let {
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(it)
            }
        }
        recyclerView.adapter = adapter
    }

}
