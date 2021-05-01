package mk.com.ukim.finki.mpip.instanim.data.entity

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    val postId: String = "",
    val userId: String? = null,
    val createdAt: Long? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val description: String? = null,
    val imageUri: String? = null,
    val likedBy: MutableList<String> = mutableListOf(),
    val comments: List<Int> = listOf()
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "postId" to postId,
            "userId" to userId,
            "createdAt" to createdAt,
            "lat" to lat,
            "lng" to lng,
            "description" to description,
            "imageUri" to imageUri,
            "likedBy" to likedBy,
            "comments" to comments
        )
    }
}