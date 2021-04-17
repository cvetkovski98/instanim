package mk.com.ukim.finki.mpip.instanim

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import mk.com.ukim.finki.mpip.instanim.data.model.Status
import mk.com.ukim.finki.mpip.instanim.ui.auth.AuthViewModel
import mk.com.ukim.finki.mpip.instanim.ui.auth.LoginActivity
import mk.com.ukim.finki.mpip.instanim.util.FactoryInjector

class SplashScreenActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        FactoryInjector.getAuthViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel.fetchCurrentUser()
        authViewModel.currentUser.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivityWithDelay(intent)
                }
                Status.ERROR -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivityWithDelay(intent)
                }
                Status.LOADING -> {
                    //do nothing
                }
            }
        })
    }

    private fun startActivityWithDelay(intent: Intent) {
        startActivity(intent)
        finish()
    }
}
