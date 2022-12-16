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
            .positionsWithoutBeacon(yPos = y)
            .size
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
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
                BeaconZone.Sensor(sensorCoordinates) to BeaconZone.Beacon(beaconCoordinates)
            }
            .map { (sensor, closestBeacon) -> BeaconZone.SensorWithClosestBeacon(sensor, closestBeacon) }
            .let { sensorsWithBeacons ->
                BeaconZone(
                    sensorsWithClosestBeacon = sensorsWithBeacons,
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
        val sensorsWithClosestBeacon: List<SensorWithClosestBeacon>,
        val isDebug: Boolean,
    ) {

        private val noBeaconCoordinates: MutableSet<Coordinates> = mutableSetOf()

        private val beaconsCoordinates: Set<Coordinates> by lazy {
            sensorsWithClosestBeacon.map { (_, beacon) -> beacon.coordinates }.toSet()
        }

        private val sensorsCoordinates: Set<Coordinates> by lazy {
            sensorsWithClosestBeacon.map { (sensor, _) -> sensor.coordinates }.toSet()
        }

        private val initialCoordinates: Set<Coordinates> by lazy {
            beaconsCoordinates + sensorsCoordinates
        }

        private val maxRadius: Int by lazy {
            sensorsWithClosestBeacon.maxOf(SensorWithClosestBeacon::radius)
        }

        private val xRange: IntRange by lazy {
            initialCoordinates.minOf(Coordinates::x) - maxRadius..initialCoordinates.maxOf(Coordinates::x) + maxRadius
        }

        private val yRange: IntRange by lazy {
            initialCoordinates.minOf(Coordinates::y)..initialCoordinates.maxOf(Coordinates::y)
        }

        fun emulate() {
            for (sensorWithBeacon in sensorsWithClosestBeacon) {
                val sensorCoordinates = sensorWithBeacon.noBeaconCoordinates
                noBeaconCoordinates.addAll(sensorCoordinates.filterNot(beaconsCoordinates::contains))
                if (isDebug) {
                    drawSensorClosestCoordinatesMap(sensorWithBeacon, sensorCoordinates)
                }
            }
        }

        fun positionsWithoutBeacon(yPos: Int): List<Coordinates> {
            return buildList {
                for (x in xRange) {
                    val coordinates = Coordinates(x = x, y = yPos)
                    if (coordinates in beaconsCoordinates) {
                        continue
                    }

                    val hasNoBeaconFromAnySensor = sensorsWithClosestBeacon.any { it.hasNoBeacon(coordinates) }
                    if (hasNoBeaconFromAnySensor) {
                        add(coordinates)
                    }
                }

                if (isDebug) {
                    drawHorizontalLineWithItsNeighbors(yPos, this.toSet())
                }
            }
        }

        fun positionsWithoutBeaconEmulated(yPos: Int): List<Coordinates> {
            val result = noBeaconCoordinates.filter { it.y == yPos }

            if (isDebug) {
                drawHorizontalLineWithItsNeighbors(yPos)
            }

            return result
        }

        /**
         * Draw map of the zone with sensor visible coordinates.
         *
         * For debug purposes.
         */
        private fun drawSensorClosestCoordinatesMap(
            sensorWithClosestBeacon: SensorWithClosestBeacon,
            sensorVisibleCoordinates: Set<Coordinates>,
        ) {
            val map = StringBuilder()
            for (y in yRange) {
                map.append("$y".padStart(length = 3, padChar = ' ') + " ")
                for (x in xRange) {
                    val char = when (Coordinates(x, y)) {
                        sensorWithClosestBeacon.sensor.coordinates -> 'S'
                        sensorWithClosestBeacon.beacon.coordinates -> 'B'
                        in sensorVisibleCoordinates -> '#'
                        else -> '.'
                    }
                    map.append(char)
                }
                map.appendLine()
            }
            println(map)
        }

        private fun drawHorizontalLineWithItsNeighbors(
            yPos: Int,
            initNoBeaconCoordinates: Set<Coordinates>? = null,
        ) {
            val noBeaconCoordinates = initNoBeaconCoordinates ?: this.noBeaconCoordinates
            val map = StringBuilder()
            for (y in yPos - 1..yPos + 1) {
                map.append("$y".padStart(length = 3) + " ")
                for (x in xRange) {
                    val char = when (Coordinates(x, y)) {
                        in sensorsCoordinates -> 'S'
                        in beaconsCoordinates -> 'B'
                        in noBeaconCoordinates -> '#'
                        else -> '.'
                    }
                    map.append(char)
                }
                map.appendLine()
            }
            println(map)
        }

        data class SensorWithClosestBeacon(
            val sensor: Sensor,
            val beacon: Beacon,
        ) {
            val radius: Int
                get() = sensor.coordinates.manhattanDistance(beacon.coordinates)

            val noBeaconCoordinates: Set<Coordinates>
                get() = sensor.visibleRange(radius)

            fun hasNoBeacon(other: Coordinates): Boolean {
                return radius >= sensor.coordinates.manhattanDistance(other)
            }
        }

        data class Sensor(
            val coordinates: Coordinates,
        ) {
            fun visibleRange(radius: Int): Set<Coordinates> {
                require(radius > 0) { "Radius cannot be negative or zero" }
                return generateCoordinatesByManhattanDistance(coordinates, radius)
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
