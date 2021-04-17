package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.util.Util

class PostCreateViewModel(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {
    private val _createResult = MutableLiveData<Resource<Unit>>()

    val createResult: LiveData<Resource<Unit>>
        get() = _createResult

    fun createPost(description: String, location: String, localUri: Uri) {
        _createResult.value = Resource.loading(Unit)

        val currentUser = authRepository.currentUser()
        val userId = currentUser.data!!.uid
        val postId = Util.generateId()

        val imageTargetUri = "$userId/$postId"

        viewModelScope.launch(IO) {
            storageRepository.uploadPhoto(Uri.parse(localUri.toString()), imageTargetUri)

            // TODO: handle possible failure
            val uri = storageRepository.getDownloadUrl(imageTargetUri)
            val imageUri = uri.data

            val post = Post(
                postId = postId,
                userId = userId,
                createdAt = System.currentTimeMillis() / 1000,
                description = description,
                location = location,
                imageUri = imageUri
            )

            _createResult.postValue(
                postRepository.createPost(post)
            )
        }
    }
}
