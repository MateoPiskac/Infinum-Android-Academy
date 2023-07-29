package infinuma.android.shows.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import infinuma.android.shows.models.RegisterRequest
import infinuma.android.shows.networking.ApiModule
import java.lang.Exception
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registrationResultLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val registrationResultLiveData: LiveData<Boolean> = _registrationResultLiveData

    fun onRegisterButtonClick(username: String, password: String) {
        viewModelScope.launch {
            try {
                registerUser(username = username, password = password)
                _registrationResultLiveData.value = true
            }
            catch (registrationFail : Exception){
                Log.e("REGISTER FAIL", registrationFail.toString())
                _registrationResultLiveData.value = false
            }
        }
    }

    private suspend fun registerUser(username: String, password: String) {
        ApiModule.retrofit.registerUser(
            RegisterRequest(username, password, password)
        )
    }
}