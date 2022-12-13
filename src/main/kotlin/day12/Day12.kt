package day12

import Challenge
import java.util.PriorityQueue
import utils.Coordinates

private data class Heightmap(
    val startPoint: Point,
    val endPoint: Point,
    val points: List<Point>,
) {

    fun printPath(path: List<Point>) {
        val maxX = points.maxOf { it.coordinates.x }
        val maxY = points.maxOf { it.coordinates.y }
        data class DrawPoint(val coordinates: Coordinates, val symbol: Char)
        val drawingSet = mutableSetOf<DrawPoint>()
        var first = true
        for (points in path.windowed(size = 2, partialWindows = true)) {
            val point = points.first()
            val nextPoint = points.getOrNull(1)
            val drawnChar = when {
                first -> 'S'.also { first = false }
                point.coordinates == endPoint.coordinates -> 'E'
                nextPoint == null -> 'e'
                point.coordinates.moveDown() == nextPoint.coordinates -> '↓'
                point.coordinates.moveUp() == nextPoint.coordinates -> '↑'
                point.coordinates.moveLeft() == nextPoint.coordinates -> '←'
                point.coordinates.moveRight() == nextPoint.coordinates -> '→'
                else -> '.'
            }
            drawingSet.add(DrawPoint(point.coordinates, drawnChar))
        }

        val result = StringBuilder()
        for (j in 0.. maxY) {
            for (i in 0.. maxX) {
                val coordinates = Coordinates(i, j)
                val char = drawingSet.firstOrNull { it.coordinates == coordinates }?.symbol ?: '.'
                result.append(char)
            }
            result.appendLine()
        }
        println(result)
    }

    fun reachablePoints(from: Point): List<Point> {
        return points.filter { from.coordinates adjacentCross it.coordinates && it.height - from.height <= 1 }
    }

    data class Point(
        val coordinates: Coordinates,
        val height: Char,
    )
}

object Day12 : Challenge(12) {

    // Dijkstra's algo
    override fun part1(input: List<String>): Any {
        val heightmap = parseInput(input)
        val visitedPoints = mutableSetOf<Heightmap.Point>()
        val pointToDistance = mutableMapOf<Heightmap.Point, Int>()
        val pointToPrev = mutableMapOf<Heightmap.Point, Heightmap.Point>()
        val queue = PriorityQueue<Heightmap.Point> { first, second ->
            val firstDistance = pointToDistance[first]
            val secondDistance = pointToDistance[second]
            when {
                firstDistance == null && secondDistance == null -> 0
                firstDistance == null -> -1
                secondDistance == null -> 1
                else -> firstDistance.compareTo(secondDistance)
            }
        }

        pointToDistance[heightmap.startPoint] = 0
        queue.offer(heightmap.startPoint)

        while (queue.isNotEmpty()) {
            val point = queue.poll()
            val distance = pointToDistance[point]!!
            val reachablePoints = heightmap.reachablePoints(from = point)
            for (reachablePoint in reachablePoints) {
                if (reachablePoint !in visitedPoints && reachablePoint !in queue) {
                    val distanceToReachablePoint = pointToDistance[reachablePoint]
                    if (distanceToReachablePoint == null || distance + 1 < distanceToReachablePoint) {
                        pointToDistance[reachablePoint] = distance + 1
                        pointToPrev[reachablePoint] = point
                    }
                    queue.offer(reachablePoint)
                }
            }
            visitedPoints.add(point)
        }

        val finalPath = buildList {
            var currentPoint: Heightmap.Point? = heightmap.endPoint
            while (currentPoint != null) {
                add(currentPoint)
                currentPoint = pointToPrev[currentPoint]
            }
        }
        heightmap.printPath(finalPath.reversed())
        return finalPath.size - 1
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
    }

    private fun parseInput(input: List<String>): Heightmap {
        lateinit var startPoint: Heightmap.Point
        lateinit var endPoint: Heightmap.Point
        val points = buildList {
            input.forEachIndexed { rowIndex, line ->
                line.forEachIndexed { colIndex, char ->
                    val coordinates = Coordinates(x = colIndex, y = rowIndex)
                    val point = when (char) {
                        'S' -> Heightmap.Point(coordinates, height = 'a').also { startPoint = it }
                        'E' -> Heightmap.Point(coordinates, height = 'z').also { endPoint = it }
                        else -> Heightmap.Point(coordinates, height = char)
                    }
                    add(point)
                }
            }
        }
        return Heightmap(
            startPoint = startPoint,
            endPoint = endPoint,
            points = points,
        )
    }
}
