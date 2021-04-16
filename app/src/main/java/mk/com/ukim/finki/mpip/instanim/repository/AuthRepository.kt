package mk.com.ukim.finki.mpip.instanim.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.instanim.data.entity.User
import mk.com.ukim.finki.mpip.instanim.data.model.Resource

object AuthRepository {

    private val userDb: DatabaseReference = Firebase.database.reference.child("users")
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun currentUser(): Resource<User> {
        lateinit var resource: Resource<User>

        val authUser: FirebaseUser? = mAuth.currentUser
        if (authUser != null) {

            userDb.child(authUser.uid).get().addOnSuccessListener {
                val user = it.getValue<User>()
                resource = if (user != null) {
                    user.authUser = authUser
                    Resource.success(user)
                } else {
                    Resource.error(null, "User not found")
                }
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error creating the user")
                }
            }.await()

        } else {
            Resource.error(null, "No user signed in")
        }

        return resource
    }


    suspend fun signUp(email: String, password: String): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(Unit, msg)
                } ?: run {
                    resource = Resource.error(Unit, "There was an error creating the user")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "Error occurred"
            }
            resource = Resource.error(Unit, message)
        }

        return resource
    }

    suspend fun signIn(email: String, password: String): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
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