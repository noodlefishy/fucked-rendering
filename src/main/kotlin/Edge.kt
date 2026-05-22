package io.cuttlefish

import kotlinx.coroutines.runBlocking
import kotlin.math.ceil

class Edge(
    start: Vertex, //minY
    end: Vertex // maxY
) {
    val yStart = ceil(start.y)
    val yEnd = ceil(end.y)

    val yDistance = end.y - start.y
    val xDistance = end.x - start.x

    val preStep = yStart /* CEILING! */ - start.y // On the Y
    val xStep = xDistance / yDistance
    var x = start.x + preStep * xStep

    fun step() = runBlocking {
        x += xStep
    }

}