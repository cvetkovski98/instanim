package mk.com.ukim.finki.mpip.instanim.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository
import mk.com.ukim.finki.mpip.instanim.ui.posts.create.PostCreateViewModel

class PostCreateViewModelFactory(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostCreateViewModel(authRepository, postRepository, storageRepository, userRepository) as T
    }
}