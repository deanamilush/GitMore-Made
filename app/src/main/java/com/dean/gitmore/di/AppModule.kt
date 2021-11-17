package com.dean.gitmore.di

import com.dean.core.domain.UserInteractor
import com.dean.core.domain.UserUseCase
import com.dean.gitmore.detail.DetailViewModel
import com.dean.gitmore.ui.follow.FollowViewModel
import com.dean.gitmore.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UserInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { FollowViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}