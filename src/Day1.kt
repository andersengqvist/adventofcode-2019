import lib.resourceLines
import lib.testAssert
import lib.testEquals

private fun runTests() {
    testAssert(calcFuel(12) == 2)
    testAssert(calcFuel(14) == 2)
    testAssert(calcFuel(1969) == 654)
    testAssert(calcFuel(100756) == 33583)

    testEquals(2, calcFuelWithAddedMassByFuel(14))
    testEquals(966, calcFuelWithAddedMassByFuel(1969))
    testEquals(50346, calcFuelWithAddedMassByFuel(100756))
}

private fun max(v1: Int, v2: Int) = if (v1 >= v2) v1 else v2

private fun calcFuel(mass: Int) = (mass / 3) - 2

private fun calcFuelOnlyMassByFuel(mass: Int): Int {
    if (mass == 0) {
        return 0
    }
    val result = max(calcFuel(mass), 0)
    return mass + calcFuelOnlyMassByFuel(result)
}

private fun calcFuelWithAddedMassByFuel(mass: Int) = calcFuelOnlyMassByFuel(calcFuel(mass))

private fun part1() {
    val result = resourceLines("2019_1_1")
            .map{ it.toInt() }
            .map { calcFuel(it) }
            .sum()

    println("1: The sum of the fuel requirements: $result")
}

private fun part2() {
    val result = resourceLines("2019_1_1")
            .map{ it.toInt() }
            .map { calcFuelWithAddedMassByFuel(it) }
            .sum()

    println("2: The sum of the fuel requirements: $result")
}

fun main(args: Array<String>) {
    runTests()
    part1()
    part2()
}