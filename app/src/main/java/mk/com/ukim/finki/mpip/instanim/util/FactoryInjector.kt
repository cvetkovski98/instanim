package mk.com.ukim.finki.mpip.instanim.util

import mk.com.ukim.finki.mpip.instanim.factories.AuthViewModelFactory
import mk.com.ukim.finki.mpip.instanim.factories.HomeViewModelFactory
import mk.com.ukim.finki.mpip.instanim.repository.AuthRepository
import mk.com.ukim.finki.mpip.instanim.repository.PostRepository

object FactoryInjector {
    fun getAuthViewModel(): AuthViewModelFactory{
        return AuthViewModelFactory(AuthRepository)
    }

    fun getHomeViewModel(): HomeViewModelFactory{
        return HomeViewModelFactory(PostRepository)
    }
}