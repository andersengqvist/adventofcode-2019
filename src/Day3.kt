import lib.resourceLines
import lib.testEquals

private fun runTests() {
    testEquals(WireSegment(Direction("R"), 75), parseWireSegment("R75"))
    testEquals(WireSegment(Direction("U"), 7), parseWireSegment("U7"))
    testEquals(null, parseWireSegment("UP88"))

    testEquals(
            159,
            findClosestIntersect(
                    parseWireSegments(listOf("R75","D30","R83","U83","L12","D49","R71","U7","L72")),
                    parseWireSegments(listOf("U62","R66","U55","R34","D71","R55","D58","R83"))
            )
    )
    testEquals(
            135,
            findClosestIntersect(
                    parseWireSegments(listOf("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51")),
                    parseWireSegments(listOf("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"))
            )
    )

    testEquals(
            610,
            findMinNumbersOfSteps(
                    parseWireSegments(listOf("R75","D30","R83","U83","L12","D49","R71","U7","L72")),
                    parseWireSegments(listOf("U62","R66","U55","R34","D71","R55","D58","R83"))
            )
    )
    testEquals(
            410,
            findMinNumbersOfSteps(
                    parseWireSegments(listOf("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51")),
                    parseWireSegments(listOf("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"))
            )
    )
}

data class Direction(private val dir: String) {
    fun moveX(x: Int): Int {
        if ("R" == dir) {
            return x + 1
        }
        if ("L" == dir) {
            return x - 1
        }
        return x
    }
    fun moveY(y: Int): Int {
        if ("U" == dir) {
            return y + 1
        }
        if ("D" == dir) {
            return y - 1
        }
        return y
    }
}

data class WireSegment(val direction: Direction, val steps: Int)

fun parseWireSegment(s: String): WireSegment? {
    val regex = "^([UDLR])(\\d+)$".toRegex()
    val matchResult = regex.find(s)
    matchResult?.let {
        return WireSegment(Direction(it.groupValues[1]), it.groupValues[2].toInt())
    }
    return null
}

private fun parseWireSegments(wire: List<String>): List<WireSegment> {
    return wire.mapNotNull { parseWireSegment(it) }
}

data class Point(val x: Int, val y: Int) {
    fun manhattanDistance() = Math.abs(x) + Math.abs(y)
}

private fun findClosestIntersect(wire1: List<WireSegment>, wire2: List<WireSegment>): Int? {
    var xPos = 0
    var yPos = 0

    val visited = mutableSetOf<Point>()

    for ((direction, steps) in wire1) {
        for (step in 1..steps) {
            xPos = direction.moveX(xPos)
            yPos = direction.moveY(yPos)
            visited.add(Point(xPos, yPos))
        }
    }

    xPos = 0
    yPos = 0
    val intersections = mutableListOf<Point>()
    for (segment in wire2) {
        for (step in 1..segment.steps) {
            xPos = segment.direction.moveX(xPos)
            yPos = segment.direction.moveY(yPos)
            val p = Point(xPos, yPos)
            if (visited.contains(p)) {
                intersections.add(p)
            }
        }
    }
    return intersections
            .map { it.manhattanDistance() }
            .toIntArray()
            .min()

}


private fun findMinNumbersOfSteps(wire1: List<WireSegment>, wire2: List<WireSegment>): Int? {
    var xPos = 0
    var yPos = 0

    val visited = mutableSetOf<Point>()

    for (segment in wire1) {
        for (step in 1..segment.steps) {
            xPos = segment.direction.moveX(xPos)
            yPos = segment.direction.moveY(yPos)
            visited.add(Point(xPos, yPos))
        }
    }

    xPos = 0
    yPos = 0
    val intersections = mutableListOf<Point>()
    for (segment in wire2) {
        for (step in 1..segment.steps) {
            xPos = segment.direction.moveX(xPos)
            yPos = segment.direction.moveY(yPos)
            val p = Point(xPos, yPos)
            if (visited.contains(p)) {
                intersections.add(p)
            }
        }
    }
    return intersections
            .map { numSteps(wire1, it) + numSteps(wire2, it) }
            .toIntArray()
            .min()

}

fun numSteps(wire: List<WireSegment>, point: Point): Int {
    if (point.x == 0 && point.y == 0) {
        return 0
    }
    var xPos = 0
    var yPos = 0
    var numSteps = 0

    loop@ for (segment in wire) {
        for (step in 1..segment.steps) {
            xPos = segment.direction.moveX(xPos)
            yPos = segment.direction.moveY(yPos)
            ++numSteps
            if (point == Point(xPos, yPos)) {
                break@loop
            }
        }
    }
    return numSteps
}


private fun part1() {
    val wires = resourceLines("2019_3_1")
            .map { it.split(",") }
            .map { parseWireSegments(it) }

    val result = findClosestIntersect(wires[0], wires[1])
    println("1: Closest intersection is at: $result")
}

private fun part2() {
    val wires = resourceLines("2019_3_1")
            .map { it.split(",") }
            .map { parseWireSegments(it) }

    val result = findMinNumbersOfSteps(wires[0], wires[1])
    println("2: Min number of steps are: $result")
}

fun main(args: Array<String>) {

    runTests()

    part1()
    part2()
}