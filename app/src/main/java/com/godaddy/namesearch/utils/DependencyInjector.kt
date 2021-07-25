package com.godaddy.namesearch.utils


import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.search_screen.SearchAdapter
import com.godaddy.namesearch.search_screen.SearchNewActivity
import com.godaddy.namesearch.search_screen.SearchViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositorySearchInjectorModule::class])
interface RepositorySearchDependencyInjector {
    fun inject(activity: SearchNewActivity)
    fun inject(viewModel: SearchViewModel)
    fun inject(adapter: SearchAdapter)
}

@Module
class RepositorySearchInjectorModule(private val activity: SearchNewActivity) {
    @Provides
    @Singleton
    fun repositorySearchProvider() : Repository {
        return activity.repository
    }
}