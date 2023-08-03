package infinuma.android.shows.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.data.EMAIL_REGEX
import infinuma.android.shows.data.LOGIN
import infinuma.android.shows.data.REGISTER_RESULT
import infinuma.android.shows.data.REMEMBER_LOGIN
import infinuma.android.shows.data.USERNAME
import infinuma.android.shows.databinding.FragmentLoginBinding
import infinuma.android.shows.ui.MainActivity
import infinuma.android.shows.ui.sharedPreferences

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var loading: AlertDialog
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
        }

        override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
            if (binding.emailInputField.text?.matches(EMAIL_REGEX) == true && (binding.passwordInputField.text?.length
                    ?: 0) >= 6
            ) {
                binding.emailInputField.error = null
                binding.loginButton.isEnabled = true
                binding.loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.purple))

            } else {
                if (binding.emailInputField.text?.matches(EMAIL_REGEX) == false)
                    binding.emailInputField.error = "Invalid Email"
                binding.loginButton.isEnabled = false
                binding.loginButton.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }

        override fun afterTextChanged(text: Editable?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(LOGIN, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        initEditText()

        if (arguments?.getBoolean(REGISTER_RESULT) == true) {
            binding.loginText.text = getString(R.string.registration_succesful)
            binding.registerTextButton.isVisible = false
        } else {
            binding.registerTextButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        viewModel.loginResultLiveData.observe(requireActivity()) {
            if (viewModel.loginResultLiveData.value == true) {
                sharedPreferences.edit {
                    putBoolean(REMEMBER_LOGIN, binding.loginCheckbox.isChecked)
                    apply()
                }
                findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
            }
        }

        binding.loginButton.setOnClickListener {
            loading = (activity as MainActivity).initLoadingBarDialog()
            viewModel.onLoginButtonClick(
                binding.emailInputField.text.toString().trim(),
                binding.passwordInputField.text.toString().trim()
            )
            sharedPreferences.edit {
                putString(USERNAME, binding.emailInputField.text.toString())
            }
        }
        viewModel.isLoading.observe(requireActivity()) {
            if (viewModel.isLoading.value == true) {
                loading.window?.setBackgroundDrawableResource(android.R.color.transparent)
                loading.show()
            } else if (viewModel.isLoading.value == false)
                loading.cancel()
        }

        return binding.root
    }

    private fun initEditText() {
        binding.emailInputField.addTextChangedListener(watcher)
        binding.passwordInputField.addTextChangedListener(watcher)
        binding.passwordInputField.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (sharedPreferences.getBoolean(REMEMBER_LOGIN, false))
            findNavController().navigate(R.id.action_loginFragment_to_showsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}