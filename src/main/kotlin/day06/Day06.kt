package day06

import Challenge

object Day06 : Challenge {
    override val day: Int = 6

    override fun part1(input: List<String>): Any {
        var result = 0
        val currentChars = mutableListOf<Char>()
        for (char in input.joinToString("")) {
            result++
            currentChars.add(char)
            when {
                currentChars.size == MARK_LENGTH && currentChars.toSet().size == MARK_LENGTH -> break
                currentChars.size == MARK_LENGTH -> currentChars.removeFirst()
                else -> continue
            }
        }
        return result
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
    }

    private const val MARK_LENGTH = 4
}
