package infinuma.android.shows.ui.showDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import infinuma.android.shows.data.Show

class ShowDetailsViewModel : ViewModel() {

    private var _showLiveData = MutableLiveData<MutableList<ReviewListItem>>()
    val showLiveData: LiveData<MutableList<ReviewListItem>> get() = _showLiveData
    private lateinit var show: Show

    init {
        _showLiveData.value = mutableListOf()
    }
    fun setShow(showClicked: Show) {
        show = showClicked
    }
    fun getShow() : Show =show

    fun getInitialReviewList(): MutableList<ReviewListItem> {
        _showLiveData.value?.add(ReviewListItem.ShowDetails(show.image, show.description))
        _showLiveData.value?.add(ReviewListItem.NoReviews)
        return _showLiveData.value!!
    }

    fun removeNoReviewsText() {
        _showLiveData.value?.removeIf { it == ReviewListItem.NoReviews }

    }
    fun addReviews(reviewsList: MutableList<ReviewListItem>){
        _showLiveData.value=reviewsList.toMutableList()
    }


}