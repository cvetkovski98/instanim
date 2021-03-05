package mk.com.ukim.finki.mpip.instanim.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _currentUser = MutableLiveData<Resource<FirebaseUser>>()
    val currentUser: LiveData<Resource<FirebaseUser>>
        get() = _currentUser

    fun fetchCurrentUser() {
        _currentUser.value = Resource.loading(null)
        viewModelScope.launch(IO) {
            _currentUser.postValue(
                authRepository.currentUser()
            )
        }
    }

    fun signInUser(email: String?, password: String?) {
        _currentUser.value = Resource.loading(null)

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            _currentUser.value = Resource.error(null, "Email or password is blank")
        } else {
            viewModelScope.launch(IO) {
                val user = authRepository.signIn(email, password)
                _currentUser.postValue(user)
            }
        }
    }

    fun signOutUser() {
        _currentUser.value = Resource.loading(null)
        authRepository.signOut()
        fetchCurrentUser()
    }

    fun signUpUser(email: String?, password: String?) {
        _currentUser.value = Resource.loading(null)

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            _currentUser.value = Resource.error(null, "Email or password is blank")
        } else {
            viewModelScope.launch(IO) {
                val user = authRepository.signUp(email, password)
                _currentUser.postValue(user)
            }
        }
    }
}