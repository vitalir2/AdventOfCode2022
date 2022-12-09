import day09.Day09
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day09Part2Test : StringSpec() {

    init {
        "AOC example" {
            val input = """
                R 5
                U 8
                L 8
                D 3
                R 17
                D 10
                L 25
                U 20
            """.trimIndent().lines()

            val result = Day09.part2(input)

            result shouldBe 36
        }
    }
}
