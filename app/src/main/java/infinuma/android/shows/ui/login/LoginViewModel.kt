package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.models.SignInRequest
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginResultLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loginResultLiveData : LiveData<Boolean> = _loginResultLiveData
    var accessToken : String? = null
    var client : String? = null
    var uid : String? = null
    fun onLoginButtonClick(username: String, password: String){
        viewModelScope.launch {
            try {
                val response = loginUser(username = username, password = password)
                accessToken= response.headers()["access-token"]
                client= response.headers()["client"]
                uid= response.headers()["uid"]
                _loginResultLiveData.value = true
            }
            catch (registrationFail : Exception){
                Log.e("LOGIN FAIL", registrationFail.toString())
                _loginResultLiveData.value = false
            }
            with(sharedPreferences) {
                edit().putString("client",client).apply()
                edit().putString("uid",uid).apply()
                edit().putString("access-token",accessToken).apply()
            }
        }
    }

    private suspend fun loginUser(username: String, password: String)=
        ApiModule.retrofit.loginUser(SignInRequest(username,password))


}