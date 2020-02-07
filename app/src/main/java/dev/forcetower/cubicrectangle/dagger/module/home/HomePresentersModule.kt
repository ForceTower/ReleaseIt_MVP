package dev.forcetower.cubicrectangle.dagger.module.home

import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.view.details.DetailsContract
import dev.forcetower.cubicrectangle.view.details.DetailsPresenter
import dev.forcetower.cubicrectangle.view.listing.ListingContract
import dev.forcetower.cubicrectangle.view.listing.ListingPresenter
import dev.forcetower.cubicrectangle.view.search.SearchContract
import dev.forcetower.cubicrectangle.view.search.SearchPresenter

@Module
object HomePresentersModule {
    @Provides
    fun provideListingPresenter(repository: MoviesRepository): ListingContract.Presenter {
        return ListingPresenter(repository)
    }

    @Provides
    fun provideSearchPresenter(repository: MoviesRepository): SearchContract.Presenter {
        return SearchPresenter(repository)
    }

    @Provides
    fun provideDetailsPresenter(repository: MoviesRepository): DetailsContract.Presenter {
        return DetailsPresenter(repository)
    }
}