import lib.resourceLines
import lib.testEquals

private fun runTests() {
    testEquals(listOf(2,0,0,0,99), runProgram(listOf(1,0,0,0,99)))
    testEquals(listOf(2,3,0,6,99), runProgram(listOf(2,3,0,3,99)))
    testEquals(listOf(2,4,4,5,99,9801), runProgram(listOf(2,4,4,5,99,0)))
    testEquals(listOf(30,1,1,4,2,5,6,0,99), runProgram(listOf(1,1,1,4,99,5,6,0,99)))
    testEquals(listOf(3500,9,10,70,2,3,11,0,99,30,40,50), runProgram(listOf(1,9,10,3,2,3,11,0,99,30,40,50)))
}

private fun runProgram(program: List<Int>): List<Int> {
    val mutableProgram = mutableListOf<Int>()
    mutableProgram.addAll(program)
    var index = 0
    while (index + 3 < mutableProgram.size) {
        val instruction = mutableProgram[index]
        if (instruction != 99) {
            val idx1 = mutableProgram[index + 1]
            val idx2 = mutableProgram[index + 2]
            val resultIndex = mutableProgram[index + 3]
            if (idx1 < 0) {
                throw IllegalArgumentException("idx1 is less than 0: $idx1 < 0 at index ${mutableProgram[index + 1]}")
            }
            if (idx1 >= mutableProgram.size) {
                throw IllegalArgumentException("idx1 is greater than program: $idx1 > ${mutableProgram.size} at index ${mutableProgram[index + 1]}")
            }
            if (idx2 < 0) {
                throw IllegalArgumentException("idx2 is less than 0: $idx2 < 0 at index ${mutableProgram[index + 2]}")
            }
            if (idx2 >= mutableProgram.size) {
                throw IllegalArgumentException("idx2 is greater than program: $idx2 > ${mutableProgram.size} at index ${mutableProgram[index + 2]}")
            }
            if (resultIndex < 0) {
                throw IllegalArgumentException("Resultindex is less than 0: $resultIndex < 0 at index ${mutableProgram[index + 3]}")
            }
            if (resultIndex >= mutableProgram.size) {
                throw IllegalArgumentException("Resultindex is greater than program: $resultIndex > ${mutableProgram.size} at index ${mutableProgram[index + 3]}")
            }
            if (instruction == 1) {
                mutableProgram[resultIndex] = mutableProgram[idx1] + mutableProgram[idx2]
            } else if (instruction == 2) {
                mutableProgram[resultIndex] = mutableProgram[idx1] * mutableProgram[idx2]
            } else {
                throw IllegalArgumentException("Unknown instruction: $instruction at index $index")
            }
        }
        else {
            index = mutableProgram.size
        }
        index += 4
    }
    return mutableProgram
}

private fun part1(program: List<Int>) {
    val mutableProgram = program.toMutableList()
    mutableProgram[1] = 12 // noun
    mutableProgram[2] = 2 // verb
    val result = runProgram(mutableProgram)
    println("1: Value at position 0: ${result[0]}")
}

private fun part2(program: List<Int>) {

    var found = false
    var result = 0

    loop@ for (noun in 0..100) {
        for (verb in 0..100) {
            val mutableProgram = program.toMutableList()
            mutableProgram[1] = noun
            mutableProgram[2] = verb
            val runned = runProgram(mutableProgram)
            if (runned[0] == 19690720) {
                result = 100 * noun + verb
                found = true
                break@loop
            }
        }
    }

    if (found) {
        println("2: 100 * noun + verb: ${result}")
    }
}


fun main(args: Array<String>) {

    runTests()

    val program = resourceLines("2019_2_1")
            .flatMap { it.split(",") }
            .map{ it.toInt() }

    part1(program)
    part2(program)
}