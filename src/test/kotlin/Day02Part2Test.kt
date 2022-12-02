import day02.Day02
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day02Part2Test : StringSpec() {

    init {
        val testedMethod = Day02::part2

        "opponent rock and should lose" {
            val input = listOf("A X")

            val result = testedMethod(input)

            result shouldBe 3
        }

        "opponent rock and should draw" {
            val input = listOf("A Y")

            val result = testedMethod(input)

            result shouldBe 4
        }

        "opponent rock and should win" {
            val input = listOf("A Z")

            val result = testedMethod(input)

            result shouldBe 8
        }

        "opponent paper and should lose" {
            val input = listOf("B X")

            val result = testedMethod(input)

            result shouldBe 1
        }

        "opponent paper and should draw" {
            val input = listOf("B Y")

            val result = testedMethod(input)

            result shouldBe 5
        }

        "opponent paper and should win" {
            val input = listOf("B Z")

            val result = testedMethod(input)

            result shouldBe 9
        }

        "opponent scissors and should lose" {
            val input = listOf("C X")

            val result = testedMethod(input)

            result shouldBe 2
        }

        "opponent scissors and should draw" {
            val input = listOf("C Y")

            val result = testedMethod(input)

            result shouldBe 6
        }

        "opponent scissors and should win" {
            val input = listOf("C Z")

            val result = testedMethod(input)

            result shouldBe 7
        }

        "many turns return sum of results" {
            val input = listOf("C Z", "A X")

            val result = testedMethod(input)

            result shouldBe 10
        }
    }
}
