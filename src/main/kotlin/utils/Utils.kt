import java.math.BigInteger
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int, name: String) = Path.of(
    "src", "main", "kotlin",
    "day${day.asTwoDigitNumber}", "$name.txt"
)
    .readLines()

val Int.asTwoDigitNumber: String
    get() = "%02d".format(this)

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun String.split(numberOfParts: Int): List<String> {
    return chunked(length / numberOfParts)
}
