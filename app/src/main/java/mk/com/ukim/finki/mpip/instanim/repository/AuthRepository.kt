package mk.com.ukim.finki.mpip.instanim.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.instanim.data.model.Resource

object AuthRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currentUser(): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>
        val user: FirebaseUser? = mAuth.currentUser
        resource = if (user != null) {
            Resource.success(user)
        } else {
            Resource.error(null, "No user signed in")
        }

        return resource
    }


    suspend fun signUp(email: String, password: String): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    resource = currentUser()
                }
                .addOnFailureListener {
                    it.message?.let { msg ->
                        resource = Resource.error(null, msg)
                    } ?: run {
                        resource = Resource.error(null, "There was an error creating the user")
                    }
                }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "Error occurred"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
        lateinit var resource: Resource<FirebaseUser>

        try {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    resource = currentUser()
                }
                .addOnFailureListener {
                    it.message?.let { msg ->
                        resource = Resource.error(null, msg)
                    } ?: run {
                        resource = Resource.error(null, "There was an error signing in")
                    }
                }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "Error occurred while trying to sign in"
            }
            resource = Resource.error(null, message)
        }
        return resource
    }

    fun signOut() {
        return mAuth.signOut()
    }
}