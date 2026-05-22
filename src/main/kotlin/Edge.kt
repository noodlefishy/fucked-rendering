package io.cuttlefish

import kotlin.math.ceil

class Edge(
    gradients: Gradients, start: Vertex, //minY
    end: Vertex, // maxY
    minyIndex: Int
) {
    val yStart = ceil(start.y)
    val yEnd = ceil(end.y)

    val yDistance = end.y - start.y
    val xDistance = end.x - start.x

    val yPreStep = yStart /* CEILING! */ - start.y // On the Y

    val xStep = xDistance / yDistance
    var x = start.x + yPreStep * xStep

    val xPreStep = x - start.x

    var colour: Vector4f =
        gradients.colour[minyIndex] + (gradients.colourYStep * yPreStep) + (gradients.colourXStep * xPreStep)

    val colourStep: Vector4f = gradients.colourYStep + (gradients.colourXStep * xStep)
    fun step() {
        x += xStep
        colour += colourStep
    }
}