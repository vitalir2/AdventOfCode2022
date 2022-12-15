package day14

import Challenge
import utils.Coordinates

object Day14 : Challenge(day = 14) {
    override fun part1(input: List<String>): Any {
        val emulation = parseInput(input)
        emulation.start()
        return emulation.sandCount
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
    }

    private fun parseInput(input: List<String>): SandEmulation {
        val rocks = input.map(::parseRock)
        return SandEmulation(
            rocks = rocks,
            sandSource = Coordinates(500, 0),
        )
    }

    private fun parseRock(line: String): SandEmulation.Rock {
        return line
            .split(" -> ")
            .map(String::toCoordinates)
            .let(SandEmulation::Rock)
    }

    /**
     * X greater -> goes right
     * Y greater -> goes down
     */
    private class SandEmulation(
        val rocks: List<Rock>,
        val sandSource: Coordinates,
    ) {

        private val fellInVoidThreshold = 1000

        private val allRockCoordinates by lazy {
            rocks.flatMap(Rock::allCoordinates)
        }

        private val landedSand: MutableList<Coordinates> = mutableListOf()

        val sandCount: Int
            get() = landedSand.size

        fun start() {
            var sandFellInVoid = false
            while (!sandFellInVoid) {
                var sandCoordinates = sandSource
                var sandStep = 0
                while (sandStep < fellInVoidThreshold) {
                    val simulateSandResult = moveSandOrBlocked(sandCoordinates)
                    if (simulateSandResult == null) {
                        landedSand.add(sandCoordinates)
                        break
                    }
                    sandCoordinates = simulateSandResult
                    sandStep++
                }
                sandFellInVoid = sandStep >= fellInVoidThreshold
            }
        }

        private fun moveSandOrBlocked(sand: Coordinates): Coordinates? {
            for (dx in dxMoveOrder) {
                val newSandCoordinates = sand.move(dx = dx, dy = 1)
                if (newSandCoordinates !in landedSand && newSandCoordinates !in allRockCoordinates) {
                    return newSandCoordinates
                }
            }
            return null
        }

        data class Rock(
            val corners: List<Coordinates>,
        ) {
            val allCoordinates: Sequence<Coordinates> = sequence {
                var prevCorner: Coordinates? = null
                for (corner in corners) {
                    if (prevCorner != null) {
                        yieldAll(prevCorner.createLine(corner))
                    } else {
                        yield(corner)
                    }
                    prevCorner = corner
                }
            }
        }

        companion object {
            private val dxMoveOrder = listOf(0, -1, 1)
        }
    }
}

private fun String.toCoordinates(separator: String = ","): Coordinates {
    return split(",").map(String::toInt).let { (x, y) -> Coordinates(x = x, y = y) }
}
