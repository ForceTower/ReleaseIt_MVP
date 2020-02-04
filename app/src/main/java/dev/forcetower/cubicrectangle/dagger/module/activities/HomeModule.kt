package dev.forcetower.cubicrectangle.dagger.module.activities

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.view.listing.BaseGenreFragment
import dev.forcetower.cubicrectangle.view.listing.ListingPresenter

@Module
abstract class HomeModule {
    @ContributesAndroidInjector
    abstract fun genreListing(): BaseGenreFragment
}
