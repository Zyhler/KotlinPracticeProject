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
    private lateinit var loginButton: Button
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
        loginButton = binding.buttonLogin

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Firebase authentication logic
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Login successful, navigate to the next screen or perform other actions

                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(requireContext(), "Authentication failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Handle other button clicks or UI interactions as needed
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}