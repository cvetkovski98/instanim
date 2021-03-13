package mk.com.ukim.finki.mpip.instanim.ui.posts.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.model.PostBuilder
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.util.Util

class PostCreateViewModel(
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {
    private var _postBuilder = PostBuilder()

    private val _createResult = MutableLiveData<Resource<Unit>>()

    val postBuilder: PostBuilder
        get() = _postBuilder

    val createResult: LiveData<Resource<Unit>>
        get() = _createResult

    init {
        _postBuilder = PostBuilder(
            postId = Util.generateId(),
            userId = "cvetkovski98" // TODO: fix with user management
        )
    }

    fun setPostUri(localUri: String) {
        _postBuilder.imageUri = localUri
    }

    fun finalizePost(description: String, location: String) {
        val localUri = _postBuilder.imageUri
        val imageTargetUri = _postBuilder.userId + "/" + _postBuilder.postId

        _postBuilder.description = description
        _postBuilder.location = location
        _postBuilder.createdAt = System.currentTimeMillis() / 1000

        _createResult.value = Resource.loading(Unit)
        viewModelScope.launch(IO) {
            localUri?.let {
                storageRepository.uploadPhoto(Uri.parse(it), imageTargetUri)

                val uri = storageRepository.getDownloadUrl(imageTargetUri) // TODO: handle possible failure
                _postBuilder.imageUri = uri.data

                _createResult.postValue(
                    postRepository.createPost(_postBuilder.toPost())
                )
            }
        }

    }
}