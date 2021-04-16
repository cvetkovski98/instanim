package mk.com.ukim.finki.mpip.instanim.data.entity

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var authUser: FirebaseUser? = null,
    val username: String = "",
    val description: String? = null,
    val follows: List<String> = listOf(),
    val followedBy: List<String> = listOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "description" to description,
            "follows" to follows,
            "followedBy" to followedBy,
        )
    }
}