package mk.com.ukim.finki.mpip.instanim.data.entity

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    val postId: String = "",
    val userId: String? = null,
    val createdAt: Long? = null,
    val location: String? = null,
    val description: String? = null,
    val imageUri: String? = null,
    val likedBy: List<Int> = listOf(),
    val comments: List<Int> = listOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "postId" to postId,
            "userId" to userId,
            "createdAt" to createdAt,
            "location" to location,
            "description" to description,
            "imageUri" to imageUri,
            "likedBy" to likedBy,
            "comments" to comments
        )
    }
}