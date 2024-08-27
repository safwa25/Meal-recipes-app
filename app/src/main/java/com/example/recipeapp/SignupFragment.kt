package com.example.recipeapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.appstorage.RepositoryImplement
import com.example.recipeapp.database.user.LocalDataBaseImplement
import com.example.recipeapp.database.user.User
import com.example.recipeapp.viewmodel.AppViewModel
import com.example.recipeapp.viewmodel.AppViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupFragment : Fragment() {
    private lateinit var emailInput : TextInputLayout
    private lateinit var passwordInput  : TextInputLayout
    private lateinit var signUpButton : Button
    private lateinit var email : TextInputEditText
    private lateinit var password  : TextInputEditText
    private lateinit var userName : TextInputEditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = AppViewModelFactory(RepositoryImplement(LocalDataBaseImplement(this.requireContext())))
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AppViewModel::class.java)
        emailInput = view.findViewById(R.id.email)
        passwordInput = view.findViewById(R.id.signPassword)
        signUpButton = view.findViewById(R.id.signBtn)
        email = view.findViewById(R.id.emailText)
        password = view.findViewById(R.id.signPasswordText)
        userName = view.findViewById(R.id.signUsernameText)
        setupEmailValidation()
        setupPasswordValidation()
        signUpButton.setOnClickListener {
            viewModel.loadUserByEmail(email.text.toString())
        }

        viewModel.userExists.observe(viewLifecycleOwner) { isExixt ->
            if (isExixt) {
                emailInput.helperText = "Email already exists"
            } else {
               // signUpButton.isEnabled = false chatgpt solution
                sharedPreferences = requireContext().getSharedPreferences("currentuser", Context.MODE_PRIVATE)
                val user = User(
                    email = email.text.toString(),
                    password = password.text.toString(),
                    name = userName.text.toString()
                )
                val editor=sharedPreferences.edit()
                editor.putInt("id",user.id)
                editor.apply()
                viewModel.insertNewUser(user)
                findNavController().navigate(R.id.first_splash)
            }
        }
    }

    private fun setupEmailValidation() {
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                emailInput.helperText = validateEmail()
                updateSignUpButtonState()

            }
        })
    }

    private fun validateEmail(): String? {
        val emailText = email.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else {
            null
        }
    }



    private fun setupPasswordValidation() {
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Optionally validate here for real-time feedback
            }
            override fun afterTextChanged(s: Editable?) {
                passwordInput.helperText = validatePassword()
                updateSignUpButtonState()
            }
        })
    }

    private fun validatePassword(): String? {
        val passwordText = password.text.toString()
        val hasLetter = passwordText.any { it.isLetter() }
        val hasDigit = passwordText.any { it.isDigit() }
        val hasSymbol = passwordText.any { !it.isLetterOrDigit() }

        val criteriaCount = listOf(hasLetter, hasDigit, hasSymbol).count { it }
        return when {
            passwordText.length <= 8 -> "Password should be longer than 8 characters"
            criteriaCount < 2 -> "Password should contain at least two options of: letters, digits, or symbols"
            else -> null
        }
    }

    private fun updateSignUpButtonState() {
        val emailValid = validateEmail() == null
        val passwordValid = validatePassword() == null
        signUpButton.isEnabled = emailValid && passwordValid
    }


}