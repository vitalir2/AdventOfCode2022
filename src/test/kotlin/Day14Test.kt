import day14.Day14
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day14Test : StringSpec() {

    init {
        "part 1, AOC example" {
            val input = """
                498,4 -> 498,6 -> 496,6
                503,4 -> 502,4 -> 502,9 -> 494,9
            """.trimIndent().lines()

            val result = Day14.part1(input)

            result shouldBe 24
        }
    }
}
