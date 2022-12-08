package day08

import Challenge

private typealias Forest = List<List<Int>>

object Day08 : Challenge {
    override val day: Int = 8

    override fun part1(input: List<String>): Any {
        val forest = createForest(input)
        val visibleTrees = mutableListOf<Int>()
        for ((rowIndex, row) in forest.withIndex()) {
            for ((columnIndex, tree) in row.withIndex()) {
                if (forest.isOnEdge(rowIndex, columnIndex)) {
                    visibleTrees.add(tree)
                    continue
                }

                val isVisibleFrom = mutableListOf<Direction>()
                for (direction in Direction.values()) {
                    forest.traverseInDirection(rowIndex, columnIndex, direction) { otherTree ->
                        if (otherTree >= tree) {
                            isVisibleFrom.add(direction)
                            return@traverseInDirection
                        }
                    }
                }
                if (isVisibleFrom.isNotEmpty()) {
                    visibleTrees.add(tree)
                }
            }
        }

        return visibleTrees.count()
    }

    override fun part2(input: List<String>): Any {
        TODO()
    }

    private fun createForest(input: List<String>): Forest {
        return buildList {
            for (treeRow in input) {
                val treeList = buildList {
                    for (tree in treeRow) {
                        add(tree.digitToInt())
                    }
                }
                add(treeList)
            }
        }
    }

    private fun Forest.traverseInDirection(
        xStart: Int,
        yStart: Int,
        direction: Direction,
        action: (currentElement: Int) -> Unit,
        ) {
        when (direction) {
            Direction.LEFT -> {
                var rowIndex = xStart - 1
                while (rowIndex >= 0) {
                    action(this[rowIndex][yStart])
                    rowIndex--
                }
            }
            Direction.RIGHT -> {
                var rowIndex = xStart + 1
                while (rowIndex <= this.lastIndex) {
                    action(this[rowIndex][yStart])
                    rowIndex++
                }
            }
            Direction.TOP -> {
                var colIndex = yStart - 1
                while (colIndex >= 0) {
                    action(this[xStart][colIndex])
                    colIndex--
                }
            }
            Direction.BOTTOM -> {
                var colIndex = yStart + 1
                while (colIndex <= this.first().lastIndex) {
                    action(this[xStart][colIndex])
                    colIndex++
                }
            }
        }
    }

    private fun Forest.isOnEdge(x: Int, y: Int): Boolean {
        return x == 0 || x == lastIndex || y == 0 || y == firstOrNull()?.lastIndex
    }

    private enum class Direction {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    }
}
