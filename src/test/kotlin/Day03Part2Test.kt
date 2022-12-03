import day03.Day03
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day03Part2Test : StringSpec() {

    init {
        val testedMethod = Day03::part2

        // r is badge (common item)
        val firstGroup = listOf("vJrwpWtwJgWrhcsFMMfFFhFp", "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", "PmmdzqPrVvPwwTWBwg")
        val expectedFirstGroupPriority = 18

        // Z is badge
        val secondGroup = listOf("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "ttgJtRGJQctTZtZT", "CrZsJsPPZsGzwwsLwLmpwMDw")
        val expectedSecondGroupPriority = 52
        "common lowercase" {
            val input = firstGroup

            val result = testedMethod(input)

            result shouldBe expectedFirstGroupPriority
        }

        "common uppercase" {
            val input = secondGroup

            val result = testedMethod(input)

            result shouldBe expectedSecondGroupPriority
        }

        "result is sum of badge priorities" {
            val input = firstGroup + secondGroup

            val result = testedMethod(input)

            result shouldBe expectedFirstGroupPriority + expectedSecondGroupPriority
        }
    }
}
