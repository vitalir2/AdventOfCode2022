package day05

import Challenge
import java.util.*
import map

object Day05 : Challenge {
    override val day: Int = 5

    override fun part1(input: List<String>): String {
        val (stacks, commands) = parse(input)
        for (command in commands) {
            val fromStack = stacks[command.fromStack]
            val toStack = stacks[command.toStack]
            repeat(command.times) {
                val item = fromStack.pop()
                toStack.push(item)
            }
        }
        return stacks.map { it.peek() }.joinToString("")
    }

    override fun part2(input: List<String>): String {
        TODO()
    }

    private fun parse(input: List<String>): ParsingResult {
        val stacksAndCommandSeparatorIndex = input.indexOfFirst { it.isEmpty() }
        val initStackInput = input.subList(0, stacksAndCommandSeparatorIndex-1)
        val commandInput = input.subList(stacksAndCommandSeparatorIndex + 1, input.size)

        val stacks: MutableList<Stack<Char>> = mutableListOf()
        val lastStackNumber = input[stacksAndCommandSeparatorIndex-1].trimEnd().last().digitToInt()
        repeat(lastStackNumber) {
            stacks.add(Stack())
        }
        initStacksValues(initStackInput, stacks)

        val commands = parseCommands(commandInput)
        return ParsingResult(stacks, commands)
    }

    private fun initStacksValues(input: List<String>, stacks: List<Stack<Char>>) {
        input
            .reversed() // start pushing from bottom
            .map { line -> line
                .chunked(4) // '[A] ', '[D] ', '    ', ..
                .withIndex() // ('[A] ', 0), ('[D] ', 1), ('    ', 2), ..
                .map { rawPossibleItemWithIndex -> // ('A', 0), ('D', 1), (null, 2), ..
                    rawPossibleItemWithIndex.map { value -> value.trim().getOrNull(1) }
                }
                .filter { it.value != null } // ('A', 0), ('D', 1), ..
            }
            .forEach { itemsOfStack -> itemsOfStack.forEach { (index, item) -> stacks[index].push(item) } }
    }

    private fun parseCommands(input: List<String>): List<ParsingResult.Command> {
        return buildList {
            for (line in input) {
                val (times, from, to) = line
                    .split(" ")
                    .filter { it.first().isDigit() }
                    .map { it.toInt() }
                val command = ParsingResult.Command(
                    times = times,
                    fromStack = from - 1,
                    toStack = to - 1
                )
                add(command)
            }
        }
    }

    private data class ParsingResult(
        val stacks: List<Stack<Char>>,
        val commands: List<Command>,
    ) {

        data class Command(
            val times: Int,
            val fromStack: Int,
            val toStack: Int,
        )
    }
}
