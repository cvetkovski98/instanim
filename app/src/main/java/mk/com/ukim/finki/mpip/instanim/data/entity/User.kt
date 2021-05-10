package mk.com.ukim.finki.mpip.instanim.data.entity

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val username: String = "",
    val name: String = "",
    val lastName: String = "",
    val imageUri: String = "",
    val bio: String? = null,
    val follows: List<String> = listOf(),
    val followedBy: List<String> = listOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "username" to username,
            "name" to name,
            "lastName" to lastName,
            "imageUri" to imageUri,
            "bio" to bio,
            "follows" to follows,
            "followedBy" to followedBy,
        )
    }
}
