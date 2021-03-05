package mk.com.ukim.finki.mpip.instanim.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object PostRepository {
    private val postRepository: DatabaseReference = Firebase.database.reference.child("posts")
}
