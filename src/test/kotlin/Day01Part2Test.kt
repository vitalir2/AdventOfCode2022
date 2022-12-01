import day01.Day01
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day01Part2Test : StringSpec() {

    init {
        "one elf - returns max" {
            val input = listOf("1000", "2000", "3000")

            val result = Day01.part2(input)

            result shouldBe 6000
        }

        "two elfs - return their sum of calories" {
            val input = listOf("1000", "2000", "", "5000")

            val result = Day01.part2(input)

            result shouldBe 8000
        }

        "many elfs - return sum of top 3 max calories" {
            val input = listOf(
                "1000", "2000", "",
                "3000", "500", "",
                "2000", "",
                "5000", "1000",
            )

            val result = Day01.part2(input)

            result shouldBe 12_500
        }
    }
}
