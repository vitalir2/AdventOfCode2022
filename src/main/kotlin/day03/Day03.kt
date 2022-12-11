package day03

import Challenge
import split

object Day03 : Challenge(3) {

    override fun part1(input: List<String>): Int {
        return input.sumOf(::calculatePriority)
    }

    override fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { rucksacks ->
                val commonItem = rucksacks
                    .drop(1)
                    .fold(rucksacks.first().toSet()) { acc, rucksack -> acc.intersect(rucksack.toSet()) }
                    .first()
                calculateItemPriority(commonItem)
            }
    }

    private fun calculatePriority(rucksack: String): Int {
        val (firstCompartment, secondCompartment) = rucksack.split(numberOfParts = 2)
        val commonItem = firstCompartment.toSet().intersect(secondCompartment.toSet()).first()
        return calculateItemPriority(commonItem)
    }

    private fun calculateItemPriority(item: Char): Int {
        return when {
            item.isLowerCase() -> item.code - 96
            item.isUpperCase() -> item.code - 38
            else -> error("Invalid item type")
        }
    }
}
