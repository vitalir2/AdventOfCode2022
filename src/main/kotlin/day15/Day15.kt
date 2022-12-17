package day15

import Challenge
import utils.Coordinates
import utils.toCoordinates
import kotlin.math.absoluteValue

object Day15 : Challenge(day = 15) {
    override fun part1(input: List<String>): Any {
        val y = input.first().toInt()
        val beaconZone = createZone(
            input = input.drop(1),
            isDebug = false,
        )
        return beaconZone
            .horizontalPositionsWithoutBeacon(yPos = y)
            .size
    }

    override fun part2(input: List<String>): Any {
        val maxCoordinateValue = input.first().toInt() * 2
        val beaconZone = createZone(
            input = input.drop(1),
            isDebug = false,
        )

        if (beaconZone.isDebug) {
            beaconZone.drawMap()
        }

        val distressBeaconCoordinates = beaconZone.findDistressBeaconFast(maxCoordinateValue)

        val tuningFrequencyCoefficient = 4_000_000L
        fun calculateTuningFrequency(beaconCoordinates: Coordinates): Long {
            return beaconCoordinates.x * tuningFrequencyCoefficient + beaconCoordinates.y
        }

        return calculateTuningFrequency(distressBeaconCoordinates)
    }

    private fun createZone(
        input: List<String>,
        isDebug: Boolean,
    ): BeaconZone {
        return input
            .map { line -> line.split(": ") }
            .map { (sensorStr, beaconStr) ->
                val sensorCoordinates = parseCoordinates(sensorStr.removePrefix("Sensor at "))
                val beaconCoordinates = parseCoordinates(beaconStr.removePrefix("closest beacon is at "))
                val beacon = BeaconZone.Beacon(beaconCoordinates)
                val sensor = BeaconZone.Sensor(
                    coordinates = sensorCoordinates,
                    closestBeacon = beacon,
                    maxDistance = sensorCoordinates.manhattanDistance(beaconCoordinates),
                )
                sensor to beacon
            }
            .let { sensorsWithBeacon ->
                BeaconZone(
                    sensors = sensorsWithBeacon.map(Pair<BeaconZone.Sensor, *>::first),
                    beacons = sensorsWithBeacon.map(Pair<*, BeaconZone.Beacon>::second),
                    isDebug = isDebug,
                )
            }
    }

    private fun parseCoordinates(string: String): Coordinates {
        return string
            .replace("x=", "")
            .replace("y=", "")
            .toCoordinates(separator = ", ")
    }

