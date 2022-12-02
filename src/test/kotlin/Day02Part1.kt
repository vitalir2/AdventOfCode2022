import day02.Day02
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day02Part1 : StringSpec() {

    init {

        val testedMethod = Day02::part1

        "rock and draw" {
            val input = listOf("A X")

            val result = testedMethod(input)

            result shouldBe 4
        }

        "rock and lose" {
            val input = listOf("B X")

            val result = testedMethod(input)

            result shouldBe 1
        }

        "rock and win" {
            val input = listOf("C X")

            val result = testedMethod(input)

            result shouldBe 7
        }

        "paper and draw" {
            val input = listOf("B Y")

            val result = testedMethod(input)

            result shouldBe 5
        }

        "paper and lose" {
            val input = listOf("C Y")

            val result = testedMethod(input)

            result shouldBe 2
        }


        "paper and win" {
            val input = listOf("A Y")

            val result = testedMethod(input)

            result shouldBe 8
        }

        "scissor and draw" {
            val input = listOf("C Z")

            val result = testedMethod(input)

            result shouldBe 6
        }

        "scissor and lose" {
            val input = listOf("A Z")

            val result = testedMethod(input)

            result shouldBe 3
        }


        "scissor and win" {
            val input = listOf("B Z")

            val result = testedMethod(input)

            result shouldBe 9
        }
    }
}
