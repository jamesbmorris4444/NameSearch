package com.brillio.newsfeed.utils


import com.brillio.newsfeed.repository.Repository
import com.brillio.newsfeed.search_screen.SearchAdapter
import com.brillio.newsfeed.search_screen.SearchNewActivity
import com.brillio.newsfeed.search_screen.SearchViewModel
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