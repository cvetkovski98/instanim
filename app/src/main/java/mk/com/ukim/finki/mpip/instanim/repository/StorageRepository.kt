package mk.com.ukim.finki.mpip.instanim.repository

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import mk.com.ukim.finki.mpip.instanim.data.model.Resource

object StorageRepository {

    private val storage = Firebase.storage("gs://instanim.appspot.com")
    private val storageRef = storage.reference

    suspend fun uploadPhoto(localUri: Uri, targetUri: String): Resource<Unit> {
        lateinit var resource: Resource<Unit>

        val target = storageRef.child(targetUri)
        val uploadTask = target.putFile(localUri)

        try {
            uploadTask.addOnSuccessListener {
                resource = Resource.success(Unit)
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                } ?: run {
                    resource = Resource.error(null, "There was an error uploading the photo")
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error uploading the photo"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

    suspend fun getDownloadUrl(targetUri: String): Resource<String> {
        lateinit var resource: Resource<String>

        val target = storageRef.child(targetUri)

        try {
            target.downloadUrl.addOnSuccessListener {
                resource = Resource.success(it.toString())
            }.addOnFailureListener {
                it.message?.let { msg ->
                    resource = Resource.error(null, msg)
                }
            }.await()
        } catch (e: Exception) {
            var message = e.message
            if (message == null) {
                message = "There was an error getting the download Uri of the photo"
            }
            resource = Resource.error(null, message)
        }

        return resource
    }

}