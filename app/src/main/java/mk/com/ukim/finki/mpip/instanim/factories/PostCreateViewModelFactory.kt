package mk.com.ukim.finki.mpip.instanim.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.ui.posts.create.PostCreateViewModel

class PostCreateViewModelFactory(
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostCreateViewModel(postRepository, storageRepository) as T
    }
}