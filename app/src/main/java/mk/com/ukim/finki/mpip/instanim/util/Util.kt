package mk.com.ukim.finki.mpip.instanim.util

object Util {
    fun generateId(): String {
        val source = "abcdefghijklmnopqrstuvwxyyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
        val outputStrLength = 20L
        val randomString = (1..outputStrLength)
            .map { kotlin.random.Random.nextInt(0, source.length) }
            .map { i -> source[i] }
            .joinToString("");
        return randomString
    }
}