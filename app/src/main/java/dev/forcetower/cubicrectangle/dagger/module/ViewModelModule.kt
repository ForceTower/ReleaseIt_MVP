package dev.forcetower.cubicrectangle.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.view.listing.ListingViewModel
import dev.forcetower.cubicrectangle.dagger.annotation.ViewModelKey

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListingViewModel::class)
    abstract fun bindHomeViewModel(vm: ListingViewModel): ViewModel

    @Binds
    abstract fun bindFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}