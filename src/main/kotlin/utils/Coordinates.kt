package utils

data class Coordinates(
    val x: Int,
    val y: Int,
) {

    fun moveLeft(): Coordinates = copy(x = x - 1, y = y)

    fun moveRight(): Coordinates = copy(x = x + 1, y = y)

    fun moveUp(): Coordinates = copy(x = x, y = y - 1)

    fun moveDown(): Coordinates = copy(x = x, y = y + 1)

    /**
     * ----------------
     * \  1   2   3 \
     * \  4  [5]  6 \
     * \  7   8   9 \
     * ----------------
     */
    infix fun adjacentTo(other: Coordinates): Boolean {
        return adjacentOnVertical(other) || adjacentOnHorizontal(other) || adjacentOnDiagonal(other)
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
