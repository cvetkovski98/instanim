package mk.com.ukim.finki.mpip.instanim.data.model

import mk.com.ukim.finki.mpip.instanim.data.entity.Post

class PostBuilder(
    var postId: String = "",
    var userId: String? = null,
    var createdAt: Long? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var description: String? = null,
    var imageUri: String? = null,
) {

    fun toPost(): Post {
        return Post(
            postId = postId,
            userId = userId,
            createdAt = createdAt,
            lat = lat,
            lng = lng,
            description = description,
            imageUri = imageUri.toString()
        )
    }
}