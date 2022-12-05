import day05.Day05
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day05Part2Test : StringSpec() {

    init {
        val testedMethod = Day05::part2

        "move one block" {
            val input = listOf(
                "[E]    ",
                "[D]    ",
                " 1   2 ",
                "",
                "move 1 from 1 to 2",
            )

            val result = testedMethod(input)

            result shouldBe "DE"
        }

        "move two blocks" {
            val input = listOf(
                "[E]     [P]",
                "[D]     [C]",
                "[M]     [Y]",
                " 1   2   3 ",
                "",
                "move 2 from 3 to 2",
            )

            val result = testedMethod(input)

            result shouldBe "EPY"
        }

        "many move commands" {
            val input = listOf(
                "[E]     [P]",
                "[D]     [C]",
                "[M]     [Y]",
                " 1   2   3 ",
                "",
                "move 2 from 3 to 2",
                "move 1 from 1 to 2",
                "move 1 from 2 to 3",
            )

            val result = testedMethod(input)

            result shouldBe "DPE"
        }
    }
}
