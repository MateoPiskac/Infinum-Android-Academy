package infinuma.android.shows.ui.showsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import infinuma.android.shows.data.ShowsDatabase
import infinuma.android.shows.ui.showDetails.ShowDetailsViewModel

class ShowDetailsViewModelFactory(
    private val database : ShowsDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}