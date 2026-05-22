package io.cuttlefish

import kotlin.math.ceil

class RenderContext(override val width: Int, override val height: Int) : Bitmap(width, height) {


    fun fillTriangle(v1: Vertex, v2: Vertex, v3: Vertex) {
        val screenSpaceTransform = Matrix4f().initScreenSpaceTransform(
            halfWidth = width / 2.toFloat(), halfHeight = height / 2.toFloat()
        )

        val sortedVertices = listOf(v1, v2, v3)
            .map { it.transform(screenSpaceTransform).perspectiveDivide() }
            .sortedBy { it.y }

        val minY = sortedVertices[0]
        val midY = sortedVertices[1]
        val maxY = sortedVertices[2]

        scanTriangle(
            minY, midY, maxY,
            whichSide = minY.triangleAreaDouble(maxY, midY) >= 0,
        )


//        val area: Float = minY.triangleAreaDouble(maxY, midY)
//        val whichSide = if (area >= 0) 1 else 0


//        scanTriangle(minY, midY, maxY, whichSide)
//        fillShape(minY.y.toInt(), maxY.y.toInt())

    }

    fun scanTriangle(minY: Vertex, midY: Vertex, maxY: Vertex, whichSide: Boolean) {
        val topToBottom = Edge(minY, maxY)
        val topToMiddle = Edge(minY, midY)
        val middleToBottom = Edge(midY, maxY)

        // First segment: from minY to midY
        processScanSegment(topToBottom, topToMiddle, whichSide, topToMiddle.yStart.toInt(), topToMiddle.yEnd.toInt())

        // Second segment: from midY to maxY
        processScanSegment(topToBottom, middleToBottom, whichSide, middleToBottom.yStart.toInt(), middleToBottom.yEnd.toInt())
    }

    /**
     * Processes a segment of the triangle by drawing scan lines between two edges.
     * The `whichSide` parameter determines which edge is considered 'left' and 'right'
     */
    private fun processScanSegment(edgeA: Edge, edgeB: Edge, whichSide: Boolean, yStart: Int, yEnd: Int) {
        var left = edgeA
        var right = edgeB

        if (whichSide) {
            val temp = left
            left = right
            right = temp
        }

        for (i in yStart until yEnd) {
            drawScanLine(left, right, i)
            left.step()
            right.step()
        }
    }

    private fun drawScanLine(left: Edge, right: Edge, i: Int) {
        val xMin = ceil(left.x).toInt()
        val xMax = ceil(right.x).toInt()

        for (j in xMin until xMax) {
            drawPixels(
                j, i,
                a = 0xFF.toByte(),
                b = 0xFF.toByte(),
                g = 0xFF.toByte(),
                r = 0xFF.toByte(),
            )
        }

    }
}