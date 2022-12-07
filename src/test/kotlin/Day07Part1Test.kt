import day07.Day07
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07Part1Test : StringSpec() {

    init {
        "example from AOC" {
            val input = """
                ${'$'} cd /
                ${'$'} ls
                dir a
                14848514 b.txt
                8504156 c.dat
                dir d
                ${'$'} cd a
                ${'$'} ls
                dir e
                29116 f
                2557 g
                62596 h.lst
                ${'$'} cd e
                ${'$'} ls
                584 i
                ${'$'} cd ..
                ${'$'} cd ..
                ${'$'} cd d
                ${'$'} ls
                4060174 j
                8033020 d.log
                5626152 d.ext
                7214296 k
            """.trimIndent().lines()

            val result = Day07.part1(input)

            result shouldBe 95437
        }

        "a root folder and a file which greater than 100 thousand" {
            val input = """
                $ cd /
                $ ls
                146855 a.txt
            """.trimIndent().lines()

            val result = Day07.part1(input)

            result shouldBe 0
        }

        "a root folder less that 100 thousands in size" {
            val input = """
                $ cd /
                $ ls
                75000 file
                dir a
                $ cd a
                $ ls
                15 ab
            """.trimIndent().lines()

            val result = Day07.part1(input)

            result shouldBe 75030
        }
    }
}
