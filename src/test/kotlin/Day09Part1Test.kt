import day09.Day09
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day09Part1Test : StringSpec() {

    init {
        "AOC example" {
            val input = """
                R 4
                U 4
                L 3
                D 1
                R 4
                D 1
                L 5
                R 2
            """.trimIndent().lines()

            val result = Day09.part1(input)

            result shouldBe 13
        }
    }
}
