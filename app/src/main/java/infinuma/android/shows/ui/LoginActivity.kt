package infinuma.android.shows.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import infinuma.android.shows.R
import infinuma.android.shows.databinding.ActivityLoginBinding
import java.util.zip.Inflater

/*
    Activity Lifecycle
    onCreate() – called when the activity is first created.
    onStart() – called when the activity is displayed to the user.
    onResume() – called when the activity is displayed to the user and is ready to receive user input.
    onPause() – called when the activity loses focus but is still visible to the user.
    onStop() – called when the activity is no longer visible to the user.
    onRestart() – called when the activity has been stopped and is starting again.
    onDestroy() – called when activity is destroyed.

    When the app is created the following methods are called:
    onCreate()
    onStart()
    onResume()

    When moving the app into the background the following methods are called:
    onPause()
    onStop()
    When moving the app back to the foreground the following methods are called:
    onRestart()
    onStart()
    onResume()

    When locking the device the following methods are called:
    onPause()
    onStop()
    When unlocking the device the following methods are called:
    onRestart()
    onStart()
    onResume()

    When killing the app the following methods are called:
    onPause()
    onStop()
    onDestroy()

 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        Log.d("LoginActivity", "onCreate")
        setContentView(binding.root)




    }
    override fun onStart() {
        super.onStart()
        Log.d("LoginActivity", "onStart is called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LoginActivity", "onRestart is called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LoginActivity", "onPause is called")
    }

    override fun onStop() {
        Log.d("LoginActivity", "onStop is called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("LoginActivity", "onDestroy is called")
        super.onDestroy()
    }
}