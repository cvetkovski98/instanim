package mk.com.ukim.finki.mpip.instanim.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository

class PostViewModel(
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {
    private val _posts = MutableLiveData<Resource<List<Post>>>()
    private val _post = MutableLiveData<Resource<Post>>()

    val posts: LiveData<Resource<List<Post>>>
        get() = _posts

    val post: LiveData<Resource<Post>>
        get() = _post

    fun fetchPosts() {
        _posts.value = Resource.loading(null)
        viewModelScope.launch(IO) {
            _posts.postValue(
                postRepository.getPosts()
            )
        }
    }

    fun fetchPost(postId: String) {
        _post.value = Resource.loading(null)
        viewModelScope.launch(IO) {
            _post.postValue(
                postRepository.getPost(postId)
            )
        }
    }
}