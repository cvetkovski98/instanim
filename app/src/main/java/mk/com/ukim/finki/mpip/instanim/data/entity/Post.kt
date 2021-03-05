package mk.com.ukim.finki.mpip.instanim.data.entity

data class Post(
    val post_id: Int,
    val user_id: Int,
    val created_at: Long,
    val location: String?,
    val description: String?,
    val liked_by: List<Int> = listOf(),
    val comments: List<Int> = listOf()
) {
}