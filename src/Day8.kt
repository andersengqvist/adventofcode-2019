import lib.resourceLines
import lib.testEquals

private fun runTests() {
    val layers = createLayers(2, 3, listOf(1,2,3,4,5,6,7,8,9,0,1,2).toIntArray())
    testEquals(2, layers.size)
    testEquals(6, layers[0].get(1, 2))
}

private class Layer(val rows: Int, val cols: Int) {
    private val matrix = Array(rows, { IntArray(cols) })

    fun set(row: Int, col: Int, value: Int) {
        matrix[row][col] = value
    }

    fun get(row: Int, col: Int) = matrix[row][col]
}

private fun createLayers(rows: Int, cols: Int, data: IntArray): List<Layer> {
    val result = mutableListOf<Layer>()

    val elem = data.iterator()
    while (elem.hasNext()) {
        val layer = Layer(rows, cols)
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (elem.hasNext()) {
                    layer.set(row, col, elem.nextInt())
                }
                else {
                    throw IllegalArgumentException("Could not create a complete layer")
                }
            }
        }
        result.add(layer)
    }

    return result
}

private fun createImage(layers: List<Layer>): Layer {
    val layer = Layer(layers[0].rows, layers[0].cols)

    for (row in 0 until layer.rows) {
        for (col in 0 until layer.cols) {
            layer.set(row, col, -1)
            for (l in layers) {
                val pixel = l.get(row, col)
                if (pixel != 2) {
                    layer.set(row, col, pixel)
                    break
                }
            }
        }
    }

    return layer
}

private fun countDigits(layer: Layer, digit: Int): Int {
    var result = 0
    for (row in 0 until layer.rows) {
        for (col in 0 until layer.cols) {
            if (layer.get(row, col) == digit) {
                ++result
            }
        }
    }
    return result
}

private fun part1(layers: List<Layer>) {
    val layer = layers.minBy { countDigits(it, 0) }

    val result = layer?.let { countDigits(layer, 1) * countDigits(layer, 2) } ?:0
    println("1: The number of 1 digits multiplied by the number of 2 digits: $result")
}

private fun printImage(image: Layer) {
    println()
    println()
    for (row in 0 until image.rows) {
        for (col in 0 until image.cols) {
            val pixel = image.get(row, col)
            if (pixel == 1) {
                print(pixel)
            }
            else {
                print(" ")
            }
        }
        println()
    }
    println()
}

private fun part2(layers: List<Layer>) {
    val image = createImage(layers)
    println("1: Image:")
    printImage(image)
}

fun main(args: Array<String>) {
    runTests()

    val str = resourceLines("2019_8_1")
            .joinToString(separator = "")
    val data = str.toCharArray().map { it.toInt() - 48 }.toIntArray()
    val layers = createLayers(6, 25, data)


    part1(layers)
    part2(layers)
}