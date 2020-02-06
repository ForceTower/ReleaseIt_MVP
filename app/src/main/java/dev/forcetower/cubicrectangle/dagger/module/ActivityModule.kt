package dev.forcetower.cubicrectangle.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.cubicrectangle.dagger.module.home.HomeModule
import dev.forcetower.cubicrectangle.view.HomeActivity
import dev.forcetower.cubicrectangle.view.details.DetailedActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun home(): HomeActivity
    @ContributesAndroidInjector
    abstract fun details(): DetailedActivity
}