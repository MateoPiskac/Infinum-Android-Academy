package infinuma.android.shows.ui.showsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinuma.android.shows.data.Show
import infinuma.android.shows.data.showsList

class ShowsViewModel : ViewModel() {

    private var _showsLiveData = MutableLiveData<List<Show>>()

    val showsLiveData: LiveData<List<Show>> get() = _showsLiveData

    fun fetchShows() {
        _showsLiveData.value = showsList
    }


}