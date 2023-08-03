package infinuma.android.shows.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.data.EMAIL_REGEX
import infinuma.android.shows.data.REGISTER_RESULT
import infinuma.android.shows.databinding.FragmentRegisterBinding
import infinuma.android.shows.ui.MainActivity

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var loading: AlertDialog

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
        }

        override fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            binding.emailInputField.error = null
            if (binding.emailInputField.text?.matches(EMAIL_REGEX) == true && (binding.passwordInputField.text?.length
                    ?: 0) >= 6 && binding.passwordInputField.text.toString() == binding.confirmPasswordInputField.text.toString()
            ) {
                binding.emailInputField.error = null
                binding.registerButton.isEnabled = true
                binding.registerButton.setTextColor(ContextCompat.getColor(context!!, R.color.purple))

            } else {
                if (binding.emailInputField.text?.matches(EMAIL_REGEX) == false)
                    binding.emailInputField.error = "Invalid Email"
                binding.registerButton.isEnabled = false
                binding.registerButton.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }

        override fun afterTextChanged(text: Editable?) {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        initEditText()
        initRegisterButton()
        viewModel.registrationResultLiveData.observe(viewLifecycleOwner) {
            if (viewModel.registrationResultLiveData.value == true) {
                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment,
                    bundleOf(REGISTER_RESULT to viewModel.registrationResultLiveData.value!!)
                )
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

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            loading = (activity as MainActivity).initLoadingBarDialog()
            viewModel.onRegisterButtonClick(
                binding.emailInputField.text.toString().trim(),
                binding.passwordInputField.text.toString().trim()
            )
        }
    }

    private fun initEditText() {
        binding.emailInputField.addTextChangedListener(watcher)
        binding.passwordInputField.addTextChangedListener(watcher)
        binding.confirmPasswordInputField.addTextChangedListener(watcher)
        binding.passwordInputField.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.confirmPasswordInputField.transformationMethod = PasswordTransformationMethod.getInstance()
    }


}