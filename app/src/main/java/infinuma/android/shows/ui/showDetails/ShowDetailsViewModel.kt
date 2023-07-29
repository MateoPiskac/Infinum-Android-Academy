package infinuma.android.shows.ui.showDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.Show
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class ShowDetailsViewModel : ViewModel() {

    private var _showLiveData = MutableLiveData<MutableList<ReviewListItem>>()
    val showLiveData: LiveData<MutableList<ReviewListItem>> get() = _showLiveData
    private lateinit var show: Show
    var reviewListUpdated : MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        _showLiveData.value = mutableListOf()
    }

    fun setShow(showClicked: Show) {
        show = showClicked
        _showLiveData.value = getInitialReviewList()
    }

    fun getShow(): Show = show

    private fun getInitialReviewList(): MutableList<ReviewListItem> {
        _showLiveData.value?.add(ReviewListItem.ShowDetails(show.image, show.description))
        _showLiveData.value?.add(ReviewListItem.Rating(show.averageRating ?: 0f, show.numOfReviews))
        return _showLiveData.value!!
    }

    fun onReviewButtonClick() {
        _showLiveData.value?.removeIf { it == ReviewListItem.NoReviews }

    }

    fun addReviews(reviewsList: MutableList<ReviewListItem>) {
        _showLiveData.value = reviewsList.toMutableList()
    }

    fun loadReviews() {
        viewModelScope.launch {
            try {
                val response = getReviews(show.showId)
                Log.e("GET REVIEWS", response.toString())
                for (review in response.reviews) {
                    _showLiveData.value?.add(
                        ReviewListItem.Review(
                            review.id,
                            review.comment,
                            review.rating,
                            review.showId,
                            review.user
                        )
                    )
                }
                Log.e("GET RETVAL", _showLiveData.value.toString())
                reviewListUpdated.value = true
            } catch (e: Exception) {
                Log.e("GET REVIEWS", e.toString())
                reviewListUpdated.value = false
            }
        }
    }

    private suspend fun getReviews(showId: Int) =
        ApiModule.retrofit.getReviews(
            showId,
            sharedPreferences.getString("access-token", "").toString(),
            sharedPreferences.getString("client", "").toString(),
            sharedPreferences.getString("uid", "").toString()
        )
}