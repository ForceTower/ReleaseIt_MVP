package dev.forcetower.cubicrectangle.dagger.module

import dagger.Module
import dagger.Provides
import dev.forcetower.cubicrectangle.core.services.FakeTMDbService
import dev.forcetower.cubicrectangle.core.services.TMDbService
import javax.inject.Singleton

@Module
class TestServiceModule {
    @Provides
    @Singleton
    fun provideFakeService(): TMDbService {
        return FakeTMDbService()
    }
}