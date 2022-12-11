abstract class Challenge(private val day: Int) {
    abstract fun part1(input: List<String>): Any
    abstract fun part2(input: List<String>): Any

    fun runChallenge(part: Int) {
        val input = readInput()
        when (part) {
            1 ->     println(part1(input))
            2 ->     println(part2(input))
            else -> error("Invalid part, exists only two parts")
        }
    }

    private fun readInput(): List<String> {
        return readInput(day, "Day${day.asTwoDigitNumber}")
    }
}
