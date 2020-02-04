package dev.forcetower.cubicrectangle.core.base

import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    open fun showSnack(string: String, duration: Int = Snackbar.LENGTH_SHORT) {}
    open fun getSnackInstance(string: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar? = null
}