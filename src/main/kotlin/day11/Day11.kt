package day11

import Challenge
import product

private typealias MonkeyId = Int

object Day11 : Challenge {
    override val day: Int = 11

    override fun part1(input: List<String>): Any {
        return solveDay(input, 20) { level -> level / 3 }
    }

    override fun part2(input: List<String>): Any {
        return solveDay(input, 10000)
    }

    private fun solveDay(
        input: List<String>,
        rounds: Int,
        onNewLevelCalculated: (worryLevel: Int) -> Int = { it },
    ): Long {
        val monkeys = buildList {
            input
                .map(String::trim)
                .chunked(7) // length of one monkey input + line break
                .map(Day11::createMonkeyFromInput)
                .forEach(::add)
        }

        // we only need to know if item is divisible by some number
        // so we can keep them in the ring of integers modulo "product of the numbers which we need divide by"
        val modulus = monkeys
            .map { it.divisibilityNumber.toLong() }
            .product()

        val inspectItemsCount: MutableMap<MonkeyId, Long> = mutableMapOf()
        repeat(rounds) {
            executeRound(monkeys, modulus, onNewLevelCalculated) { monkeyId ->
                val lastValue = inspectItemsCount[monkeyId] ?: 0
                inspectItemsCount[monkeyId] = lastValue + 1
            }
        }

        return inspectItemsCount
            .values
            .sortedDescending()
            .take(2)
            .product()
    }

    private fun createMonkeyFromInput(input: List<String>): Monkey {
        val monkeyId = input[0]
            .removePrefix("Monkey ")
            .takeWhile(Char::isDigit)
            .toInt()

        val initItems = input[1]
            .removePrefix("Starting items: ")
            .split(", ")
            .map(String::toInt)

        val changeWorryLevel = input[2]
            .removePrefix("Operation: new = ")
            .let { operation ->
                val (left, op, right) = operation.split(" ")
                Monkey.Expression(
                    left = left.toOperand(),
                    operation = op.toOperation(),
                    right = right.toOperand(),
                )
            }

        val divisibleBy = input[3]
            .removePrefix("Test: divisible by ")
            .toInt()

        val ifDivisible = input[4]
            .removePrefix("If true: throw to monkey ")
            .toInt()

        val ifNotDivisible = input[5]
            .removePrefix("If false: throw to monkey ")
            .toInt()

        val divisibilityTest = Monkey.DivisibilityTest(
            number = divisibleBy,
            throwToIfPassed = ifDivisible,
            throwToIfFailed = ifNotDivisible,
        )

        return Monkey(
            id = monkeyId,
            items = initItems.toMutableList(),
            changeWorryLevel = changeWorryLevel,
            divisibilityTest = divisibilityTest,
        )
    }

    private fun String.toOperand(): Monkey.Expression.Operand {
        return when (this) {
            "old" -> Monkey.Expression.Operand.OldExpressionValue
            else -> Monkey.Expression.Operand.Number(this.toInt())
        }
    }

    private fun String.toOperation(): Monkey.Expression.Operation {
        return when (this) {
            "*" -> Monkey.Expression.Operation.MULTIPLICATION
            "+" -> Monkey.Expression.Operation.ADDITION
            else -> error("Invalid operation")
        }
    }

    private fun executeRound(
        monkeys: List<Monkey>,
        modulus: Long,
        transformLevel: (worryLevel: Int) -> Int,
        onItemInspected: (monkeyId: MonkeyId) -> Unit,
    ) {
        for (monkey in monkeys) {
            val thrownItems = mutableListOf<ThrownItem>()
            for (item in monkey.items) {
                onItemInspected(monkey.id)
                val newWorryLevel = transformLevel((monkey.changeWorryLevel(item) % modulus).toInt())
                val newMonkeyOwner = monkey.test(newWorryLevel)
                thrownItems += ThrownItem(
                    worryLevel = newWorryLevel,
                    toMonkey = newMonkeyOwner,
                )
            }
            monkey.items.clear()
            for (thrownItem in thrownItems) {
                monkeys[thrownItem.toMonkey].items += thrownItem.worryLevel
            }
        }
    }

    private data class ThrownItem(
        val worryLevel: Int,
        val toMonkey: Int,
    )

    private class Monkey(
        val id: MonkeyId,
        val items: MutableList<Int>,
        private val changeWorryLevel: Expression,
        private val divisibilityTest: DivisibilityTest,
    ) {

        val divisibilityNumber: Int
            get() = divisibilityTest.number

        fun changeWorryLevel(item: Int): Long {
            return changeWorryLevel.execute(item)
        }

        fun test(worryLevel: Int): Int {
            return divisibilityTest.test(worryLevel)
        }

        class Expression(
            private val left: Operand,
            private val operation: Operation,
            private val right: Operand,
        ) {
            sealed interface Operand {
                @JvmInline
                value class Number(val number: Int) : Operand
                object OldExpressionValue : Operand
            }

            enum class Operation {
                ADDITION,
                MULTIPLICATION,
            }

            fun execute(oldValue: Int): Long {
                val leftValue = when (left) {
                    is Operand.Number -> left.number
                    is Operand.OldExpressionValue -> oldValue
                }.toLong()
                val rightValue = when (right) {
                    is Operand.Number -> right.number
                    is Operand.OldExpressionValue -> oldValue
                }.toLong()
                return when (operation) {
                    Operation.ADDITION -> leftValue + rightValue
                    Operation.MULTIPLICATION -> leftValue * rightValue
                }
            }
        }
        class DivisibilityTest(
            val number: Int,
            private val throwToIfPassed: Int,
            private val throwToIfFailed: Int,
        ) {

            fun test(worryLevel: Int): Int {
                return if (worryLevel % number == 0) {
                    throwToIfPassed
                } else {
                    throwToIfFailed
                }
            }
        }
    }
}
