import day13.Day13
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day13Test : StringSpec() {

    init {
        "part 1, AOC example" {
            val input = """
                [1,1,3,1,1]
                [1,1,5,1,1]

                [[1],[2,3,4]]
                [[1],4]

                [9]
                [[8,7,6]]

                [[4,4],4,4]
                [[4,4],4,4,4]

                [7,7,7,7]
                [7,7,7]

                []
                [3]

                [[[]]]
                [[]]

                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
            """.trimIndent().lines()

            val result = Day13.part1(input)

            result shouldBe 13
        }

        "part 2, AOC example" {
            val input = """
                [1,1,3,1,1]
                [1,1,5,1,1]

                [[1],[2,3,4]]
                [[1],4]

                [9]
                [[8,7,6]]

                [[4,4],4,4]
                [[4,4],4,4,4]

                [7,7,7,7]
                [7,7,7]

                []
                [3]

                [[[]]]
                [[]]

                [1,[2,[3,[4,[5,6,7]]]],8,9]
                [1,[2,[3,[4,[5,6,0]]]],8,9]
            """.trimIndent().lines()

            val result = Day13.part2(input)

            result shouldBe 140
        }
    }
}
