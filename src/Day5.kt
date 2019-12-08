import lib.IntCodeComputer
import lib.resourceLines

private fun part1(program: List<Int>) {
    val computer = IntCodeComputer(program)
    computer.addInput(1)
    computer.runProgram()
    assert(computer.isHalted(), { "Computer has not halted" } )
    val outputs = computer.outputs()
    outputs.forEachIndexed { index, value ->
        if (index < outputs.lastIndex) {
            assert(value == 0, { "Expected zero at index $index, got $value" })
        }
    }

    println("1: Diagnostic code: ${outputs.last()}")
}

private fun part2(program: List<Int>) {
    val computer = IntCodeComputer(program)
    computer.addInput(5)
    computer.runProgram()
    assert(computer.isHalted(), { "Computer has not halted" } )
    val outputs = computer.outputs()
    assert(outputs.size == 1, { "Expected one output, got ${outputs.size}" })

    println("1: Diagnostic code: ${outputs[0]}")
}

fun main(args: Array<String>) {

    val program = resourceLines("2019_5_1")
            .flatMap { it.split(",") }
            .map { it.toInt() }

    part1(program)
    part2(program)
}