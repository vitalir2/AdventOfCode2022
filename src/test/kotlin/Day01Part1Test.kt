import day01.Day01
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01Part1Test : StringSpec() {

    init {
        "one elf - should return its calories sum" {
            val input = listOf("1000", "2000", "3000")

            val result = Day01.part1(input)

            result shouldBe 6000
        }

        "two elfs - should return max of two sums" {
            val input = listOf(
                "1000", "2000", "3000", "",
                "2000", "30000",
            )

            val result = Day01.part1(input)

            result shouldBe 32_000
        }

        "three elfs - return max" {
            val input = listOf(
                "2000", "3000", "",
                "5000", "3000", "",
                "1000",
            )

            val result = Day01.part1(input)

            result shouldBe  8000
        }
    }
}
