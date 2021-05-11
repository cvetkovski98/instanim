package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository
import mk.com.ukim.finki.mpip.instanim.util.Util

class PostCreateViewModel(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _createResult = MutableLiveData<Resource<Unit>>()

    val createResult: LiveData<Resource<Unit>>
        get() = _createResult

    fun createPost(description: String, lat: Double, lng: Double, localUri: Uri) {
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
            val username = userRepository.getUser(userId).data?.username

            val post = Post(
                postId = postId,
                userId = userId,
                username = username,
                createdAt = System.currentTimeMillis() / 1000,
                description = description,
                lat = lat,
                lng = lng,
                imageUri = imageUri
            )

            _createResult.postValue(
                postRepository.createPost(post)
            )
        }
    }
}
