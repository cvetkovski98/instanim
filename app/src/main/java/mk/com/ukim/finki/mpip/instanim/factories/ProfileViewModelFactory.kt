package mk.com.ukim.finki.mpip.instanim.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository
import mk.com.ukim.finki.mpip.instanim.ui.profile.ProfileViewModel

class ProfileViewModelFactory(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(postRepository, userRepository, authRepository) as T
    }
}
