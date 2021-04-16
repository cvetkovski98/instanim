package mk.com.ukim.finki.mpip.instanim.util

import kotlin.random.Random

object Util {
    fun generateId(): String {
        val source = "abcdefghijklmnopqrstuvwxyyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
        val outputStrLength = 20
        return (1..outputStrLength)
            .map { Random.nextInt(0, source.length) }
            .map { i -> source[i] }
            .joinToString("")
    }
}