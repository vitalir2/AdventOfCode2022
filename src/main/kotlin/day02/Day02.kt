package day02

import Challenge

object Day02 : Challenge {

    /**
     * my:
     * X - Rock
     * Y - Paper
     * Z - Scissors
     * opponent:
     * A - Rock
     * B - Paper
     * C - Scissors
     */
    private val myTurnToScores = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3,
    )

    /**
     * First - draw
     * Second - lose
     * Third - win
     */
    private val myTurnToOpponentMoves = mapOf(
        "X" to "ABC",
        "Y" to "BCA",
        "Z" to "CAB",
    )

    /**
     * behavior:
     * X - lose
     * Y - draw
     * Z - win
     * opponent turn:
     * A - Rock
     * B - Paper
     * C - Scissors
     */
    private val myBehaviorToScores = mapOf(
        "X" to 0,
        "Y" to 3,
        "Z" to 6,
    )

    private val myBehaviorToOpponentTurns = mapOf(
        "X" to "BCA",
        "Y" to "ABC",
        "Z" to "CAB",
    )

    override val day: Int = 2

    override fun part1(input: List<String>): Int {
        return input.sumOf {
            val (opponentChoice, myChoice) = it.split(" ")
            val opponentMoves = myTurnToOpponentMoves[myChoice]!!
            var turnScore = 0
            for ((index, possibleMove) in opponentMoves.withIndex()) {
                if (possibleMove == opponentChoice.first()) {
                    turnScore = when (index) {
                        0 -> 3
                        1 -> 0
                        2 -> 6
                        else -> error("Invalid index")
                    }
                }
            }
            myTurnToScores[myChoice]!! + turnScore
        }
    }

    override fun part2(input: List<String>): Int {
        return input.sumOf {
            val (opponentChoice, myBehavior) = it.split(" ")
            val opponentMoves = myBehaviorToOpponentTurns[myBehavior]!!
            var chosenScore = 0
            for ((index, possibleMove) in opponentMoves.withIndex()) {
                if (possibleMove == opponentChoice.first()) {
                    chosenScore = when (index) {
                        0 -> 1
                        1 -> 2
                        2 -> 3
                        else -> error("Invalid index")
                    }
                }
            }
            myBehaviorToScores[myBehavior]!! + chosenScore
        }
    }
}
