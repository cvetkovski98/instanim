package mk.com.ukim.finki.mpip.instanim.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import mk.com.ukim.finki.mpip.instanim.databinding.FragmentProfileBinding
import mk.com.ukim.finki.mpip.instanim.ui.auth.AuthViewModel
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class ProfileFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels {
        FactoryInjector.getAuthViewModel()
    }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}