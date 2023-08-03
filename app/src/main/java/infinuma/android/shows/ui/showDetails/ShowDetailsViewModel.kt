package infinuma.android.shows.ui.showDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.data.ReviewEntity
import infinuma.android.shows.data.Show
import infinuma.android.shows.data.ShowsDatabase
import infinuma.android.shows.data.UserEntity
import infinuma.android.shows.models.AddReviewRequest
import infinuma.android.shows.models.User
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowDetailsViewModel(private val database: ShowsDatabase) : ViewModel() {

    private var _showLiveData = MutableLiveData<MutableList<ReviewListItem>>()
    val showLiveData: LiveData<MutableList<ReviewListItem>> get() = _showLiveData
    private lateinit var show: Show
    var reviewListUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean> = _isLoading

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
                var averageRating: Float = 0f
                database.reviewDAO().insertAllReviews(response.reviews.map { review ->
                    ReviewEntity(
                        reviewId = review.id.toInt(),
                        comment = review.comment,
                        rating = review.rating,
                        showId = review.showId,
                        userId = review.user.id.toInt()
                    )
                }
                )
                for (review in response.reviews) {
                    averageRating += review.rating
                    _showLiveData.value?.add(
                        ReviewListItem.Review(
                            review.id,
                            review.comment,
                            review.rating,
                            review.showId,
                            review.user
                        )
                    )
                    database.usersDAO().insertUser(
                        UserEntity(
                            review.user.id.toInt(),
                            review.user.email,
                            review.user.imageUrl?:""
                        ))
                }
                averageRating /= response.reviews.size
                _showLiveData.value!![1] = ReviewListItem.Rating(averageRating, response.reviews.size)
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

    fun addReviewToDatabase(reviewBody: String, rating: Int, showId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.usersDAO().insertUser(UserEntity( //i am passing the email as id because i do not know how to determine the user id per se
                    0,
                    sharedPreferences.getString("uid", "")!!,
                    sharedPreferences.getString(
                        PROFILE_PHOTO_URI, ""
                    )!!))
                database.reviewDAO().addReview(
                    ReviewEntity(
                        0,
                        reviewBody,
                        rating,
                        showId,
                        database.usersDAO().getUserByEmail(sharedPreferences.getString("uid", "")!!).id
                        )
                    )

            }
            reviewListUpdated.value = !reviewListUpdated.value!!
        }
    }

    fun loadReviewsFromDatabase(showId: Int) {
        viewModelScope.launch {
            _showLiveData.value = mutableListOf()
            withContext(Dispatchers.IO) {

                var data: MutableList<ReviewListItem> = getInitialReviewList()
                val user = database.usersDAO().getUserByEmail(sharedPreferences.getString("uid", "")!!)
                data.addAll(database.reviewDAO().getReviews(showId).map { reviewEntity ->
                    ReviewListItem.Review(
                        reviewEntity.reviewId.toString() ?: "0",
                        reviewEntity.comment,
                        reviewEntity.rating,
                        reviewEntity.showId,
                        User(
                            user.id.toString(),
                            user.email,
                            user.avatar
                        )
                    )
                })
                _showLiveData.postValue(data)
            }
        }
    }

    suspend fun addReview(reviewBody: String, rating: Int, showId: Int) =
        ApiModule.retrofit.addReview(
            AddReviewRequest(reviewBody, rating, showId),
            sharedPreferences.getString("access-token", "")!!,
            sharedPreferences.getString("client", "")!!,
            sharedPreferences.getString("uid", "")!!
        )
}