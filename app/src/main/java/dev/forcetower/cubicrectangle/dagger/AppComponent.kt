package dev.forcetower.cubicrectangle.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.forcetower.cubicrectangle.CubicApp
import dev.forcetower.cubicrectangle.dagger.module.ActivityModule
import dev.forcetower.cubicrectangle.dagger.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<CubicApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: CubicApp): Builder
        fun build(): AppComponent
    }
}