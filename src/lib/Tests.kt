package lib

fun testAssert(value: Boolean) = value || throw AssertionError()

fun testEquals(expected: Any?, actual: Any?) {
    if (!(expected == actual)) {
        throw AssertionError("Expected $expected, got $actual")
    }
}