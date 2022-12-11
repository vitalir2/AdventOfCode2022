package day09

import Challenge
import utils.Coordinates
import kotlin.math.absoluteValue
import kotlin.math.sign

object Day09 : Challenge(9) {

    override fun part1(input: List<String>): Any {
        return modelRopeMovement(input, ROPE_LENGTH_PART_1)
    }

    override fun part2(input: List<String>): Any {
        return modelRopeMovement(input, ROPE_LENGTH_PART_2)
    }

    private fun modelRopeMovement(input: List<String>, ropeLength: Int): Int {
        val rope = List(ropeLength - 1) { Coordinates(0, 0) }.toMutableList() // last - tail
        var headCoordinates = rope.first()
        val tailCoordinatesHistory = mutableSetOf(rope.last())

        for (command in input) {
            val splitCommand = command.split(" ")
            val direction = splitCommand[0].toCharArray().first()
            val steps = splitCommand[1].toInt()
            repeat(steps) {
                when (direction) {
                    'R' -> headCoordinates = headCoordinates.moveRight()
                    'D' -> headCoordinates = headCoordinates.moveDown()
                    'L' -> headCoordinates = headCoordinates.moveLeft()
                    'U' -> headCoordinates = headCoordinates.moveUp()
                }
                for (ropePartIndex in rope.indices) {
                    var ropePart = rope[ropePartIndex]
                    val previousRopePart = rope.getOrElse(ropePartIndex-1) { headCoordinates }
                    ropePart = ropePart moveToTouch previousRopePart
                    if (ropePartIndex == rope.lastIndex) {
                        tailCoordinatesHistory.add(ropePart)
                    }
                    rope[ropePartIndex] = ropePart
                }
            }
        }


        return tailCoordinatesHistory.count()
    }

    private infix fun Coordinates.moveToTouch(other: Coordinates): Coordinates {
        return when {
            this == other || this adjacentTo other -> this
            else -> {
                val coordinatesDiff = other - this
                // not adjacent => on the same row or column
                when (coordinatesDiff.x.absoluteValue + coordinatesDiff.y.absoluteValue) {
                    2 -> this.copy(x = x + coordinatesDiff.x / 2, y = y + coordinatesDiff.y / 2)
                    else -> this.copy(x = x + coordinatesDiff.x.sign, y = y + coordinatesDiff.y.sign)
                }
            }
        }
    }

    private const val ROPE_LENGTH_PART_1 = 2
    private const val ROPE_LENGTH_PART_2 = 10
}
