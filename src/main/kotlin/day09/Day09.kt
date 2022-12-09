package day09

import Challenge
import utils.Coordinates
import kotlin.math.absoluteValue
import kotlin.math.sign

object Day09 : Challenge {
    override val day: Int = 9

    override fun part1(input: List<String>): Any {

        var headCoordinates = Coordinates(0, 0)
        var tailCoordinates = headCoordinates
        val lastTailCoordinates = mutableSetOf(tailCoordinates)

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
                tailCoordinates = tailCoordinates moveToTouch headCoordinates
                lastTailCoordinates.add(tailCoordinates)
            }
        }


        return lastTailCoordinates.count()
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
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
}
