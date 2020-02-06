package dev.forcetower.cubicrectangle.dagger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.forcetower.cubicrectangle.TestApplication
import dev.forcetower.cubicrectangle.dagger.module.ActivityModule
import dev.forcetower.cubicrectangle.dagger.module.AppModule
import dev.forcetower.cubicrectangle.dagger.module.TestServiceModule
import dev.forcetower.cubicrectangle.dagger.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivityModule::class,
    AppModule::class,
    TestServiceModule::class,
    ViewModelModule::class
])
interface TestComponent : AndroidInjector<TestApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TestComponent
    }
}