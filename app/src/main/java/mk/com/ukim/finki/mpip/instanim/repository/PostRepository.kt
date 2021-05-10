package mk.com.ukim.finki.mpip.instanim.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.instanim.data.entity.Post
import mk.com.ukim.finki.mpip.instanim.data.model.Resource

object PostRepository {

    private val postDb: DatabaseReference = Firebase.database.reference.child("posts")

    suspend fun getPosts(): Resource<List<Post>> {
        lateinit var resource: Resource<List<Post>>

        try {
            postDb.get().addOnSuccessListener {
                val idPostMap = it.getValue<Map<String, Post>>()
                resource = if (idPostMap != null) {
                    Resource.success(idPostMap.values.toList())
                } else {
                    Resource.error(null, "Post not found")
                }
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error fetching the posts")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error fetching the posts"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun getPosts(userId: String): Resource<List<Post>> {
        lateinit var resource: Resource<List<Post>>
        val query: Query = postDb.orderByChild("userId").equalTo(userId)

        try {
            query.get().addOnSuccessListener {
                val idPostMap = it.getValue<Map<String, Post>>()
                resource = if (idPostMap != null) {
                    Resource.success(idPostMap.values.toList())
                } else {
                    Resource.error(null, "Post not found")
                }
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error fetching the posts")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error fetching the posts"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun getPost(postId: String): Resource<Post> {
        lateinit var resource: Resource<Post>

        try {
            postDb.child(postId).get().addOnSuccessListener {
                val post = it.getValue<Post>()
                resource = if (post != null) {
                    Resource.success(post)
                } else {
                    Resource.error(null, "Post not found")
                }
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error fetching the post")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error fetching the post"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun createPost(post: Post): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        try {
            postDb.child(post.postId).setValue(post).addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error creating the post")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error creating the post"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun updatePost(post: Post): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        val postValues = post.toMap()
        val update = hashMapOf<String, Any>(
            post.postId to postValues
        )

        try {
            postDb.updateChildren(update).addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error updating the post")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error creating the post"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }


    suspend fun removePost(postId: String): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        try {
            postDb.child(postId).removeValue().addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error removing the post")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error removing the post"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

}
