package com.godaddy.namesearch.search_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.FragmentSearchBinding
import com.godaddy.namesearch.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : BaseFragment() {

    lateinit var searchViewModel: SearchViewModel
    private var preDrawComplete = false
    private var position = 0

    companion object {
        fun newInstance(position: Int): SearchFragment {
            val fragment = SearchFragment()
            fragment.position = position
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_search, container, false) as FragmentSearchBinding
        binding.lifecycleOwner = this
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(this)).get(SearchViewModel::class.java)
        (binding as FragmentSearchBinding).searchViewModel = searchViewModel
        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (!preDrawComplete) {
                    if (requireView().height > 0) {
                        preDrawComplete = true
                        requireView().viewTreeObserver.removeOnPreDrawListener(this)
                        val listHeight = (requireView().view_cart_button.y - requireView().search_button.height - requireView().search_button.y - 100).toInt()
                        searchViewModel.initialize(listHeight)
                        return false
                    }
                }
                return true
            }
        })
        return binding.root
    }
}