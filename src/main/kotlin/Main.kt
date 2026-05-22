package io.cuttlefish

import java.lang.Math.toRadians




fun main() {
    val display = Display(800, 600, "fucked rendering")

    val target = display.frameBuffer


//    val stars = Stars3D(4096, 64f, 20f)

    val a = Vertex(-1f, -1f, 0f)
    val b = Vertex(0f, 1f, 0f)
    val c = Vertex(1f, -1f, 0f)


    val projection: Matrix4f = Matrix4f().initPerspective(
        fov = toRadians(70.0).toFloat(),
        aspectRatio = (target.width / target.height).toFloat(),
        zNear = 0.1f,
        zFar = 1000f
    )
    var previousTime = System.nanoTime()
    var rotCounter = 0f

    while (true) {
        val current = System.nanoTime()
        val delta = ((current - previousTime) / 1_000_000_000.0).toFloat()
        previousTime = current
//        stars.updateAndRender(target, delta)
        rotCounter += delta *5
        val translation = Matrix4f().initTranslation(0f, 0f, 3f) // set 3units away
        val rotation = Matrix4f().initRotation(0f, rotCounter, 0f)
        val transform = projection.Mul(translation.Mul(rotation))

        target.clear(0x00)
        target.fillTriangle(
            v1 = c.transform(transform),
            v2 = b.transform(transform),
            v3 = a.transform(transform)
        )

//        target.scanConvertTriangle(minY,midY,maxY,0)
//        target.fillShape(100, 300)


        display.swapBuffers()
    }

}