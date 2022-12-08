import day08.Day08
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day08Part1Test : StringSpec() {

    init {
        "example AOC" {
            val input = """
                30373
                25512
                65332
                33549
                35390
            """.trimIndent().lines()

            val result = Day08.part1(input)

            result shouldBe 21
        }
    }
}
