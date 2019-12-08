
import lib.testEquals

private fun runTests() {
    /*
    111111 meets these criteria (double 11, never decreases).
    223450 does not meet these criteria (decreasing pair of digits 50).
    123789 does not meet these criteria (no double).
    */
    testEquals(true, validPasswordPt1(111111))
    testEquals(false, validPasswordPt1(223450))
    testEquals(false, validPasswordPt1(123789))


    /*
    112233 meets these criteria because the digits never decrease and all repeated digits are exactly two digits long.
    123444 no longer meets the criteria (the repeated 44 is part of a larger group of 444).
    111122 meets the criteria (even though 1 is repeated more than twice, it still contains a double 22).
     */
    testEquals(true, validPasswordPt2(112233))
    testEquals(false, validPasswordPt2(123444))
    testEquals(true, validPasswordPt2(111122))
}

private fun validPasswordPt1(password: Int): Boolean {
    // Char to int gives th code position
    val pwdDigits = password.toString().toCharArray().map { it.toInt() - 48 }

    var lowest = -1
    var adjacentDigits = false
    for (digit in pwdDigits) {
        if (digit < lowest) {
            return false
        } else if (digit == lowest) {
            adjacentDigits = true
        }
        lowest = digit
    }

    return adjacentDigits
}

private fun part1() {
    val valid = (125730..579381).count { validPasswordPt1(it) }
    println("1: Number different passwords: $valid")
}


private fun validPasswordPt2(password: Int): Boolean {
    // Char to int gives th code position
    val pwdDigits = password.toString().toCharArray().map { it.toInt() - 48 }

    var lowest = -1
    var adjacentDigits = false
    var numAdjacentDigits = 1
    for (digit in pwdDigits) {
        if (digit < lowest) {
            return false
        } else if (digit == lowest) {
            ++numAdjacentDigits
        } else {
            if (numAdjacentDigits == 2) {
                adjacentDigits = true
            }
            numAdjacentDigits = 1
        }

        lowest = digit
    }

    return adjacentDigits || numAdjacentDigits == 2
}

private fun part2() {
    val valid = (125730..579381).count { validPasswordPt2(it) }
    println("2: Number different passwords: $valid")
}

fun main(args: Array<String>) {
    runTests()

    part1()
    part2()
}