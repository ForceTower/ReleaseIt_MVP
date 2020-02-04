package dev.forcetower.cubicrectangle.dagger.module.activities

import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.view.listing.ListingContract
import dev.forcetower.cubicrectangle.view.listing.ListingPresenter

@Module
object HomePresentersModule {
    @Provides
    fun provideListingPresenter(repository: MoviesRepository): ListingContract.Presenter {
        return ListingPresenter(repository)
    }
}