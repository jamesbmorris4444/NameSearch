package com.godaddy.namesearch.search_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ActivityDomainSearchNewBinding
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.utils.SearchCallbacks
import timber.log.Timber


class SearchNewActivity : AppCompatActivity(), SearchCallbacks {

    lateinit var repository: Repository
    lateinit var searchViewModel: SearchViewModel
    private lateinit var activityMainBinding: ActivityDomainSearchNewBinding
    lateinit var toolbar: Toolbar
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