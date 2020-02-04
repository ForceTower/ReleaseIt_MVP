package dev.forcetower.cubicrectangle.dagger.module.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.cubicrectangle.view.listing.GenreListingFragment
import dev.forcetower.cubicrectangle.view.listing.GenresFragment

@Module
abstract class HomeModule {
    @ContributesAndroidInjector
    abstract fun listing(): GenreListingFragment
    @ContributesAndroidInjector
    abstract fun genres(): GenresFragment
}
