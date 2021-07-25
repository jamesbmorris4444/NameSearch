package com.brillio.newsfeed.search_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brillio.newsfeed.R
import com.brillio.newsfeed.databinding.ActivityDomainSearchNewBinding
import com.brillio.newsfeed.repository.Repository
import com.brillio.newsfeed.utils.SearchCallbacks
import timber.log.Timber


class SearchNewActivity : AppCompatActivity(), SearchCallbacks {

    lateinit var repository: Repository
    lateinit var searchViewModel: SearchViewModel
    private lateinit var activityMainBinding: ActivityDomainSearchNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository()
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_domain_search_new)
        activityMainBinding.lifecycleOwner = this
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(this)).get(SearchViewModel::class.java)
        activityMainBinding.searchViewModel = searchViewModel
        activityMainBinding.resultsListView.addItemDecoration(SearchViewModel.VerticalDividerItemDecoration(this, R.drawable.divider_drawable))
    }

    override fun fetchSearchActivity(): SearchNewActivity {
        return this
    }

    override fun fetchSearchRootView(): View {
        return activityMainBinding.root
    }
}