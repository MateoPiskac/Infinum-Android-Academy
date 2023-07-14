package infinuma.android.shows.ui

import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.core.content.ContextCompat
import infinuma.android.shows.R
import infinuma.android.shows.databinding.ActivityLoginBinding

const val USERNAME = "username"
class LoginActivity : AppCompatActivity() {
    private val emailRegex : Regex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    private lateinit var binding: ActivityLoginBinding
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
        }

        override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
            if (binding.emailInputField.text?.matches(emailRegex) == true && (binding.passwordInputField.text?.length
                    ?: 0) >= 6
            ) {
               binding.emailInputField.error = null
                binding.loginButton.isEnabled = true
                binding.loginButton.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.purple))

            } else {
                if(binding.emailInputField.text?.matches(emailRegex) == false)
                    binding.emailInputField.error = "Invalid Email"
                binding.loginButton.isEnabled = false
                binding.loginButton.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
            }
        }

        override fun afterTextChanged(text: Editable?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        Log.d("LoginActivity", "onCreate")
        setContentView(binding.root)

        binding.emailInputField.addTextChangedListener(watcher)
        binding.passwordInputField.addTextChangedListener(watcher)
        binding.passwordInputField.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, binding.emailInputField.text?.substring(0, binding.emailInputField.text!!.indexOf("@")))
            startActivity(intent)
        }
    }

}