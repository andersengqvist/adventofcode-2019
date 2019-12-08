import lib.resourceLines
import lib.testEquals

private fun runTests() {
    val com = Orbitable("COM")
    val b = Orbitable("B", com)
    val c = Orbitable("C", b)
    val d = Orbitable("D", c)
    val e = Orbitable("E", d)
    val f = Orbitable("F", e)
    val g = Orbitable("G", b)
    val h = Orbitable("H", g)
    val i = Orbitable("I", d)
    val j = Orbitable("J", e)
    val k = Orbitable("k", j)
    val l = Orbitable("L", k)
    testEquals(12, com.size())
    testEquals(1, f.size())
    testEquals(42, com.checksum())
    testEquals(com.name, i.getRoot().name)
    testEquals("H", h.name)
    testEquals(0, l.checksum())

    val input = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L",
            "K)YOU",
            "I)SAN"
    )
    val (_, you, santa) = buildMap(input)
    if (you != null && santa != null) {
        testEquals(4, orbitsBetween(you, santa))
    }
    else {
        throw AssertionError("you and/or santa is null")
    }
}

class Orbitable(val name: String) {
    private var parent: Orbitable? = null
    private val children = mutableListOf<Orbitable>()

    constructor(name: String, parent: Orbitable): this(name) {
        parent.addChild(this)
    }

    fun addChild(child: Orbitable) {
        if (child.parent != null) {
            throw IllegalArgumentException("Child ${child.name} already has parent ${child.parent!!.name}")
        }
        child.parent = this
        children.add(child)
    }

    fun isRoot() = parent == null

    fun getRoot(): Orbitable = if (isRoot()) this else parent!!.getRoot()

    fun getParent() = parent

    fun size(): Int = children.map { it.size() }.toIntArray().sum() + 1

    fun checksum() = children.map { it.checkTheSum(1) }.toIntArray().sum()

    private fun checkTheSum(depth: Int): Int = children.map { it.checkTheSum(depth + 1) }.toIntArray().sum() + depth
}

private fun buildMap(input: List<String>): Triple<Orbitable, Orbitable?, Orbitable?> {
    val orbitsMap = mutableMapOf<String, Orbitable>()
    var objects = 0
    val regex = "^([^)]+)\\)([^)]+)$".toRegex()

    var you: Orbitable? = null
    var santa: Orbitable? = null
    for (s in input) {
        val matchResult = regex.find(s)
        matchResult?.let {

            val parentName = it.groupValues[1]
            val parent = orbitsMap.getOrPut(parentName) {
                ++objects
                Orbitable(parentName)
            }

            val childName = it.groupValues[2]
            val child = orbitsMap.getOrPut(childName) {
                ++objects
                Orbitable(childName)
            }
            if (childName == "YOU") {
                you = child
            } else if (childName == "SAN") {
                santa = child
            }

            parent.addChild(child)
        }
    }

    val root = orbitsMap.values.first().getRoot()
    if (objects != root.size()) {
        throw IllegalArgumentException("Not a good graph: objects: $objects, graph: ${root.size()}")
    }

    return Triple(root, you, santa)
}

private fun orbitsBetween(you: Orbitable, santa: Orbitable): Int {
    var yourParent = you.getParent()
    var yourDistance = 0
    var santasDistance = 0
    loop@ while (yourParent != null) {
        var santasParent = santa.getParent()
        santasDistance = 0
        while (santasParent != null) {
            if (yourParent.name == santasParent.name) {
                break@loop
            }
            santasParent = santasParent.getParent()
            ++santasDistance
        }
        yourParent = yourParent.getParent()
        ++yourDistance
    }
    return yourDistance + santasDistance
}

private fun part1(center: Orbitable) {
    val checksum = center.checksum()
    println("1: Total number of direct and indirect orbits: $checksum") // 270768
}

private fun part2(you: Orbitable, santa: Orbitable) {
    val distanse = orbitsBetween(you, santa)
    println("2: Minimum number of orbital transfers required: $distanse") // 451
}

fun main(args: Array<String>) {
    runTests()

    val input = resourceLines("2019_6_1")

    val (center, you, santa) = buildMap(input)
    part1(center)
    if (you != null && santa != null) {
        part2(you, santa)
    }
}