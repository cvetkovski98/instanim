package mk.com.ukim.finki.mpip.instanim.util

import mk.com.ukim.finki.mpip.instanim.factories.AuthViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.PostCreateViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.PostViewModelFactory
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository
import mk.com.ukim.finki.mpip.instanim.repository.StorageRepository

object FactoryInjector {
    fun getAuthViewModel(): AuthViewModelFactory {
        return AuthViewModelFactory(AuthRepository)
    }

    fun getPostViewModel(): PostViewModelFactory {
        return PostViewModelFactory(PostRepository, StorageRepository)
    }

    fun getPostCreateViewModel(): PostCreateViewModelFactory {
        return PostCreateViewModelFactory(PostRepository, StorageRepository)
    }
}