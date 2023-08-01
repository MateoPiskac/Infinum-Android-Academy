package infinuma.android.shows.ui.showsList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.data.Show
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import java.io.File
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ShowsViewModel() : ViewModel() {

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
                _showsLiveData.value = response.shows

            } catch (exception: Exception) {
                Log.e("GET SHOWS", exception.toString())
            }
            _isLoading.value = false
        }
    }

    private suspend fun getShows(accessToken: String, client: String, uid: String) =
        ApiModule.retrofit.getShows(accessToken, client, uid)

    fun uploadProfileImage(photo: File) {
        viewModelScope.launch {
            try {
//                Log.e("UPLOAD IMAGE", photoPath)
//                Log.e("IMAGE FORMAT", photoPath.toRequestBody("image/*".toMediaTypeOrNull()).toString())
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

