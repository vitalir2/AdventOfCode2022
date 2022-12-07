package day07

import Challenge
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

object Day07 : Challenge {
    override val day: Int = 7

    private var root = Node.Folder(null, "/")
    private lateinit var currentFolder: Node.Folder

    override fun part1(input: List<String>): Any {
        resetLastFileSystem()
        input.forEach(::parseLine)
        return root.allSizesOfFolders
            .filter { it <= PART1_DIR_SIZE_THRESHOLD }
            .sum()
    }

    override fun part2(input: List<String>): Any {
        TODO()
    }

    private fun resetLastFileSystem() {
        root = Node.Folder(null, "/")
        currentFolder = root
    }

    private fun parseLine(line: String) {
        when {
            line.startsWith("$") -> handleCommand(line)
            else -> handleListDirectoriesResult(line)
        }
    }

    private fun handleCommand(command: String) {
        val splitLine = command.split(" ")
        when (splitLine[1]) {
            "cd" -> {
                val argument = splitLine[2]
                currentFolder = when (argument) {
                    "/" -> {
                        root
                    }

                    ".." -> {
                        currentFolder.parent!!
                    }

                    else -> {
                        currentFolder.nodes
                            .filterIsInstance<Node.Folder>()
                            .first { it.name == argument }
                    }
                }
            }

            "ls" -> {
                // Ignore, start reading from the next step
            }
        }
    }

    private fun handleListDirectoriesResult(result: String) {
        val (arg, name) = result.split(" ")
        val newNode = if (arg == "dir") {
            Node.Folder(currentFolder, name)
        } else {
            Node.File(currentFolder, arg.toLong(), name)
        }
        currentFolder.nodes.add(newNode)
    }

    private const val PART1_DIR_SIZE_THRESHOLD = 100_000
}

private sealed interface Node {
    val size: Long
    val parent: Folder?

    data class File(
        override val parent: Folder,
        override val size: Long,
        val name: String,
    ) : Node
    data class Folder(
        override val parent: Folder?,
        val name: String,
        val nodes: MutableList<Node> = mutableListOf(),
    ) : Node {
        override val size: Long
            get() = nodes.sumOf(Node::size)
    }
}

@OptIn(ExperimentalContracts::class)
private fun Node.isFolder(): Boolean {
    contract {
        returns() implies (this@isFolder is Node.Folder)
    }

    return this is Node.Folder
}

private val Node.allSizesOfFolders: Sequence<Long>
    get() = sequence {
        if (isFolder()) {
            yield(size)
            for (node in nodes) {
                yieldAll(node.allSizesOfFolders)
            }
        }
    }
