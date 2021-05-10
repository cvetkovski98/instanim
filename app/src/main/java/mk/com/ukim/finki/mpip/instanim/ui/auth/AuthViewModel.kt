package mk.com.ukim.finki.mpip.instanim.ui.auth

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {
    private val _currentUser = MutableLiveData<Resource<FirebaseUser>>()
    val currentUser: LiveData<Resource<FirebaseUser>>
        get() = _currentUser

    fun fetchCurrentUser() {
        _currentUser.value = Resource.loading(null)
        _currentUser.value = authRepository.currentUser()
    }

    fun signInUser(email: String?, password: String?) {
        _currentUser.value = Resource.loading(null)

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            _currentUser.value = Resource.error(null, "Email or password is blank")
        } else {
            viewModelScope.launch(IO) {
                val authUser = authRepository.signIn(email, password)
                _currentUser.postValue(authUser)
            }
        }
    }

    fun signOutUser() {
        _currentUser.value = Resource.loading(null)
        authRepository.signOut()
        fetchCurrentUser()
    }

    fun signUpUser(
        username: String,
        email: String?,
        password: String?,
        name: String,
        lastName: String,
        bio: String,
        profileImageLocalUri: Uri
    ) {
        _currentUser.value = Resource.loading(null)

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            _currentUser.value = Resource.error(null, "Email or password is blank")
            return
        }

        viewModelScope.launch(IO) {
            // create auth user
            val authUserResource = authRepository.signUp(email, password)

            if (authUserResource.status != Status.SUCCESS) {
                // auth user creation failed & return result
                _currentUser.postValue(
                    authUserResource
                )
                return@launch
            }

            // upload user profile image
            val imageTargetUri = "${authUserResource.data!!.uid}/$username"
            storageRepository.uploadPhoto(
                Uri.parse(profileImageLocalUri.toString()),
                imageTargetUri
            )
            val uri = storageRepository.getDownloadUrl(imageTargetUri)
            val imageUri = uri.data!!

            val user = User(
                uid = authUserResource.data.uid,
                username = username,
                name = name,
                lastName = lastName,
                imageUri = imageUri,
                bio = bio,
                follows = listOf(),
                followedBy = listOf()
            )

            // create app user
            val appUserResource = userRepository.createUser(authUserResource.data.uid, user)

            if (appUserResource.status != Status.SUCCESS) {
                // app user creation failed & delete auth user
                authUserResource.data.delete()
                lateinit var resultMessage: String
                authUserResource.message?.let {
                    resultMessage = it
                } ?: run {
                    resultMessage = "There was an error creating an application user"
                }
                _currentUser.postValue(
                    Resource.error(null, resultMessage)
                )
            } else {
                // both succeeded return result
                _currentUser.postValue(
                    authUserResource
                )
            }
        }
    }
}
