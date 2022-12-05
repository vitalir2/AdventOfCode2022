import day04.Day04
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day04Part1Test : StringSpec() {

    init {
        val testedMethod = Day04::part1

        "both ranges do not intersect" {
            val input = listOf("2-4,6-7")

            val result = testedMethod(input)

            result shouldBe 0
        }

        "first range contains the second one" {
            val input = listOf("1-11,5-6")

            val result = testedMethod(input)

            result shouldBe 1
        }

        "second range contains the first one" {
            val input = listOf("1-2,1-5")

            val result = testedMethod(input)

            result shouldBe 1
        }

        "both ranges intersects" {
            val input = listOf("5-7,6-8")

            val result = testedMethod(input)

            result shouldBe 0
        }

        "both are equal" {
            val input = listOf("5-5,5-5")

            val result = testedMethod(input)

            result shouldBe 1
        }

        "two ranges fully contain other ones" {
            val input = listOf("5-6,1-7", "11-16,12-13")

            val result = testedMethod(input)

            result shouldBe 2
        }
    }
}
