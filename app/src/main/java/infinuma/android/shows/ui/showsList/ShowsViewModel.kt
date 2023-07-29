package infinuma.android.shows.ui.showsList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.Show
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class ShowsViewModel : ViewModel() {

    private var _showsLiveData = MutableLiveData<List<Show>>()
    val showsLiveData: LiveData<List<Show>> get() = _showsLiveData
    private lateinit var accessToken : String
    private lateinit var client : String
    private lateinit var uid : String
    fun fetchShows() {
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
        }
    }


}

private suspend fun getShows(accessToken: String, client: String, uid: String) =
    ApiModule.retrofit.getShows(accessToken, client, uid)

