package utils

data class Coordinates(
    val x: Int,
    val y: Int,
) {

    fun move(dx: Int = 0, dy: Int = 0): Coordinates {
        require(dx != 0 || dy != 0) { "No dx and dy were passed" }
        return copy(x = x + dx, y = y + dy)
    }

    fun moveLeft(): Coordinates = move(dx = -1)

    fun moveRight(): Coordinates = move(dx = 1)

    fun moveUp(): Coordinates = move(dy = -1)

    fun moveDown(): Coordinates = move(dy = 1)

    fun createLine(end: Coordinates): List<Coordinates> {
        val xDiff = this.x - end.x
        val yDiff = this.y - end.y
        require(xDiff != 0 || yDiff != 0) { "Cannot create line between the same coordinates" }

        var current = this
        var currentXDiff = xDiff
        var currentYDiff = yDiff
        return buildList {
            val dx = if (xDiff < 0) 1 else -1
            val dy = if (yDiff < 0) 1 else -1
            while (currentXDiff != 0 && currentYDiff != 0) {
                add(current)
                current = current.move(dx = dx, dy = dy)
                currentXDiff += dx
                currentYDiff += dy
            }
            while (currentXDiff != 0) {
                add(current)
                current = current.move(dx = dx)
                currentXDiff += dx
            }
            while (currentYDiff != 0) {
                add(current)
                current = current.move(dy = dy)
                currentYDiff += dy
            }
            // add the last coordinates
            add(current)
        }
    }

    infix fun adjacentTo(other: Coordinates): Boolean {
        return adjacentOnVertical(other) || adjacentOnHorizontal(other) || adjacentOnDiagonal(other)
    }

    infix fun adjacentCross(other: Coordinates): Boolean {
        return adjacentOnVertical(other) || adjacentOnHorizontal(other)
    }

    private fun adjacentOnVertical(other: Coordinates): Boolean {
        return (x == other.x && y == other.y - 1) || (x == other.x && y == other.y + 1)
    }

    private fun adjacentOnHorizontal(other: Coordinates): Boolean {
        return (y == other.y && x == other.x - 1) || (y == other.y && x == other.x + 1)
    }


    private fun adjacentOnDiagonal(other: Coordinates): Boolean {
        return (x == other.x + 1 && y == other.y + 1)
                || (x == other.x + 1 && y == other.y - 1)
                || (x == other.x - 1 && y == other.y + 1)
                || (x == other.x - 1 && y == other.y - 1)
    }

    operator fun minus(other: Coordinates): Coordinates {
        return Coordinates(
            x = x - other.x,
            y = y - other.y,
        )
    }
}
