import day12.Day12
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day12Test : StringSpec() {

    init {
        "AOC example, part 1" {
            val input = """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent().lines()

            val result = Day12.part1(input)

            result shouldBe 31
        }

        "AOC example, part 2" {
            val input = """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent().lines()

            val result = Day12.part2(input)

            result shouldBe 29
        }
    }
}
