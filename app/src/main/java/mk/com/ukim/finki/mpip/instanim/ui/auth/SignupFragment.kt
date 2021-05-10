package mk.com.ukim.finki.mpip.instanim.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun redirectToProfileUpdate(username: String, email: String, password: String) {
        val action = SignupFragmentDirections.actionSignupFragmentToUpdateProfileFragment(
            username, password, email
        )
        findNavController().navigate(action)
    }

    private fun setupButtons() {
        binding.signupButton.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (username.isBlank() ||
                email.isBlank() ||
                password.isBlank() ||
                confirmPassword.isBlank()
            ) {
                Toast.makeText(
                    context,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password != confirmPassword) {
                Toast.makeText(
                    context,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                redirectToProfileUpdate(username, email, password)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
