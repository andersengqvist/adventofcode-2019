import lib.IntCodeComputer
import lib.permuteMap
import lib.resourceLines
import kotlin.test.assertEquals

private fun runTests() {
    assertEquals(
            43210,
            amplifierController(
                    listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0),
                    listOf(4,3,2,1,0)
            )
    )
    assertEquals(
            54321,
            amplifierController(
                    listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23, 101,5,23,23,1,24,23,23,4,23,99,0,0),
                    listOf(0,1,2,3,4)
            )
    )
    assertEquals(
            65210,
            amplifierController(
                    listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33, 1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0),
                    listOf(1,0,4,3,2)
            )
    )
    assertEquals(
            139629729,
            amplifierController(
                    listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26, 27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5),
                    listOf(9,8,7,6,5)
            )
    )
    assertEquals(
            18216,
            amplifierController(
                    listOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54, -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4, 53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10),
                    listOf(9,7,8,5,6)
            )
    )
}

private fun amplifierController(program: List<Int>, settings: List<Int>): Int {
    val signal = 0

    val computers = mutableListOf<IntCodeComputer>()
    for (setting in settings) {
        val computer = IntCodeComputer(program)
        computer.addInput(setting)
        computers.add(computer)
    }

    val signals = mutableListOf<Int>()
    signals.add(signal)

    loop@ while (true) {
        for (computer in computers) {
            if (computer.isHalted()) {
                break@loop
            }
            else {
                computer.addInputs(signals)
                signals.clear()
                computer.runProgram()
                signals.addAll(computer.outputs())
            }
        }
    }
    if (signals.size != 1) {
        throw ArrayIndexOutOfBoundsException("Size of output should be 1, was ${signals.size}")
    }
    return signals[0]
}

private fun tryPermutations(program: List<Int>, psl: Int, psh: Int): Int {
    return (psl..psh)
            .toList()
            .permuteMap {
                amplifierController(program, it)
            }
            .toIntArray()
            .max() ?: 0
}

private fun part1(program: List<Int>) {
    val result = tryPermutations(program, 0, 4)

    println("1: The highest signal that can be sent to the thrusters: $result")
}

private fun part2(program: List<Int>) {
    val result = tryPermutations(program, 5, 9)

    println("2: The highest signal that can be sent to the thrusters: $result")
}

fun main(args: Array<String>) {
    runTests()

    val program = resourceLines("2019_7_1")
            .flatMap { it.split(",") }
            .map { it.toInt() }

    part1(program)
    part2(program)
}