package day01

import readInput

object Day01  {
    fun part1(input: List<String>): Int {
        val caloriesForEveryElf = mutableListOf<Int>()
        var currentElfCalories = 0
        for (element in input) {
            if (element.isEmpty()) {
                caloriesForEveryElf.add(currentElfCalories)
                currentElfCalories = 0
            } else {
                currentElfCalories += element.toInt()
            }
        }
        caloriesForEveryElf.add(currentElfCalories)
        return caloriesForEveryElf.max()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }
}

fun main(args: Array<String>) {
    val input = readInput(1, "Day01")
    when (args[0].toInt()) {
        1 ->     println(Day01.part1(input))
        2 ->     println(Day01.part2(input))
        else -> error("Invalid part, exists only two parts")
    }
}
