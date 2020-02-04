package dev.forcetower.cubicrectangle.view

import android.os.Bundle
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseActivity
import dev.forcetower.cubicrectangle.core.extensions.inTransaction
import dev.forcetower.cubicrectangle.view.listing.GenreListingFragment

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                add(R.id.fragment_container, GenreListingFragment())
            }
        }
    }
}
