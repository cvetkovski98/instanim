package mk.com.ukim.finki.mpip.instanim.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileBinding
import mk.com.ukim.finki.mpip.instanim.ui.auth.AuthViewModel
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels {
        FactoryInjector.getAuthViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener {
            authViewModel.signOutUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
