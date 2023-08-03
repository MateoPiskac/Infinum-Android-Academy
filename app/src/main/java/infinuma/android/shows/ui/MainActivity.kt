package infinuma.android.shows.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import infinuma.android.shows.R
import infinuma.android.shows.databinding.ActivityMainBinding
import infinuma.android.shows.networking.ApiModule

lateinit var sharedPreferences: SharedPreferences
class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Shows", MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApiModule.initRetrofit(this)
    }
    fun initLoadingBarDialog(): AlertDialog {
        return AlertDialog.Builder(this).setView(R.layout.loading_spinner_dialog).setCancelable(false)
            .create()
    }

    fun isInternetConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ||
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        }

        return false
    }

}