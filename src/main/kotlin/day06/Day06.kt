package day06

import Challenge

object Day06 : Challenge {
    override val day: Int = 6

    override fun part1(input: List<String>): Any {
        return findDistinctCharsEnd(input.joinToString(""), MARKER_LENGTH)
    }

    override fun part2(input: List<String>): Any {
        return findDistinctCharsEnd(input.joinToString(""), MESSAGE_LENGTH)
    }

    private fun findDistinctCharsEnd(input: String, lengthOfChars: Int): Int {
        var result = 0
        val currentChars = mutableListOf<Char>()
        for (char in input) {
            result++
            currentChars.add(char)
            when {
                currentChars.size == lengthOfChars && currentChars.toSet().size == lengthOfChars -> break
                currentChars.size == lengthOfChars -> currentChars.removeFirst()
                else -> continue
            }
        }
        return result
    }

    private const val MARKER_LENGTH = 4
    private const val MESSAGE_LENGTH = 14
}
