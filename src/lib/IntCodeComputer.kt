package lib

/**
 * Return the digit at index idx.
 * Counting starts at the least significant digit.
 * If idx is greater than the size of the number, 0 is returned
 *
 * Example digitAt(4, 0) == 4
 * Example digitAt(4, 1) == 0
 * Example digitAt(1234, 1) == 3
 * Example digitAt(1234, 1) == 3
 */
private fun digitAt(number: Int, idx: Int): Int {
    var result = number
    for (i in 0 until idx) {
        result /= 10
    }
    return result % 10
}


class IntCodeComputer(program: List<Int>) {
    private val mutableProgram = program.toMutableList()
    private val inputs = mutableListOf<Int>()
    private val outputs = mutableListOf<Int>()
    private var index = 0

    fun addInput(input: Int) {
        inputs.add(input)
    }

    fun addInputs(ins: List<Int>) {
        inputs.addAll(ins)
    }

    fun outputs(): List<Int> {
        val res = outputs.toList()
        outputs.clear()
        return res
    }

    fun isHalted() = index >= mutableProgram.size

    private fun getVal(idx: Int, mode: Int): Int {
        if (idx < 0 ) {
            throw IllegalArgumentException("Index less than 0: $idx")
        }
        if (idx >= mutableProgram.size) {
            throw IllegalArgumentException("Index larger than program: $idx >= ${mutableProgram.size}")
        }
        if (mode == 0) {
            // position mode
            val newIdx = mutableProgram[idx]
            if (newIdx < 0 ) {
                throw IllegalArgumentException("Index at position $idx less than 0: $newIdx")
            }
            if (idx >= mutableProgram.size) {
                throw IllegalArgumentException("Index at position $idx larger than program: $newIdx >= ${mutableProgram.size}")
            }
            return mutableProgram[newIdx]
        } else if (mode == 1) {
            // immediate mode
            return mutableProgram[idx]
        }
        throw IllegalArgumentException("Unknown mode: $mode")
    }

    /**
     * Run program until halted, or input is required
     */
    fun runProgram() {
        var waitingForInput = false
        while (index < mutableProgram.size && !waitingForInput) {
            val instruction = mutableProgram[index]
            val opCode = digitAt(instruction, 1) * 10 + digitAt(instruction, 0)
            val paramMode1 = digitAt(instruction, 2)
            val paramMode2 = digitAt(instruction, 3)
            if (opCode == 99) {
                index = mutableProgram.size
            } else if (opCode == 1) {
                val param1 = getVal(index + 1, paramMode1)
                val param2 = getVal(index + 2, paramMode2)
                val resultIndex = mutableProgram[index + 3]
                mutableProgram[resultIndex] = param1 + param2
                index += 4
            } else if (opCode == 2) {
                val param1 = getVal(index + 1, paramMode1)
                val param2 = getVal(index + 2, paramMode2)
                val resultIndex = mutableProgram[index + 3]
                mutableProgram[resultIndex] = param1 * param2
                index += 4
            } else if (opCode == 3) {
                if (inputs.isNotEmpty()) {
                    val input = inputs[0]
                    inputs.removeAt(0)
                    val resultIndex = mutableProgram[index + 1]
                    mutableProgram[resultIndex] = input
                    index += 2
                }
                else {
                    waitingForInput = true
                }
            } else if (opCode == 4) {
                val paramMode = digitAt(instruction, 2)
                val param = getVal(index + 1, paramMode)
                outputs.add(param)
                index += 2
            } else if (opCode == 5) {
                /*
                 Opcode 5 is jump-if-true:
                 if the first parameter is non-zero,
                 it sets the instruction pointer to the value from the second parameter.
                  Otherwise, it does nothing.
                 */
                val param1 = getVal(index + 1, paramMode1)
                if (param1 != 0) {
                    val param2 = getVal(index + 2, paramMode2)
                    index = param2
                } else {
                    index += 3
                }
            } else if (opCode == 6) {
                /*
                 * Opcode 6 is jump-if-false:
                 * if the first parameter is zero,
                  * it sets the instruction pointer to the value from the second parameter.
                  * Otherwise, it does nothing.
                 */
                val param1 = getVal(index + 1, paramMode1)
                if (param1 == 0) {
                    val param2 = getVal(index + 2, paramMode2)
                    index = param2
                } else {
                    index += 3
                }
            } else if (opCode == 7) {
                /*
                Opcode 7 is less than: if the first parameter is less than the second parameter,
                 it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
                 */
                val param1 = getVal(index + 1, paramMode1)
                val param2 = getVal(index + 2, paramMode2)
                val resultIndex = mutableProgram[index + 3]
                if (param1 < param2) {
                    mutableProgram[resultIndex] = 1
                } else {
                    mutableProgram[resultIndex] = 0
                }
                index += 4
            } else if (opCode == 8) {
                /*
                Opcode 8 is equals: if the first parameter is equal to the second parameter,
                it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
                 */
                val param1 = getVal(index + 1, paramMode1)
                val param2 = getVal(index + 2, paramMode2)
                val resultIndex = mutableProgram[index + 3]
                if (param1 == param2) {
                    mutableProgram[resultIndex] = 1
                } else {
                    mutableProgram[resultIndex] = 0
                }
                index += 4
            } else {
                throw Exception("Unexpected opCode $opCode at index $index")
            }
        }
    }
}