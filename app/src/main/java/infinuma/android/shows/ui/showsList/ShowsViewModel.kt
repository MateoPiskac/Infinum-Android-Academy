package infinuma.android.shows.ui.showsList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.data.Show
import infinuma.android.shows.data.ShowEntity
import infinuma.android.shows.data.ShowsDatabase
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ShowsViewModel(
    private val database: ShowsDatabase
) : ViewModel() {

    private var _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> get() = _showsLiveData
    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean> = _isLoading
    private lateinit var accessToken: String
    private lateinit var client: String
    private lateinit var uid: String
    fun fetchShows() {
        _isLoading.value = true
        accessToken = sharedPreferences.getString("access-token", "debug").toString()
        client = sharedPreferences.getString("client", "").toString()
        uid = sharedPreferences.getString("uid", "").toString()
        viewModelScope.launch {
            try {
                val response = getShows(accessToken!!, client!!, uid!!)
                database.showDAO().insertAllShows(response.shows.map { show ->
                    ShowEntity(show.showId, show.averageRating, show.description, show.image, show.numOfReviews, show.title)
                })
                _showsLiveData.value = response.shows
            } catch (exception: Exception) {
                Log.e("GET SHOWS", exception.toString())
            }
            _isLoading.value = false
        }
    }

    private suspend fun getShows(accessToken: String, client: String, uid: String) =
        ApiModule.retrofit.getShows(accessToken, client, uid)

    fun fetchShowsFromDatabase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val data = database.showDAO().getAllShows().map { showEntity ->
                    Show(
                        showId = showEntity.showId,
                        averageRating = showEntity.averageRating,
                        description = showEntity.description, "",
                        numOfReviews = showEntity.numOfReviews,
                        title = showEntity.title
                    )
                }
                _showsLiveData.postValue(data)
                Log.e("GET SHOWS FROM DB", data.toString())
            }
        }

    }

    fun uploadProfileImage(photo: File) {
        viewModelScope.launch {
            try {
                val response = uploadProfilePhoto(
                    MultipartBody.Part.createFormData(
                        "image", photo.name,
                        photo.asRequestBody()
                    )
                )
                sharedPreferences.edit().putString(PROFILE_PHOTO_URI, response.user.imageUrl).apply()
                Log.e("UPLOAD IMAGE SUCCESS", response.toString())
            } catch (exception: Exception) {
                Log.e("UPLOAD IMAGE FAIL", exception.toString())
            }

        }
    }

    private suspend fun uploadProfilePhoto(image: MultipartBody.Part) =
        ApiModule.retrofit.uploadImage(
            sharedPreferences.getString("access-token", "debug").toString(),
            sharedPreferences.getString("client", "").toString(),
            sharedPreferences.getString("uid", "").toString(),
            image
        )
}

