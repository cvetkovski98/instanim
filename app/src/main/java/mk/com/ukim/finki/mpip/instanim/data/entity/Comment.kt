package mk.com.ukim.finki.mpip.instanim.data.entity

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    val userId: String = "",
    val content: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "content" to content
        )
    }
}
