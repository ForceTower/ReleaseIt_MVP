package dev.forcetower.cubicrectangle.view.listing

import androidx.lifecycle.ViewModel
import javax.inject.Inject

// This view model is used only to retain instance state.
// I believe it is a better implementation than onRetainNonConfigurationInstanceState on the Activity
// (and view model will override this)
// Yes, i hate when recyclerview resets current position or the data fetched is lost,
class ListingViewModel @Inject constructor(
    val presenter: ListingContract.Presenter
) : ViewModel()