package infinuma.android.shows.ui.showsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import infinuma.android.shows.data.ShowsDatabase

class ShowsViewModelFactory(
    private val database : ShowsDatabase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}