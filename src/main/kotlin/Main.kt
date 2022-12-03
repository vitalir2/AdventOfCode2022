import day03.Day03

fun main(args: Array<String>) {
    val challenge: Challenge = Day03
    val input = readInput(challenge.day, "Day${challenge.day.asTwoDigitNumber}")
    when (args[0].toInt()) {
        1 ->     println(challenge.part1(input))
        2 ->     println(challenge.part2(input))
        else -> error("Invalid part, exists only two parts")
    }
}
