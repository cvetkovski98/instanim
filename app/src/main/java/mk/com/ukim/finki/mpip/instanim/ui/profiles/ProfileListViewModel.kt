package mk.com.ukim.finki.mpip.instanim.ui.profiles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Resource
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository

class ProfileListViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _profiles = MutableLiveData<Resource<List<User>>>()
    val profiles: LiveData<Resource<List<User>>>
        get() = _profiles

    fun fetchProfiles(query: String?) {
        _profiles.value = Resource.loading(null)

        if (query.isNullOrBlank()) {
            viewModelScope.launch(IO) {
                _profiles.postValue(
                    userRepository.getUsers(null)
                )
            }
        } else {
            viewModelScope.launch(IO) {
                Log.d("fetchProfiles", "fetchProfiles: $query")
                _profiles.postValue(
                    userRepository.getUsers(query)
                )
            }
        }
    }

}
