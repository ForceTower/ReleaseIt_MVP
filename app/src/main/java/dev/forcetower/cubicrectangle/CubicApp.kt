package dev.forcetower.cubicrectangle

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.support.DaggerApplication
import dev.forcetower.cubicrectangle.dagger.AppComponent
import dev.forcetower.cubicrectangle.dagger.DaggerAppComponent
import timber.log.Timber

class CubicApp : DaggerApplication() {
    private val component: AppComponent by lazy { createComponent() }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun applicationInjector() = component

}