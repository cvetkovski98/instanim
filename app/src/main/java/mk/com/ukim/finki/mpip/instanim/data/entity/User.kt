package mk.com.ukim.finki.mpip.instanim.data.entity

data class User(
    val user_id: Long,
    val username: String,
    val email: String,
    val description: String?,
    val follows: List<String> = listOf(),
    val followedBy: List<String> = listOf()
) {
}