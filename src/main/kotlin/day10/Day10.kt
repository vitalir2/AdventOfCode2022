package day10

import Challenge

object Day10 : Challenge(10) {

    override fun part1(input: List<String>): Any {
        val neededSignalStrength = List(6) { 20 + it * 40 }
        val cycleToSignalStrength = mutableMapOf<Int, Int>()

        runDevice(input) { cycle, xRegister ->
            cycleToSignalStrength[cycle] = cycle * xRegister
        }

        return cycleToSignalStrength
            .filterKeys { cycle -> cycle in neededSignalStrength }
            .map(Map.Entry<Int,Int>::value)
            .sum()
    }

    override fun part2(input: List<String>): Any {
        val crtResult = mutableListOf<String>()
        val crtLine = StringBuilder()
        val crtLineLength = 40

        runDevice(input) { _, xRegister ->
            val drawnChar = if (crtLine.length in xRegister-1..xRegister+1) {
                '#'
            } else {
                '.'
            }
            crtLine.append(drawnChar)
            if (crtLine.length == crtLineLength) {
                crtResult.add(crtLine.toString())
                crtLine.clear()
            }
        }

        return crtResult.joinToString(separator = "\n")
    }

    private fun runDevice(input: List<String>, onCycleIncreased: (cycle: Int, xRegister: Int) -> Unit) {
        var cycle = 0
        var xRegister = 1

        fun increaseCycle() {
            cycle++
            onCycleIncreased(cycle, xRegister)
        }

        for (command in input) {
            when (command) {
                "noop" -> {
                    increaseCycle()
                }
                // addx V
                else -> {
                    val arg = command.split(" ")[1].toInt()
                    repeat(2) {
                        increaseCycle()
                    }
                    xRegister += arg
                }
            }
        }
    }
}
