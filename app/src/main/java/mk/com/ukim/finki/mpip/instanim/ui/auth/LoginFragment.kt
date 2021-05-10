package mk.com.ukim.finki.mpip.instanim.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentLoginBinding
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels {
        FactoryInjector.getAuthViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        authViewModel.currentUser.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.SUCCESS -> {
                    redirectToHome()
                }
                Status.LOADING -> {
                    // do nothing
                }
            }
        })
    }

    private fun setupButtons() {
        binding.signinButton.setOnClickListener {
            handleLogin()
        }

        binding.signupButton.setOnClickListener {
            redirectToSignUp()
        }
    }

    private fun handleLogin() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        authViewModel.signInUser(email, password)
    }

    private fun redirectToHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
        findNavController().navigate(action)
    }

    private fun redirectToSignUp() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
