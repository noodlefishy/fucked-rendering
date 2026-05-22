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

    var textureCord: Vector4f =
        gradients.textureCords[minyIndex] + (gradients.textureCordYStep * yPreStep) + (gradients.textureCordXStep * xPreStep)

    val colourStep: Vector4f = gradients.textureCordYStep + (gradients.textureCordXStep * xStep)
    fun step() {
        x += xStep
        textureCord += colourStep
    }
}