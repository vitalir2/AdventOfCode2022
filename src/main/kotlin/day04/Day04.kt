package day04

import Challenge
import toIntRange

object Day04 : Challenge {
    override val day: Int = 4

    override fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { pairOfElves -> pairOfElves.split(",") }
            .map { elves -> elves.map { it.toIntRange("-").toSet() } }
            .filter { (firstElfInterval, secondElfInterval) ->
                firstElfInterval.containsAll(secondElfInterval) || secondElfInterval.containsAll(firstElfInterval)
            }
            .count()
    }

    override fun part2(input: List<String>): Int {
        TODO("Not yet implemented")
    }
}
