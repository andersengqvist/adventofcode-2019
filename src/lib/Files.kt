package lib

import java.io.File

fun resourceLines(fileName: String): List<String> = File("resources/${fileName}.txt").readLines()