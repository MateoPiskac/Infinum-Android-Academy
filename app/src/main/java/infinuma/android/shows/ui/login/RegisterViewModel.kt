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
    private val _isLoading : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isLoading : LiveData<Boolean> = _isLoading
    fun onRegisterButtonClick(username: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                registerUser(username = username, password = password)
                _registrationResultLiveData.value = true
            }
            catch (registrationFail : Exception){
                Log.e("REGISTER FAIL", registrationFail.toString())
                _registrationResultLiveData.value = false
            }
            _isLoading.value = false
        }
    }

    private suspend fun registerUser(username: String, password: String) {
        ApiModule.retrofit.registerUser(
            RegisterRequest(username, password, password)
        )
    }
}