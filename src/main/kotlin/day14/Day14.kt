package day14

import Challenge
import utils.Coordinates
import utils.toCoordinates

object Day14 : Challenge(day = 14) {
    override fun part1(input: List<String>): Any {
        return emulateInput(input, hasFloor = false)
    }

    override fun part2(input: List<String>): Any {
        return emulateInput(input, hasFloor = true)
    }

    private fun emulateInput(
        input: List<String>,
        hasFloor: Boolean,
    ): Int {
        val emulation = createEmulation(input, hasFloor = hasFloor)
        emulation.start()
        return emulation.sandCount
    }

    private fun createEmulation(input: List<String>, hasFloor: Boolean): SandEmulation {
        val rocks = input.map(::parseRock)
        return SandEmulation(
            rocks = rocks,
            sandSource = Coordinates(500, 0),
            hasFloor = hasFloor,
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
        val hasFloor: Boolean,
    ) {

        private var sandFellInVoid = false

        private val allRockCoordinates by lazy {
            rocks.flatMap(Rock::allCoordinates).toSet()
        }

        private val bottomLineY by lazy {
            allRockCoordinates.maxOf(Coordinates::y) + 2
        }

        private val landedSand: MutableSet<Coordinates> = mutableSetOf()

        val sandCount: Int
            get() = landedSand.size

        fun start() {
            resetSimulation()
            while (!isCompletedSimulation()) {
                emulateSand()
            }
        }

        private fun emulateSand() {
            var sandCoordinates = sandSource
            var emulationStep = 0
            while (emulationStep < bottomLineY) {
                val newCoordinates = moveSandOrBlocked(sandCoordinates)
                if (newCoordinates == null) {
                    landedSand.add(sandCoordinates)
                    break
                }
                sandCoordinates = newCoordinates
                emulationStep++
            }
            sandFellInVoid = emulationStep >= bottomLineY
        }

        private fun resetSimulation() {
            sandFellInVoid = false
            landedSand.clear()
        }

        private fun isCompletedSimulation(): Boolean {
            return if (hasFloor) {
                sandSource in landedSand
            } else {
                sandFellInVoid
            }
        }

        private fun moveSandOrBlocked(sand: Coordinates): Coordinates? {
            for (dx in dxMoveOrder) {
                val newSandCoordinates = sand.move(dx = dx, dy = 1)
                if (newSandCoordinates !in landedSand
                    && newSandCoordinates !in allRockCoordinates
                    && !(hasFloor && isOnFloor(newSandCoordinates))
                ) {
                    return newSandCoordinates
                }
            }
            return null
        }

        private fun isOnFloor(coordinates: Coordinates): Boolean {
            return coordinates.y == bottomLineY
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
