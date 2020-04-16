package com.proj.memeboard.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proj.memeboard.ui.login.LoginViewModel
import com.proj.memeboard.ui.main.detail.MemeDetailViewModel
import com.proj.memeboard.ui.main.home.MemeViewModel
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel
import com.proj.memeboard.ui.main.user.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MemeViewModel::class)
    abstract fun memeViewModel(viewModel: MemeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewMemeViewModel::class)
    abstract fun newMemeViewModel(viewModel: NewMemeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun userViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MemeDetailViewModel::class)
    abstract fun detailViewModel(viewModel: MemeDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}