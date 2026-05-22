package io.cuttlefish


// foreshadowing
class Vertex(
    val pos: Vector4f,
    val colour: Vector4f
) {

//    constructor(x: Float, y: Float, z: Float) : this(Vector4f(x, y, z, 1f))

    // Expose x, y, and z safely
    val x: Float get() = pos.x
    val y: Float get() = pos.y
    val z: Float get() = pos.z


    // Very shitty way to deal with the problem of not using this.x/y
//    constructor(pos: Vector4f) : this(pos.x, pos.y, pos.z)

    fun transform(transformM: Matrix4f): Vertex {
        return Vertex(transformM.transform(pos),colour)
    }

    fun perspectiveDivide(): Vertex {
        // W is like a Z for where each VERTEX is, W is for perspective
        return Vertex(
            pos = Vector4f(x = pos.x / pos.w, y = pos.y / pos.w, z = pos.z / pos.w, w = pos.w)
            ,colour
        )
    }

    fun triangleAreaDouble(b: Vertex, c: Vertex): Float {
        val x1 = b.x - pos.x
        val y1 = b.y - pos.y

        val x2 = c.x - pos.x
        val y2 = c.y - pos.y

        return (x1 * y2 - x2 * y1) // Google said so: 2d cross product
    }
}
