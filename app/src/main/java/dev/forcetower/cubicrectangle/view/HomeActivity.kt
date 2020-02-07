package dev.forcetower.cubicrectangle.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseActivity
import dev.forcetower.cubicrectangle.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun showSnack(string: String, duration: Int) {
        getSnackInstance(string, duration)?.show()
    }

    override fun getSnackInstance(string: String, duration: Int): Snackbar? {
        return Snackbar.make(binding.root, string, duration)
    }
}
