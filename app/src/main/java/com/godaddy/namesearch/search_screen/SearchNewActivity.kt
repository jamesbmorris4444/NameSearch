package com.godaddy.namesearch.search_screen

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ActivityDomainSearchNewBinding
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.utils.SearchCallbacks
import kotlinx.android.synthetic.main.activity_domain_search_new.view.*
import timber.log.Timber

class SearchNewActivity : AppCompatActivity(), SearchCallbacks {

    lateinit var repository: Repository
    lateinit var searchViewModel: SearchViewModel
    private lateinit var activityMainBinding: ActivityDomainSearchNewBinding
    private var preDrawComplete = false
    var listHeight = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository()
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_domain_search_new)
        activityMainBinding.lifecycleOwner = this
        searchViewModel = ViewModelProvider(this, SearchViewModelFactory(this)).get(SearchViewModel::class.java)
        activityMainBinding.searchViewModel = searchViewModel
        activityMainBinding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (!preDrawComplete) {
                    if (activityMainBinding.root.height > 0) {
                        preDrawComplete = true
                        activityMainBinding.root.viewTreeObserver.removeOnPreDrawListener(this)
                        listHeight = (activityMainBinding.root.view_cart_button.y - activityMainBinding.root.search_button.height - activityMainBinding.root.search_button.y - 100).toInt()
                        searchViewModel.initialize()
                        return false
                    }
                }
                return true
            }
        })
    }

    override fun fetchSearchActivity(): SearchNewActivity {
        return this
    }

    override fun fetchSearchRootView(): View {
        return activityMainBinding.root
    }
}