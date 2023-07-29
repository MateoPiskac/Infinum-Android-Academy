package infinuma.android.shows.ui

import android.content.SharedPreferences
import android.os.Bundle
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

}