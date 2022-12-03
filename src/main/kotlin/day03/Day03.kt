package day03

import Challenge
import split

object Day03 : Challenge {
    override val day: Int = 3

    override fun part1(input: List<String>): Int {
        return input.sumOf(::calculatePriority)
    }

    override fun part2(input: List<String>): Int {
        TODO("Not yet implemented")
    }

    private fun calculatePriority(rucksack: String): Int {
        val (firstCompartment, secondCompartment) = rucksack.split(numberOfParts = 2)
        val commonItem = firstCompartment.toSet().intersect(secondCompartment.toSet()).first()
        return when {
            commonItem.isLowerCase() -> commonItem.code - 96
            commonItem.isUpperCase() -> commonItem.code - 38
            else -> error("Invalid item type")
        }
    }
}
