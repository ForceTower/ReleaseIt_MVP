package dev.forcetower.cubicrectangle.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.cubicrectangle.dagger.module.activities.HomeModule
import dev.forcetower.cubicrectangle.dagger.module.activities.HomePresentersModule
import dev.forcetower.cubicrectangle.view.HomeActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [HomeModule::class, HomePresentersModule::class])
    abstract fun home(): HomeActivity
}