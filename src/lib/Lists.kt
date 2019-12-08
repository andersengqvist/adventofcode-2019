package lib

fun <T> MutableList<T>.swap(idx1: Int, idx2: Int) {
    val tmp = this[idx1]
    this[idx1] = this[idx2]
    this[idx2] = tmp
}

/**
 * Calculates all permutation of the list and calls the callback with each permutation.
 * Algorithm taken from https://www.baeldung.com/java-array-permutations but rewritten for Kotlin
 */
fun <T> List<T>.permute(callback: (List<T>) -> Unit) {
    val elements = this.toMutableList()

    val indexes = MutableList(this.size, { 0 })

    callback(elements)

    var i = 0
    while (i < this.size) {
        if (indexes[i] < i) {
            elements.swap(if (i % 2 == 0)  0 else indexes[i], i)
            callback(elements)
            indexes[i]++
            i = 0
        }
        else {
            indexes[i] = 0
            i++
        }
    }
}

/**
 * Similar to permute, but the callback returns a value.
 * The value is collected in a list and returned
 */
fun <T, U> List<T>.permuteMap(callback: (List<T>) -> U): List<U> {
    val elements = this.toMutableList()
    val result = mutableListOf<U>()
    val indexes = MutableList(this.size, { 0 })

    result.add(callback(elements))

    var i = 0
    while (i < this.size) {
        if (indexes[i] < i) {
            elements.swap(if (i % 2 == 0)  0 else indexes[i], i)
            result.add(callback(elements))
            indexes[i]++
            i = 0
        }
        else {
            indexes[i] = 0
            i++
        }
    }
    return result
}