import day07.Day07
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day07Part2Test : StringSpec() {

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

            val result = Day07.part2(input)

            result shouldBe 24933642
        }
    }
}
