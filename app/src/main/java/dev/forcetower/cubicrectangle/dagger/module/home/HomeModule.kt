package dev.forcetower.cubicrectangle.dagger.module.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.cubicrectangle.view.listing.ListingFragment
import dev.forcetower.cubicrectangle.view.genres.GenresFragment

@Module
abstract class HomeModule {
    @ContributesAndroidInjector
    abstract fun listing(): ListingFragment
    @ContributesAndroidInjector
    abstract fun genres(): GenresFragment
}
