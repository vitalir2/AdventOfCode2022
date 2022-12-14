package day13

import Challenge
import product

object Day13 : Challenge(13) {
    override fun part1(input: List<String>): Int {
        val packetPairs = parseInput(input)

        val inRightOrderIndices = mutableSetOf<Int>()
        packetPairs.forEachIndexed { index, pair -> if (isPairInTheRightOrder(pair)) inRightOrderIndices.add(index+1) }
        return inRightOrderIndices.sum()
    }

    override fun part2(input: List<String>): Any {
        val dividerPackets = listOf(
            PacketData(PacketData.Value.List(PacketData.Value.List(PacketData.Value.Number(2)))),
            PacketData(PacketData.Value.List(PacketData.Value.List(PacketData.Value.Number(6)))),
        )
        val sortedPackets = input
            .filter(String::isNotBlank)
            .map(::parsePacketData)
            .toMutableList().apply { addAll(dividerPackets) }
            .toList()
            .sortedWith { left, right -> isValuesInRightOrder(left.value, right.value) }
            .reversed()

        return sortedPackets
            .mapIndexed { index, packetData -> index + 1 to packetData }
            .toMap()
            .filterValues(dividerPackets::contains)
            .map(Map.Entry<Int, PacketData>::key)
            .product()
    }

    private fun parseInput(input: List<String>): List<Pair<PacketData, PacketData>> {
        return input
            .filter(String::isNotBlank)
            .chunked(2)
            .map(::parsePacketPair)
    }

    private fun parsePacketPair(input: List<String>): Pair<PacketData, PacketData> {
        return input
            .map(::parsePacketData)
            .let { (first, second) -> first to second }
    }

    private fun parsePacketData(input: String): PacketData {
        return PacketData(parsePacketValue(input, mutableListOf()) as PacketData.Value.List)
    }

    /**
     * [1,2,3] -> PacketData(List(Number(1),Number(2),Number(3))
     *
     * [1,[1],2] -> PacketData(List(Number(1),List(Number(1)),Number(2)
     *
     * [] -> PacketData(List())
     *
     * [1] -> PacketData(List(Number(1))
     *
     * [[[9]]] -> PacketData(List(List(List(Number(9))))
     *
     * [1,2,3,4,[5,6,[7,8,9]]]
     * -> List(Number(1),Number(2),Number(3),Number(4),List(Number(5),Number(6),List(Number(7),Number(8),Number(9)))
     *
     */
    private fun parsePacketValue(
        input: String,
        parentList: MutableList<PacketData.Value>,
    ): PacketData.Value {
        val input = input.removeSurrounding(prefix = "[", suffix = "]")
        val list = mutableListOf<PacketData.Value>()

        val listStartIndices = ArrayDeque<Int>()
        var currentIndex = 0
        while (currentIndex != input.length) {
            when (input[currentIndex]) {
                '[' -> {
                    listStartIndices.addFirst(currentIndex)
                }
                ']' -> {
                    val listStartIndex = listStartIndices.removeFirst()
                    if (listStartIndices.isEmpty()) {
                        parsePacketValue(input.substring(listStartIndex..currentIndex), list)
                    }
                }
                ',' -> {
                    // Ignore
                }
                in '0'..'9' -> {
                    if (listStartIndices.isEmpty()) {
                        val numberString = StringBuilder()
                        while (input.getOrNull(currentIndex) != null && input[currentIndex].isDigit()) {
                            numberString.append(input[currentIndex])
                            currentIndex++
                        }
                        currentIndex--
                        list.add(PacketData.Value.Number(numberString.toString().toInt()))
                    }
                }
                else -> error("Invalid char")
            }
            currentIndex++
        }
        val result = PacketData.Value.List(list)
        parentList.add(result)
        return result
    }

    private fun isPairInTheRightOrder(pair: Pair<PacketData, PacketData>): Boolean {
        val (first, second) = pair.toList().map(PacketData::value)
        return isValuesInRightOrder(first, second).toBooleanOrNull()!!
    }

    private fun isValuesInRightOrder(left: PacketData.Value, right: PacketData.Value): Int {
        return when {
            left is PacketData.Value.Number && right is PacketData.Value.Number ->
                right.value.compareTo(left.value)
            left is PacketData.Value.List && right is PacketData.Value.List -> {
                val values = left.value.zip(right.value)
                var isInRightOrder = 0
                for (value in values) {
                    val result = isValuesInRightOrder(value.first, value.second)
                    if (result != 0) {
                        isInRightOrder = result
                        break
                    }
                }
                if (isInRightOrder == 0) right.value.size.compareTo(left.value.size) else isInRightOrder
            }
            left is PacketData.Value.Number && right is PacketData.Value.List -> {
                isValuesInRightOrder(PacketData.Value.List(listOf(left)), right)
            }
            left is PacketData.Value.List && right is PacketData.Value.Number -> {
                isValuesInRightOrder(left, PacketData.Value.List(listOf(right)))
            }
            else -> error("Impossible case")
        }
    }

    private fun Int.toBooleanOrNull(): Boolean? = when {
        this > 0 -> true
        this < 0 -> false
        else -> null
    }

    private data class PacketData(
        val value: Value.List,
    ) {
        sealed interface Value {
            data class Number(val value: Int) : Value {
                override fun toString(): String {
                    return "$value"
                }
            }
            data class List(val value: kotlin.collections.List<Value>) : Value {

                constructor(single: Value) : this(listOf(single))
                override fun toString(): String {
                    return "[${value.joinToString(",")}]"
                }
            }
        }
    }
}
