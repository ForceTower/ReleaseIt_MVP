package dev.forcetower.cubicrectangle.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseActivity
import dev.forcetower.cubicrectangle.core.extensions.inTransaction
import dev.forcetower.cubicrectangle.databinding.ActivityHomeBinding
import dev.forcetower.cubicrectangle.view.listing.GenresFragment

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                add(R.id.fragment_container, GenresFragment())
            }
        }
    }
}
