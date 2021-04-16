package mk.com.ukim.finki.mpip.instanim.repository
//
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.database.ktx.getValue
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.tasks.await
//import mk.com.ukim.finki.mpip.instanim.data.entity.User
//import mk.com.ukim.finki.mpip.instanim.data.model.Resource
//
//object UserRepository {
//
//
//    suspend fun getUsers(): Resource<List<User>> {
//        lateinit var resource: Resource<List<User>>
//
//        userDb.get().addOnSuccessListener {
//            val idUserMap = it.getValue<Map<String, User>>()
//            resource = if (idUserMap != null) {
//                Resource.success(idUserMap.values.toList())
//            } else {
//                Resource.error(null, "User not found")
//            }
//        }.addOnFailureListener {
//            it.message?.let { msg ->
//                resource = Resource.error(null, msg)
//            } ?: run {
//                resource = Resource.error(null, "There was an error creating the user")
//            }
//        }.await()
//
//        return resource
//    }
//
//    suspend fun getUser(userId: String): Resource<User> {
//        lateinit var resource: Resource<User>
//
//
//
//        return resource
//    }
//
//    suspend fun createUser(user: User): Resource<Unit> {
//        lateinit var resource: Resource<Unit>
//
//        userDb.child(user.uid).setValue(user).addOnSuccessListener {
//            resource = Resource.success(Unit)
//        }.addOnFailureListener {
//            it.message?.let { msg ->
//                resource = Resource.error(null, msg)
//            } ?: run {
//                resource = Resource.error(null, "There was an error creating the user")
//            }
//        }.await()
//
//        return resource
//    }
//
//    suspend fun updateUser(user: User): Resource<Unit> {
//        lateinit var resource: Resource<Unit>
//
//        val userValues = user.toMap()
//        val update = hashMapOf<String, Any>(
//            user.uid to userValues
//        )
//
//        userDb.updateChildren(update).addOnSuccessListener {
//            resource = Resource.success(Unit)
//        }.addOnFailureListener {
//            it.message?.let { msg ->
//                resource = Resource.error(null, msg)
//            } ?: run {
//                resource = Resource.error(null, "There was an error updating the user")
//            }
//        }.await()
//
//        return resource
//    }
//
//
//    suspend fun removeUser(userId: String): Resource<Unit> {
//        lateinit var resource: Resource<Unit>
//
//        userDb.child(userId).removeValue().addOnSuccessListener {
//            resource = Resource.success(Unit)
//        }.addOnFailureListener {
//            it.message?.let { msg ->
//                resource = Resource.error(null, msg)
//            } ?: run {
//                resource = Resource.error(null, "There was an error removing the user")
//            }
//        }.await()
//
//        return resource
//    }
//
//}