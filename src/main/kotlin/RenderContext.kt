package io.cuttlefish

import kotlin.math.*

class RenderContext(override val width: Int, override val height: Int) : Bitmap(width, height) {


    fun fillTriangle(v1: Vertex, v2: Vertex, v3: Vertex, texture: Bitmap) {
        val screenSpaceTransform = Matrix4f().initScreenSpaceTransform(
            halfWidth = width / 2.toFloat(), halfHeight = height / 2.toFloat()
        )

        val sortedVertices =
            listOf(v1, v2, v3).map { it.transform(screenSpaceTransform).perspectiveDivide() }.sortedBy { it.y }

        val minY = sortedVertices[0]
        val midY = sortedVertices[1]
        val maxY = sortedVertices[2]

        scanTriangle(
            minY, midY, maxY, whichSide = minY.triangleAreaDouble(maxY, midY) >= 0, texture = texture
        )


//        val area: Float = minY.triangleAreaDouble(maxY, midY)
//        val whichSide = if (area >= 0) 1 else 0


//        scanTriangle(minY, midY, maxY, whichSide)
//        fillShape(minY.y.toInt(), maxY.y.toInt())

    }

    fun scanTriangle(minY: Vertex, midY: Vertex, maxY: Vertex, whichSide: Boolean, texture: Bitmap) {
        val gradients = Gradients(minY, midY, maxY)
        val topToBottom = Edge(gradients, minY, maxY, 0)
        val topToMiddle = Edge(gradients, minY, midY, 0)
        val middleToBottom = Edge(gradients, midY, maxY, 1)

        // First segment: from minY to midY
        processEdgeScan(
            topToBottom, topToMiddle, whichSide, topToMiddle.yStart.toInt(), topToMiddle.yEnd.toInt(), texture
        )

        // Second segment: from midY to maxY
        processEdgeScan(
            topToBottom, middleToBottom, whichSide, middleToBottom.yStart.toInt(), middleToBottom.yEnd.toInt(), texture
        )
    }

    /**
     * Processes a segment of the triangle by drawing scan lines between two edges.
     * The `whichSide` parameter determines which edge is considered 'left' and 'right'
     */
    private fun processEdgeScan(
        edgeA: Edge, edgeB: Edge, whichSide: Boolean, yStart: Int, yEnd: Int, texture: Bitmap
    ) {
        var left = edgeA
        var right = edgeB

        if (whichSide) {
            val temp = left
            left = right
            right = temp
        }

        val clampedYStart = yStart.coerceIn(0, height)
        val clampedYEnd = yEnd.coerceIn(0, height)


        for (i in clampedYStart until clampedYEnd) {
            drawScanLine(left, right, i, texture)
            left.step()
            right.step()
        }
    }

    private fun drawScanLine(left: Edge, right: Edge, i: Int, texture: Bitmap) {
        val xMin = ceil(left.x).toInt()
        val xMax = ceil(right.x).toInt()

        val xPreStepAbstract = xMin - left.x

        val xDistance = right.x - left.x

        val texturecordxXstep = (right.textureCord.x - left.textureCord.x) / xDistance
        val texturecordxYstep = (right.textureCord.y - left.textureCord.y) / xDistance


        var textureCordX = left.textureCord.x + texturecordxXstep * xPreStepAbstract
        var textureCordY = left.textureCord.y + texturecordxYstep * xPreStepAbstract


        // Color + (Step * Offset)
//        val minColour = left.textureCord + (gradients.textureCordXStep * xPreStepMin)
//        val maxColour = right.textureCord + (gradients.textureCordXStep * xPreStepMax)
//
//        var lerpAmount = 0f
//        val lerpStep = 1f / (xMax - xMin)

        for (j in xMin until xMax) {
//            val r = (colour.x * 255f + 0.5f).toInt().toByte()
//            val g = (colour.y * 255f + 0.5f).toInt().toByte()
//            val b = (colour.z * 255f + 0.5f).toInt().toByte()
//            drawPixels(j, i, a = 0xFF.toByte(), b = b, g = g, r = r)

            val srcX: Int = (textureCordX * (texture.width - 1) + 0.5f).toInt()
            val srcY: Int = (textureCordY * (texture.height - 1) + 0.5f).toInt()

            copyPixel(j, i, srcX, srcY, texture)

            textureCordX += texturecordxXstep
            textureCordY += texturecordxYstep
            //lerpAmount += lerpStep
        }

    }
}