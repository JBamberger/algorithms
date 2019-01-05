package de.jbamberger.algorithms





fun Long.log2():Int {
    var i = this
    var j = -1
    while (i != 0L) {
        j++
        i = i ushr 1
    }
    return j
}

fun Int.log2():Int {
    var i = this
    var j = -1

    while (i != 0) {
        j++
        i = i ushr 1
    }
    return j
}