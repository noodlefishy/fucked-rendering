package io.cuttlefish

import java.awt.Canvas
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import javax.swing.JFrame


class Display(width: Int, height: Int, title: String) : Canvas() {

    val frame: JFrame = JFrame()

    val frameBuffer = RenderContext(width, height)
    private val displayImage = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
    private val displayComponents = (displayImage.raster.dataBuffer as DataBufferByte).data

    // https://docs.oracle.com/javase/8/docs/api/java/awt/image/Raster.html#dataBuffer
    // "The DataBuffer that stores the image data", via magical kotlin class magic
//    val slider = JSlider(JSlider.HORIZONTAL, 0, 100, 50)


    init {
        val size = Dimension(width, height)
        size.also {
            preferredSize = it
            minimumSize = it
            maximumSize = it
        }

        createBufferStrategy(1)
        run {
            frame.add(this)
//            frame.add(slider, BorderLayout.SOUTH)
            frame.pack()
            frame.isResizable = false
            frame.title = title


//            frame.layout = FlowLayout()


            frame.setLocationRelativeTo(null) // set in middle of screen
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.isVisible = true

        }

//        frameBuffer.clear(0x80.toByte())
//        frameBuffer.drawPixels(
//            x = 100,
//            y = 100,
//            a  = 0x00.toByte(),
//            b = 0x00.toByte(),
//            g = 0x00.toByte(),
//            r  = 0xFF.toByte(),
//            )

    }

    val graphic: Graphics = bufferStrategy.drawGraphics

    fun swapBuffers() {
        frameBuffer.copyToByteArray(displayComponents)
        graphic.drawImage(/* p0 = */ displayImage,/* x = */
            0,/* p2 = */
            0,/* width = */
            frameBuffer.width,/* height = */
            frameBuffer.height,/* observer = */
            null
        )
        bufferStrategy.show()
    }

    fun GetFrameBuffer(): RenderContext {
        return frameBuffer
    }
}