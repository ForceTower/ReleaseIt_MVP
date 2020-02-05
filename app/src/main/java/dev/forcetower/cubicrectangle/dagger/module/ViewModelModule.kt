package dev.forcetower.cubicrectangle.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.view.listing.ListingViewModel
import dev.forcetower.cubicrectangle.dagger.annotation.ViewModelKey
import dev.forcetower.cubicrectangle.view.details.DetailsViewModel
import dev.forcetower.cubicrectangle.view.search.SearchViewModel

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListingViewModel::class)
    abstract fun bindHomeViewModel(vm: ListingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(vm: DetailsViewModel): ViewModel

    @Binds
    abstract fun bindFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory
}