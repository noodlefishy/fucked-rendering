package io.cuttlefish

class Gradients(miny: Vertex, midy: Vertex, maxy: Vertex) {
    val textureCords = arrayOf(miny.textureCords, midy.textureCords, maxy.textureCords)

    private val oneOverDX = 1f /
            (((midy.x - maxy.x) * (miny.y - maxy.y)) - ((miny.x - maxy.x) * (midy.y - maxy.y)))

    val oneOverDY = -oneOverDX

    val textureCordXStep = (((textureCords[1] - textureCords[2]) * (miny.y - maxy.y)) -
            ((textureCords[0] - textureCords[2]) * (midy.y - maxy.y))) * oneOverDX

    val textureCordYStep = (((textureCords[1] - textureCords[2]) * (miny.x - maxy.x)) -
            ((textureCords[0] - textureCords[2]) * (midy.x - maxy.x))) * oneOverDY

    val textureCordX = FloatArray(3)
    val textureCordY = FloatArray(3)
    val oneOverZ = FloatArray(3)
    val depth = FloatArray(3)
    val lightAmt = FloatArray(3)

    // --- Steps (Kotlin Styled) ---
    val textureCordXXStep: Float
    val textureCordXYStep: Float
    val textureCordYXStep: Float
    val textureCordYYStep: Float
    val oneOverZXStep: Float
    val oneOverZYStep: Float
    val depthXStep: Float
    val depthYStep: Float
    val lightAmtXStep: Float
    val lightAmtYStep: Float

    init {
        // Interpolation depth setup (Z component of position vector)
        depth[0] = miny.pos.z
        depth[1] = midy.pos.z
        depth[2] = maxy.pos.z

        // W component contains the perspective Z info
        oneOverZ[0] = 1.0f / miny.pos.w
        oneOverZ[1] = 1.0f / midy.pos.w
        oneOverZ[2] = 1.0f / maxy.pos.w

        // Project texture coordinates to screen space using 1/Z
        textureCordX[0] = miny.textureCords.x * oneOverZ[0]
        textureCordX[1] = midy.textureCords.x * oneOverZ[1]
        textureCordX[2] = maxy.textureCords.x * oneOverZ[2]

        textureCordY[0] = miny.textureCords.y * oneOverZ[0]
        textureCordY[1] = midy.textureCords.y * oneOverZ[1]
        textureCordY[2] = maxy.textureCords.y * oneOverZ[2]

        // Gradients calculation across X & Y steps
        textureCordXXStep = calcXStep(textureCordX, miny, midy, maxy, oneOverDX)
        textureCordXYStep = calcYStep(textureCordX, miny, midy, maxy, oneOverDY)
        textureCordYXStep = calcXStep(textureCordY, miny, midy, maxy, oneOverDX)
        textureCordYYStep = calcYStep(textureCordY, miny, midy, maxy, oneOverDY)
        oneOverZXStep = calcXStep(oneOverZ, miny, midy, maxy, oneOverDX)
        oneOverZYStep = calcYStep(oneOverZ, miny, midy, maxy, oneOverDY)
        depthXStep = calcXStep(depth, miny, midy, maxy, oneOverDX)
        depthYStep = calcYStep(depth, miny, midy, maxy, oneOverDY)
        lightAmtXStep = calcXStep(lightAmt, miny, midy, maxy, oneOverDX)
        lightAmtYStep = calcYStep(lightAmt, miny, midy, maxy, oneOverDY)
    }

    // --- Private Math Helpers ---
    private fun calcXStep(values: FloatArray, minYVert: Vertex, midYVert: Vertex, maxYVert: Vertex, oneOverdX: Float): Float {
        return (((values[1] - values[2]) * (minYVert.y - maxYVert.y)) -
                ((values[0] - values[2]) * (midYVert.y - maxYVert.y))) * oneOverdX
    }

    private fun calcYStep(values: FloatArray, minYVert: Vertex, midYVert: Vertex, maxYVert: Vertex, oneOverdY: Float): Float {
        return (((values[1] - values[2]) * (minYVert.x - maxYVert.x)) -
                ((values[0] - values[2]) * (midYVert.x - maxYVert.x))) * oneOverdY
    }

    private fun saturate(value: Float): Float {
        return value.coerceIn(0.0f, 1.0f)
    }
}