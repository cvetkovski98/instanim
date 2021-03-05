package mk.com.ukim.finki.mpip.instanim.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository

class HomeViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}