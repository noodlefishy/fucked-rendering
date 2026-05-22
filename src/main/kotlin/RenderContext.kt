package io.cuttlefish

import kotlin.math.ceil

class RenderContext(override val width: Int, override val height: Int) : Bitmap(width, height) {
    val scanBuffer = IntArray(2 * height)


    fun fillTriangle(v1: Vertex, v2: Vertex, v3: Vertex) {
        val screenSpaceTransform = Matrix4f().initScreenSpaceTransform(
            halfWidth = width / 2.toFloat(), halfHeight = height / 2.toFloat()
        )

        var minY: Vertex = v1.transform(screenSpaceTransform).perspectiveDivide()
        var midY: Vertex = v2.transform(screenSpaceTransform).perspectiveDivide()
        var maxY: Vertex = v3.transform(screenSpaceTransform).perspectiveDivide()

        //TODO v1 | x = -1.428, y = 1.428, z = 2.800
        // v1.transform | x = -171.259, y = -128.444, z = 2.800

        if (maxY.y < midY.y) {
            val temp: Vertex = maxY
            maxY = midY
            midY = temp
        }
        if (midY.y < minY.y) {
            val temp: Vertex = midY
            midY = minY
            minY = temp
        }
        if (maxY.y < midY.y) {
            val temp: Vertex = maxY
            maxY = midY
            midY = temp
        }
        scanTriangle(
            minY, midY, maxY,
            whichSide = minY.triangleAreaDouble(maxY, midY) >= 0,
        )


//        val area: Float = minY.triangleAreaDouble(maxY, midY)
//        val whichSide = if (area >= 0) 1 else 0


//        scanTriangle(minY, midY, maxY, whichSide)
//        fillShape(minY.y.toInt(), maxY.y.toInt())

    }


    fun drawScanBuffer(y: Int, xMin: Int, xMax: Int) {
        scanBuffer[y * 2] = xMin; scanBuffer[y * 2 + 1] = xMax
    }

    fun fillShape(yMin: Int, yMax: Int) {
        for (i in yMin until yMax) {
            val xMin = scanBuffer[i * 2]
            val xMax = scanBuffer[i * 2 + 1]

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


    fun scanConvertTriangle(minY: Vertex, midY: Vertex, maxY: Vertex, whichSide: Int) {
        scanConvertLine(minY, maxY, 0 + whichSide)
        scanConvertLine(minY, midY, 1 - whichSide)
        scanConvertLine(midY, maxY, 1 - whichSide)
    }

    fun scanTriangle(minY: Vertex, midY: Vertex, maxY: Vertex, whichSide: Boolean) {
        val topToBottom: Edge = Edge(minY, maxY)
        val topToMiddle: Edge = Edge(minY, midY)
        val middleToBottom: Edge = Edge(midY, maxY)

        // These represent "whichside"
        // scanConvertLine(minY, maxY, 0 + whichSide)
        // scanConvertLine(minY, midY, 1 - whichSide)
        // scanConvertLine(midY, maxY, 1 - whichSide)
        var left = topToBottom // if whichside == 0, goes across whole triangle
        var right = topToMiddle
        if (whichSide) {
            val temp = left
            left = right
            right = temp
        }
        // Using topToMiddle because it represents all 3 states if doubled
        var yStart = topToMiddle.yStart.toInt()
        var yEnd = topToMiddle.yEnd.toInt()

        for (i in yStart until yEnd) {
            drawScanLine(left, right, i)
            left.step()
            right.step()
        }

        // This copy is the "doubled" part
        left = topToBottom
        right = middleToBottom
        if (whichSide) {
            val temp = left
            left = right
            right = temp
        }

        yStart = middleToBottom.yStart.toInt()
        yEnd = middleToBottom.yEnd.toInt()

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

    private fun scanConvertLine(minY: Vertex, maxY: Vertex, whichSide: Int) {
        val yStart = minY.y
        val yEnd = maxY.y
        val xStart = minY.x
        val xEnd = maxY.x

        val yDistance = yEnd - yStart
        val xDistance = xEnd - xStart

        if (yDistance <= 0) return

        val xStep: Float = xDistance / yDistance
        var xCurrent: Float = xStart
        for (j in yStart.toInt() until yEnd.toInt()) {
            scanBuffer[j * 2 + whichSide/* min/max side*/] = xCurrent.toInt()
            xCurrent += xStep

        }


    }
}