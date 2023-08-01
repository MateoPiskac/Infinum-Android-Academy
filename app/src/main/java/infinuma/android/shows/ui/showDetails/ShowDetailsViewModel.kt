package infinuma.android.shows.ui.showDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.Show
import infinuma.android.shows.models.AddReviewRequest
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class ShowDetailsViewModel : ViewModel() {

    private var _showLiveData = MutableLiveData<MutableList<ReviewListItem>>()
    val showLiveData: LiveData<MutableList<ReviewListItem>> get() = _showLiveData
    private lateinit var show: Show
    var reviewListUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isLoading : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading : LiveData<Boolean> = _isLoading
    init {
        _showLiveData.value = mutableListOf()
    }

    fun setShow(showClicked: Show) {
        show = showClicked
        _showLiveData.value = getInitialReviewList()
    }

    fun getShow(): Show = show
    fun getShowId(): Int = show.showId

    private fun getInitialReviewList(): MutableList<ReviewListItem> {
        _showLiveData.value?.add(ReviewListItem.ShowDetails(show.image, show.description))
        _showLiveData.value?.add(ReviewListItem.Rating(show.averageRating ?: 0f, show.numOfReviews))
        return _showLiveData.value!!
    }

    fun onReviewButtonClick() {
        _showLiveData.value?.removeIf { it == ReviewListItem.NoReviews }

    }

    fun loadReviews() {
        _isLoading.value = true
        _showLiveData.value = mutableListOf()
        getInitialReviewList()
        viewModelScope.launch {
            try {
                val response = getReviews(show.showId)
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
                reviewListUpdated.value = !reviewListUpdated.value!!
            } catch (e: Exception) {
                Log.e("GET REVIEWS", e.toString())
            }
            _isLoading.value = false
        }
    }

    private suspend fun getReviews(showId: Int) =
        ApiModule.retrofit.getReviews(
            showId,
            sharedPreferences.getString("access-token", "").toString(),
            sharedPreferences.getString("client", "").toString(),
            sharedPreferences.getString("uid", "").toString()
        )
    suspend fun addReview(reviewBody: String, rating: Int, showId: Int) =
        ApiModule.retrofit.addReview(
            AddReviewRequest(reviewBody, rating, showId),
            sharedPreferences.getString("access-token", "")!!,
            sharedPreferences.getString("client", "")!!,
            sharedPreferences.getString("uid", "")!!
        )
}