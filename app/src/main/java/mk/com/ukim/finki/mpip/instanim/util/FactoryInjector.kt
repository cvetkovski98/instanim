package mk.com.ukim.finki.mpip.instanim.util

import mk.com.ukim.finki.mpip.instanim.factories.AuthViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.PostCreateViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.PostViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.ProfileViewModelFactory
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository
import mk.com.ukim.finki.mpip.instanim.repository.UserRepository

object FactoryInjector {
    fun getAuthViewModel(): AuthViewModelFactory {
        return AuthViewModelFactory(AuthRepository, UserRepository, StorageRepository)
    }

    fun getPostViewModel(): PostViewModelFactory {
        return PostViewModelFactory(PostRepository, StorageRepository, AuthRepository)
    }

    fun getPostCreateViewModel(): PostCreateViewModelFactory {
        return PostCreateViewModelFactory(AuthRepository, PostRepository, StorageRepository)
    }

    fun getProfileViewModel(): ProfileViewModelFactory {
        return ProfileViewModelFactory(PostRepository, UserRepository, AuthRepository)
    }
}
