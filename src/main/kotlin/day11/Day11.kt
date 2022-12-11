package day11

import Challenge

object Day11 : Challenge {
    override val day: Int = 11

    override fun part1(input: List<String>): Any {
        val monkeys = buildList {
            input
                .map(String::trim)
                .chunked(7) // length of one monkey input + line break
                .map(::createMonkeyFromInput)
                .forEach(::add)
        }

        val inspectItemsCount: MutableMap<Int, Long> = mutableMapOf()
        repeat(20) {
            executeRound(monkeys) { monkeyNumber ->
                val lastValue = inspectItemsCount[monkeyNumber] ?: 0
                inspectItemsCount[monkeyNumber] = lastValue + 1
            }
        }

        return inspectItemsCount
            .values
            .sortedDescending()
            .take(2)
            .reduce { acc, i -> acc * i }
    }

    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
    }

    private fun createMonkeyFromInput(input: List<String>): Monkey {
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
        onItemInspected: (monkeyNumber: Int) -> Unit,
    ) {
        for ((monkeyNumber, monkey) in monkeys.withIndex()) {
            val thrownItems = mutableListOf<ThrownItem>()
            for (item in monkey.items) {
                onItemInspected(monkeyNumber)
                val newWorryLevel = monkey.changeWorryLevel.execute(item) / 3
                val newMonkeyOwner = monkey.divisibilityTest.test(newWorryLevel)
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

    private data class Monkey(
        val items: MutableList<Int>,
        val changeWorryLevel: Expression,
        val divisibilityTest: DivisibilityTest,
    ) {

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

            fun execute(oldValue: Int): Int {
                val leftValue = when (left) {
                    is Operand.Number -> left.number
                    is Operand.OldExpressionValue -> oldValue
                }
                val rightValue = when (right) {
                    is Operand.Number -> right.number
                    is Operand.OldExpressionValue -> oldValue
                }
                return when (operation) {
                    Operation.ADDITION -> leftValue + rightValue
                    Operation.MULTIPLICATION -> leftValue * rightValue
                }
            }
        }
        class DivisibilityTest(
            private val number: Int,
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
