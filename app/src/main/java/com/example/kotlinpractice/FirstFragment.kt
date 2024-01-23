package com.example.kotlinpractice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kotlinpractice.databinding.FragmentFirstBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordSignUpEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views from the binding
        emailEditText = binding.editTextTextEmailAddressForLogin
        passwordEditText = binding.editTextTextPasswordForLogin
        passwordSignUpEditText = binding.editTextTextPasswordForSignup
        loginButton = binding.buttonLogin
        signUpButton = binding.buttonSignUp

        binding.editTextTextPasswordForSignup.visibility = View.GONE


        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Firebase authentication logic
            if(!password.isNullOrEmpty() && !email.isNullOrEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Login successful, navigate to the next screen or perform other actions

                            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                        } else {

                            loginFailed()
                        }
                    }
            }
            else
            {
                loginFailed()
            }
        }
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordSignUp = passwordSignUpEditText.text.toString()

            if(passwordSignUp.isEmpty()) {
                binding.editTextTextPasswordForSignup.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Create your login, with a repeated password",
                    Toast.LENGTH_SHORT
                ).show()
            }


            if(!password.isNullOrEmpty() && !email.isNullOrEmpty() && !passwordSignUp.isNullOrEmpty()) {
                if (password == passwordSignUp) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    requireContext(),
                                    "You created a login, and are now logged in as $email",
                                    Toast.LENGTH_SHORT
                                ).show()

                                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                            }
                            else
                            {

                            }
                        }
                }
                else
                {
                    Toast.makeText(
                        requireContext(),
                        "You didnt repeat the password correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }
    fun loginFailed() {
        Toast.makeText(
            requireContext(),
            "Authentication failed, try again",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}