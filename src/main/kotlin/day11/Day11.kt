package day11

import Challenge
import product

private typealias MonkeyId = Int

object Day11 : Challenge(11) {

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
        val monkeys = input
            .map(String::trim)
            .chunked(7) // length of one monkey input + line break
            .map(::createMonkeyFromInput)

        // we only need to know if item is divisible by some number,
        // so we can keep them in the ring of integers modulo "product of the numbers which we need divide by"
        val modulus = monkeys
            .map(Monkey::divisibilityNumber)
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
            ifTrue = ifDivisible,
            ifFalse = ifNotDivisible,
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
        monkeys.forEach { it.inspectItems(monkeys, modulus, transformLevel, onItemInspected) }
    }

    private data class ThrownItem(
        val worryLevel: Int,
        val toMonkey: MonkeyId,
    )

    private class Monkey(
        private val id: MonkeyId,
        private val items: MutableList<Int>,
        private val changeWorryLevel: Expression,
        private val divisibilityTest: DivisibilityTest,
    ) {

        val divisibilityNumber: Long
            get() = divisibilityTest.number.toLong()

        fun inspectItems(
            monkeys: List<Monkey>,
            modulus: Long,
            transformLevel: (worryLevel: Int) -> Int,
            onItemInspected: (monkeyId: MonkeyId) -> Unit,
        ) {
            val thrownItems = mutableListOf<ThrownItem>()
            for (item in items) {
                onItemInspected(id)
                val newWorryLevel = transformLevel((changeWorryLevel.execute(item) % modulus).toInt())
                val newMonkeyOwner = divisibilityTest.test(newWorryLevel)
                thrownItems += ThrownItem(
                    worryLevel = newWorryLevel,
                    toMonkey = newMonkeyOwner,
                )
            }
            items.clear()
            for (thrownItem in thrownItems) {
                monkeys[thrownItem.toMonkey].items += thrownItem.worryLevel
            }
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
                val leftValue = left.value(oldValue).toLong()
                val rightValue = right.value(oldValue).toLong()
                return when (operation) {
                    Operation.ADDITION -> leftValue + rightValue
                    Operation.MULTIPLICATION -> leftValue * rightValue
                }
            }

            companion object {
                private fun Operand.value(oldExpressionValue: Int): Int {
                    return when (this) {
                        is Operand.Number -> number
                        is Operand.OldExpressionValue -> oldExpressionValue
                    }
                }
            }
        }
        class DivisibilityTest(
            val number: Int,
            private val ifTrue: MonkeyId,
            private val ifFalse: MonkeyId,
        ) {

            fun test(worryLevel: Int): MonkeyId {
                return if (worryLevel % number == 0) ifTrue else ifFalse
            }
        }
    }
}
