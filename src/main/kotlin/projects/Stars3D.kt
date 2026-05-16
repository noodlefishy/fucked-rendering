package io.cuttlefish.projects

import io.cuttlefish.RenderContext
import java.lang.Math.toRadians
import kotlin.math.tan
import kotlin.random.Random

class Stars3D(
    val numberOfStars: Int,
    val spread: Float,
    val speed: Float,
    private val starX: FloatArray = FloatArray(numberOfStars),
    private val starY: FloatArray = FloatArray(numberOfStars),
    private val starZ: FloatArray = FloatArray(numberOfStars),

    ) {
    init {
        for (i in 0 until numberOfStars) initStar(i)
    }


    // x = -1 = left
    // x = 1 = right

    private fun initStar(index: Int) {
        // 2 * ... - 0.5f is so it is between -1.0 and 1.0
        starX[index] = (2 * (Random.nextFloat() - 0.5f) * spread)// .coerceIn(-1f,1f)
        starY[index] = (2 * (Random.nextFloat() - 0.5f).coerceIn(-1f, 1f) * spread)//.coerceIn(-1f,1f)

        // -1 is the camera, so lets just make sure it is not there
        starZ[index] = (Random.nextFloat() + 0.000001f) * spread
    }

    fun updateAndRender(target: RenderContext, delta: Float) {
        val tanHalfFOV = tan(toRadians((90f / 2f).toDouble()))
        target.clear(0x00.toByte())

        val hHeight = target.height / 2f
        val hWidth = target.width / 2f


        for (i in 0 until numberOfStars) {
            starZ[i] -= delta * speed // subtract to TAKE AWAY from camera

            if (starZ[i] <= 0) {
                initStar(i)
            }

            // https://www.youtube.com/watch?v=D3IhkRulkFE&list=PLEETnX-uPtBUbVOok816vTl1K9vV1GgH5&index=7
            // the reason for / starZ[i]

            val x: Int = ((starX[i] / (tanHalfFOV * starZ[i])) * hWidth + hWidth).toInt()
            val y: Int = ((starY[i] / (tanHalfFOV * starZ[i])) * hHeight + hHeight).toInt()



            if ((x < 0 || x >= target.width) || (y < 0 || y >= target.height)) {
                initStar(i)
            } else {
                target.drawPixels(
                    x, y, a = 0xFF.toByte(), b = 0xFF.toByte(), g = 0xFF.toByte(), r = 0xFF.toByte()
                )
            }

        }

    }
}