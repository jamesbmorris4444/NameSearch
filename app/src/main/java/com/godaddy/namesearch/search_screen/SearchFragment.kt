package com.godaddy.namesearch.search_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.FragmentSearchBinding
import com.godaddy.namesearch.recyclerview.RecyclerViewFragment
import com.godaddy.namesearch.repository.storage.Domain
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.concurrent.TimeUnit


class SearchFragment : RecyclerViewFragment() {

    lateinit var searchViewModel: SearchViewModel
    private var preDrawComplete = false
    private var position = 0
    override var adapter: SearchAdapter = SearchAdapter(this)
    override var itemDecorator: RecyclerView.ItemDecoration? = null
    var adapterList: MutableList<Domain> = mutableListOf()

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
        setUpLoadMoreListener()
        subscribeForData()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView((binding as FragmentSearchBinding).resultsListView)
        itemDecorator = DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(requireContext()) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    private val paginator = PublishProcessor.create<Int>()
    private var loading = false
    private var pageNumber = 1
    private val visibleThreshold = 1
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var compositeDisposable = CompositeDisposable()

    private fun setUpLoadMoreListener() {
        (binding as FragmentSearchBinding).resultsListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (binding as FragmentSearchBinding).resultsListView.layoutManager?.let { layoutManager ->
                    totalItemCount = layoutManager.itemCount
                    lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
                if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    pageNumber++
                    paginator.onNext(pageNumber)
                    loading = true
                }
            }
        })
    }

    private fun subscribeForData() {
        val disposable = paginator
            .onBackpressureDrop()
            .doOnNext {
                loading = true
                getNonNullActivity().progressBarVisibility.set(View.VISIBLE)
            }
            .concatMapSingle<Any> { page: Int ->
               dataFromNetwork(page)
                    .subscribeOn(Schedulers.io())
                    .doOnError { throwable -> }
                    .onErrorReturn { throwable -> ArrayList() }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items: Any ->
                adapter.addAll(items as List<Domain>)
                adapter.notifyDataSetChanged()
                loading = false
                getNonNullActivity().progressBarVisibility.set(View.GONE)
            }
        compositeDisposable.add(disposable)
        paginator.onNext(pageNumber)
    }

    private fun dataFromNetwork(page: Int): Single<List<Domain>> {
        return Single.just(true)
            .delay(2, TimeUnit.SECONDS)
            .map {
                val items: MutableList<Domain> = mutableListOf()
                for (i in 1..20) {
                    items.add(Domain(name = "Item $page.$i", price = "45.00", productId = 44, false))
                }
                items
            }
    }
}