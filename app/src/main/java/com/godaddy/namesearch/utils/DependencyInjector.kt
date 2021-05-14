package com.godaddy.namesearch.utils

import com.godaddy.namesearch.cart_screen.CartNewActivity
import com.godaddy.namesearch.cart_screen.CartViewModel
import com.godaddy.namesearch.login_screen.LoginNewActivity
import com.godaddy.namesearch.login_screen.LoginViewModel
import com.godaddy.namesearch.payment_screen.PaymentNewActivity
import com.godaddy.namesearch.payment_screen.PaymentViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.search_screen.SearchNewActivity
import com.godaddy.namesearch.search_screen.SearchViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryLoginInjectorModule::class])
interface RepositoryLoginDependencyInjector {
    fun inject(viewModel: LoginViewModel)
}

@Singleton
@Component(modules = [RepositorySearchInjectorModule::class])
interface RepositorySearchDependencyInjector {
    fun inject(viewModel: SearchViewModel)
}

@Singleton
@Component(modules = [RepositoryCartInjectorModule::class])
interface RepositoryCartDependencyInjector {
    fun inject(viewModel: CartViewModel)
}

@Singleton
@Component(modules = [RepositoryPaymentInjectorModule::class])
interface RepositoryPaymentDependencyInjector {
    fun inject(viewModel: PaymentViewModel)
}

@Module
class RepositoryLoginInjectorModule(private val activity: LoginNewActivity) {
    @Provides
    @Singleton
    fun repositoryLoginProvider() : Repository {
        return activity.repository
    }
}

@Module
class RepositorySearchInjectorModule(private val activity: SearchNewActivity) {
    @Provides
    @Singleton
    fun repositorySearchProvider() : Repository {
        return activity.repository
    }
}

@Module
class RepositoryCartInjectorModule(private val activity: CartNewActivity) {
    @Provides
    @Singleton
    fun repositoryCartProvider() : Repository {
        return activity.repository
    }
}

@Module
class RepositoryPaymentInjectorModule(private val activity: PaymentNewActivity) {
    @Provides
    @Singleton
    fun repositoryPaymentProvider() : Repository {
        return activity.repository
    }
}