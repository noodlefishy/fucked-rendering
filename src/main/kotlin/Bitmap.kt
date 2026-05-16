package io.cuttlefish

import java.util.*

open class Bitmap(open val width: Int, open val height: Int, open val components: ByteArray = ByteArray(size = width * height * 4)) {

    init {
        // Java uses ARGB, 4 values, therefor each pixel is x4
    }


    fun clear(shade: Byte) {
        Arrays.fill(/* a = */ components, /* val = */ shade)

    }


    fun drawPixels(x: Int, y: Int, a: Byte, b: Byte, g: Byte, r: Byte) {
        val yIndexing = y * width
        val index = (x + yIndexing) * 4
        components[index + 0] = a
        components[index + 1] = b
        components[index + 2] = g
        components[index + 3] = r

    }

    fun copyToByteArray(destination: ByteArray) {

        for (i in 0 until width * height) {
            // We must ignore the 'a'
            destination[i * 3 + 0] = components[i * 4 + 1]
            destination[i * 3 + 1] = components[i * 4 + 2]
            destination[i * 3 + 2] = components[i * 4 + 3]

//            val a: Int = components[i * 4 + 0].toInt() shl 24
//            val r: Int = components[i * 4 + 1].toInt() shl 16
//            val g: Int = components[i * 4 + 2].toInt() shl 8
//            val b: Int = components[i * 4 + 3].toInt()
//
//            destination[i] = a or r or g or b

        }
    }
}