    private class BeaconZone(
        val sensors: List<Sensor>,
        val beacons: List<Beacon>,
        val isDebug: Boolean,
    ) {

        private val beaconsCoordinates: Set<Coordinates> by lazy {
            beacons.map(Beacon::coordinates).toSet()
        }

        private val sensorsCoordinates: Set<Coordinates> by lazy {
            sensors.map(Sensor::coordinates).toSet()
        }

        private val initialCoordinates: Set<Coordinates> by lazy {
            beaconsCoordinates + sensorsCoordinates
        }

        private val maxDistanceOfSensors: Int by lazy {
            sensors.maxOf(Sensor::maxDistance)
        }

        private val xRange: IntRange by lazy {
            initialCoordinates.minOf(Coordinates::x) - maxDistanceOfSensors..initialCoordinates.maxOf(Coordinates::x) + maxDistanceOfSensors
        }

        private val yRange: IntRange by lazy {
            initialCoordinates.minOf(Coordinates::y) - maxDistanceOfSensors..initialCoordinates.maxOf(Coordinates::y) + maxDistanceOfSensors
        }

        fun drawMap() {
            val map = StringBuilder()
            val sensorsVisibleCoordinates = sensors.flatMap(Sensor::visibleRange)
            for (y in yRange) {
                map.append("$y".padStart(length = 3, padChar = ' ') + " ")
                for (x in xRange) {
                    val char = when (Coordinates(x, y)) {
                        in sensorsCoordinates -> 'S'
                        in beaconsCoordinates -> 'B'
                        in sensorsVisibleCoordinates -> '#'
                        else -> '.'
                    }
                    map.append(char)
                }
                map.appendLine()
            }
            println(map)
        }

        fun horizontalPositionsWithoutBeacon(
            yPos: Int,
            xRange: IntRange? = null,
        ): List<Coordinates> {
            val range = xRange ?: this.xRange
            return findCoordinatesWithoutBeacons(FixedCoordinate.Y(yPos), range) { coordinates ->
                sensors.any { sensor -> sensor.hasNoDistressBeacon(coordinates) }
            }
        }

        private sealed class FixedCoordinate(val value: Int) {
            class X(value: Int) : FixedCoordinate(value)
            class Y(value: Int) : FixedCoordinate(value)
        }

        private fun findCoordinatesWithoutBeacons(
            fixedCoordinate: FixedCoordinate,
            range: IntRange,
            predicate: (coordinates: Coordinates) -> Boolean,
        ): List<Coordinates> {
            return buildList {
                for (dynamicCoordinate in range) {
                    val coordinates = when (fixedCoordinate) {
                        is FixedCoordinate.X -> Coordinates(x = fixedCoordinate.value, y = dynamicCoordinate)
                        is FixedCoordinate.Y -> Coordinates(x = dynamicCoordinate, y = fixedCoordinate.value)
                    }
                    if (coordinates in beaconsCoordinates) {
                        continue
                    }

                    if (predicate(coordinates)) {
                        add(coordinates)
                    }
                }
            }
        }

        fun findDistressBeaconFast(maxCoordinateValue: Int): Coordinates {
            val coordinateRange = 0..maxCoordinateValue
            return sensors.flatMap { sensor ->
                // Look around the border of the sensor
                val possibleBeaconDistance = sensor.maxDistance + 1
                (-possibleBeaconDistance..possibleBeaconDistance).flatMap { dx ->
                    val dy = possibleBeaconDistance - dx.absoluteValue
                    val x = sensor.coordinates.x + dx
                    val sensorY = sensor.coordinates.y
                    listOf(Coordinates(x, sensorY - dy), Coordinates(x, sensorY + dy))
                        .filter { it.x in coordinateRange && it.y in coordinateRange }
                }
            }.first { coordinates -> sensors.none { sensor -> sensor.hasNoDistressBeacon(coordinates) } }
        }

        data class Sensor(
            val coordinates: Coordinates,
            val closestBeacon: Beacon,
            val maxDistance: Int,
        ) {
            fun visibleRange(): Set<Coordinates> {
                return generateCoordinatesByManhattanDistance(coordinates, maxDistance)
            }

            fun hasNoDistressBeacon(other: Coordinates): Boolean {
                return maxDistance > coordinates.manhattanDistance(other)
            }
        }

        data class Beacon(
            val coordinates: Coordinates,
        )
    }
}

fun Coordinates.manhattanDistance(other: Coordinates): Int {
    return (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}

fun generateCoordinatesByManhattanDistance(
    origin: Coordinates,
    distance: Int,
): Set<Coordinates> {
    return generateCoordinatesByManhattanDistance(
        setOf(origin),
        distance,
        1,
    )
}

private val movements = listOf(
    -1 to 0,
    1 to 0,
    0 to -1,
    0 to 1,
)

tailrec fun generateCoordinatesByManhattanDistance(
    coordinatesSet: Set<Coordinates>,
    distance: Int,
    currentDistance: Int,
): Set<Coordinates> {
    return if (currentDistance > distance) {
        coordinatesSet
    } else {
        generateCoordinatesByManhattanDistance(
            coordinatesSet = coordinatesSet + coordinatesSet.flatMap { coordinates ->
                movements.map { (dx, dy) ->
                    coordinates.move(dx = dx, dy = dy)
                }
            }.toSet(),
            distance = distance,
            currentDistance = currentDistance + 1,
        )
    }
}
