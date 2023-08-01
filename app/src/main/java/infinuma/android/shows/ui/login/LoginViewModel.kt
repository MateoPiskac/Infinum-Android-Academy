package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.models.SignInRequest
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginResultLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loginResultLiveData : LiveData<Boolean> = _loginResultLiveData
    private val _isLoading : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading : LiveData<Boolean> = _isLoading
    private var accessToken : String? = null
    private var client : String? = null
    private var uid : String? = null
    fun onLoginButtonClick(username: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = loginUser(username = username, password = password)
                if(response.isSuccessful) {
                    accessToken = response.headers()["access-token"]
                    client = response.headers()["client"]
                    uid = response.headers()["uid"]
                    sharedPreferences.edit().putString(PROFILE_PHOTO_URI, response.body()?.user?.imageUrl).apply()
                    _loginResultLiveData.value = true
                }
                else
                    _loginResultLiveData.value = false
            }
            catch (registrationFail : Exception){
                Log.e("LOGIN FAIL", registrationFail.toString())
                _loginResultLiveData.value = false
            }
            finally {
                _isLoading.value = false
                with(sharedPreferences) {
                    edit().putString("client",client).apply()
                    edit().putString("uid",uid).apply()
                    edit().putString("access-token",accessToken).apply()
                }
            }

        }
    }

    private suspend fun loginUser(username: String, password: String)=
        ApiModule.retrofit.loginUser(SignInRequest(username,password))


}