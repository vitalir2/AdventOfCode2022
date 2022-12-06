import day06.Day06
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day06Part2Test : StringSpec() {

    init {
        "first 14 chars is mark" {
            val input = listOf(('a'..'z').toList().joinToString(""))

            val result = Day06.part2(input)

            result shouldBe 14
        }

        "first char is duplicated" {
            val input = listOf("a" + ('a'..'z').toList().joinToString(""))

            val result = Day06.part2(input)

            result shouldBe 15
        }

        "example input 4" {
            val input = listOf("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")

            val result = Day06.part2(input)

            result shouldBe 26
        }
    }
}
