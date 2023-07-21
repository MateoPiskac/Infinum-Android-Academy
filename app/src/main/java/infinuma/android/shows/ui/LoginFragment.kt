package infinuma.android.shows.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val emailRegex: Regex = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    private lateinit var binding: FragmentLoginBinding
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
                binding.loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.purple))

            } else {
                if (binding.emailInputField.text?.matches(emailRegex) == false)
                    binding.emailInputField.error = "Invalid Email"
                binding.loginButton.isEnabled = false
                binding.loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }

        override fun afterTextChanged(text: Editable?) {
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.emailInputField.addTextChangedListener(watcher)
        binding.passwordInputField.addTextChangedListener(watcher)
        binding.passwordInputField.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
        }
        return binding.root
    }

}