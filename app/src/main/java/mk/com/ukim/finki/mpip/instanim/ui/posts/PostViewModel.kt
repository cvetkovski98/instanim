package mk.com.ukim.finki.mpip.instanim.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.Comment
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository

class PostViewModel(
    private val postRepository: PostRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _posts = MutableLiveData<Resource<List<Post>>>()
    private val _post = MutableLiveData<Resource<Post>>()
    private val _updatePostStatus = MutableLiveData<Resource<Unit>>()

    val posts: LiveData<Resource<List<Post>>>
        get() = _posts

    val post: LiveData<Resource<Post>>
        get() = _post

    val updatePostStatus: LiveData<Resource<Unit>>
        get() = _updatePostStatus

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

    fun likePost(post: Post) {
        _updatePostStatus.value = Resource.loading(null)
        authRepository.currentUser().data?.uid?.let {
            if (post.likedBy.contains(it))
                post.likedBy.remove(it)
            else
                post.likedBy.add(it)
        }
        viewModelScope.launch(IO) {
            _updatePostStatus.postValue(
                postRepository.updatePost(post)
            )
        }
    }

    fun postComment(post: Post, comment: String) {
        _updatePostStatus.value = Resource.loading(null)
        viewModelScope.launch(IO) {
            authRepository.currentUser().data?.uid?.let {
                val username = userRepository.getUser(it).data?.username
                val commentToBePosted = Comment(username.toString(), comment)
                post.comments.add(commentToBePosted)
            }
            _updatePostStatus.postValue(
                postRepository.updatePost(post)
            )
        }
    }
}