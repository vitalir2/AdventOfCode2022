import day06.Day06
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day06Part1Test : StringSpec() {

    init {
        "first 4 chars is mark" {
            val input = listOf(('a'..'d').toString())

            val result = Day06.part1(input)

            result shouldBe 4
        }

        "first char is duplicated" {
            val input = listOf("abcad")

            val result = Day06.part1(input)

            result shouldBe 5
        }

        "two times character is duplicated" {
            val input = listOf("ababde")

            val result = Day06.part1(input)

            result shouldBe 6
        }
    }
}
