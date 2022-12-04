package day04

import Challenge
import toIntRange

object Day04 : Challenge {
    override val day: Int = 4

    override fun part1(input: List<String>): Int {
        return countNumberOfElvesPairs(input) { firstInterval, secondInterval ->
            firstInterval.containsAll(secondInterval) || secondInterval.containsAll(firstInterval)
        }
    }

    override fun part2(input: List<String>): Int {
        return countNumberOfElvesPairs(input) { firstInterval, secondInterval ->
            (firstInterval intersect secondInterval).isNotEmpty()
        }
    }

    private fun countNumberOfElvesPairs(
        input: List<String>,
        predicate: (firstInterval: Set<Int>, secondInterval: Set<Int>) -> Boolean,
    ): Int {
        return input
            .asSequence()
            .map { pairOfElves -> pairOfElves.split(",") }
            .map { elves -> elves.map { it.toIntRange("-").toSet() } }
            .filter { (firstInterval, secondInterval) -> predicate(firstInterval, secondInterval) }
            .count()
    }
}
