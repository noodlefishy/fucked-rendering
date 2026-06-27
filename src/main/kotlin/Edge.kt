package io.cuttlefish

import kotlin.math.*

class Edge(
    gradients: Gradients,
    start: Vertex, //minY
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

    val textureCordX = gradients.textureCords[minyIndex].x +
            gradients.textureCordXStep.x * xPreStep +
            gradients.textureCordXStep.y * yPreStep

    val textureCordXStep = gradients.textureCordXStep.y + gradients.textureCordXStep.x * xStep


    val textureCordY = gradients.textureCords[minyIndex].y +
            gradients.textureCordYStep.x * xPreStep +
            gradients.textureCordYStep.y * yPreStep

    val textureCordYStep = gradients.textureCordYStep.y + gradients.textureCordYStep.x * xStep


//    val oneOverZStep =


    val colourStep: Vector4f = gradients.textureCordYStep + (gradients.textureCordXStep * xStep)
    fun step() {
        x += xStep
        textureCord += colourStep
    }
}