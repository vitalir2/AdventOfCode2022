import day03.Day03
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day03Part1Test : StringSpec() {

    init {
        val testedMethod = Day03::part1

        "common lowercase" {
            val input = listOf("vJrwpWtwJgWrhcsFMMfFFhFp")

            val result = testedMethod(input)

            result shouldBe 16
        }

        "common uppercase" {
            val input = listOf("jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL")

            val result = testedMethod(input)

            result shouldBe 38
        }

        "result is sum of priorities" {
            val input = listOf("wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "CrZsJsPPZsGzwwsLwLmpwMDw")

            val result = testedMethod(input)

            // v + s
            result shouldBe 22 + 19
        }
    }
}
