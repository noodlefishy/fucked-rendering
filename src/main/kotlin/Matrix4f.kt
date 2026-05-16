package io.cuttlefish

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix4f {
    private var matrix: Array<FloatArray?>

    init {
        matrix = Array(4) { FloatArray(4) }
    }

    fun initIdentity(): Matrix4f {
        matrix[0]!![0] = 1f; matrix[1]!![0] = 0f
        matrix[0]!![1] = 0f; matrix[1]!![1] = 1f
        matrix[0]!![2] = 0f; matrix[1]!![2] = 0f
        matrix[0]!![3] = 0f; matrix[1]!![3] = 0f

        matrix[2]!![0] = 0f; matrix[3]!![0] = 0f
        matrix[2]!![1] = 0f; matrix[3]!![1] = 0f
        matrix[2]!![2] = 1f; matrix[3]!![2] = 0f
        matrix[2]!![3] = 0f; matrix[3]!![3] = 1f

        return this
    }

    // take normalized co-ords, and turn into pixel places
    fun initScreenSpaceTransform(halfWidth: Float, halfHeight: Float): Matrix4f {
        matrix[0]!![0] = halfWidth
        matrix[0]!![1] = 0f
        matrix[0]!![2] = 0f
        matrix[0]!![3] = halfWidth

        matrix[1]!![0] = 0f
        matrix[1]!![1] = -halfHeight
        matrix[1]!![2] = 0f
        matrix[1]!![3] = halfHeight

        matrix[2]!![0] = 0f
        matrix[2]!![1] = 0f
        matrix[2]!![2] = 1f
        matrix[2]!![3] = 0f

        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun initTranslation(x: Float, y: Float, z: Float): Matrix4f {
        matrix[0]!![0] = 1f
        matrix[0]!![1] = 0f
        matrix[0]!![2] = 0f
        matrix[0]!![3] = x
        matrix[1]!![0] = 0f
        matrix[1]!![1] = 1f
        matrix[1]!![2] = 0f
        matrix[1]!![3] = y
        matrix[2]!![0] = 0f
        matrix[2]!![1] = 0f
        matrix[2]!![2] = 1f
        matrix[2]!![3] = z
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun initRotation(x: Float, y: Float, z: Float, angle: Float): Matrix4f {
        val sin = sin(angle.toDouble()).toFloat()
        val cos = cos(angle.toDouble()).toFloat()

        matrix[0]!![0] = cos + x * x * (1 - cos)
        matrix[0]!![1] = x * y * (1 - cos) - z * sin
        matrix[0]!![2] = x * z * (1 - cos) + y * sin
        matrix[0]!![3] = 0f
        matrix[1]!![0] = y * x * (1 - cos) + z * sin
        matrix[1]!![1] = cos + y * y * (1 - cos)
        matrix[1]!![2] = y * z * (1 - cos) - x * sin
        matrix[1]!![3] = 0f
        matrix[2]!![0] = z * x * (1 - cos) - y * sin
        matrix[2]!![1] = z * y * (1 - cos) + x * sin
        matrix[2]!![2] = cos + z * z * (1 - cos)
        matrix[2]!![3] = 0f
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun initRotation(x: Float, y: Float, z: Float): Matrix4f {
        val rx = Matrix4f()
        val ry = Matrix4f()
        val rz = Matrix4f()

        rz.matrix[0]!![0] = cos(z.toDouble()).toFloat()
        rz.matrix[0]!![1] = -sin(z.toDouble()).toFloat()
        rz.matrix[0]!![2] = 0f
        rz.matrix[0]!![3] = 0f
        rz.matrix[1]!![0] = sin(z.toDouble()).toFloat()
        rz.matrix[1]!![1] = cos(z.toDouble()).toFloat()
        rz.matrix[1]!![2] = 0f
        rz.matrix[1]!![3] = 0f
        rz.matrix[2]!![0] = 0f
        rz.matrix[2]!![1] = 0f
        rz.matrix[2]!![2] = 1f
        rz.matrix[2]!![3] = 0f
        rz.matrix[3]!![0] = 0f
        rz.matrix[3]!![1] = 0f
        rz.matrix[3]!![2] = 0f
        rz.matrix[3]!![3] = 1f

        rx.matrix[0]!![0] = 1f
        rx.matrix[0]!![1] = 0f
        rx.matrix[0]!![2] = 0f
        rx.matrix[0]!![3] = 0f
        rx.matrix[1]!![0] = 0f
        rx.matrix[1]!![1] = cos(x.toDouble()).toFloat()
        rx.matrix[1]!![2] = -sin(x.toDouble()).toFloat()
        rx.matrix[1]!![3] = 0f
        rx.matrix[2]!![0] = 0f
        rx.matrix[2]!![1] = sin(x.toDouble()).toFloat()
        rx.matrix[2]!![2] = cos(x.toDouble()).toFloat()
        rx.matrix[2]!![3] = 0f
        rx.matrix[3]!![0] = 0f
        rx.matrix[3]!![1] = 0f
        rx.matrix[3]!![2] = 0f
        rx.matrix[3]!![3] = 1f

        ry.matrix[0]!![0] = cos(y.toDouble()).toFloat()
        ry.matrix[0]!![1] = 0f
        ry.matrix[0]!![2] = -sin(y.toDouble()).toFloat()
        ry.matrix[0]!![3] = 0f
        ry.matrix[1]!![0] = 0f
        ry.matrix[1]!![1] = 1f
        ry.matrix[1]!![2] = 0f
        ry.matrix[1]!![3] = 0f
        ry.matrix[2]!![0] = sin(y.toDouble()).toFloat()
        ry.matrix[2]!![1] = 0f
        ry.matrix[2]!![2] = cos(y.toDouble()).toFloat()
        ry.matrix[2]!![3] = 0f
        ry.matrix[3]!![0] = 0f
        ry.matrix[3]!![1] = 0f
        ry.matrix[3]!![2] = 0f
        ry.matrix[3]!![3] = 1f

        matrix = rz.Mul(ry.Mul(rx)).getM()

        return this
    }

    fun initScale(x: Float, y: Float, z: Float): Matrix4f {
        matrix[0]!![0] = x
        matrix[0]!![1] = 0f
        matrix[0]!![2] = 0f
        matrix[0]!![3] = 0f
        matrix[1]!![0] = 0f
        matrix[1]!![1] = y
        matrix[1]!![2] = 0f
        matrix[1]!![3] = 0f
        matrix[2]!![0] = 0f
        matrix[2]!![1] = 0f
        matrix[2]!![2] = z
        matrix[2]!![3] = 0f
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun initPerspective(fov: Float, aspectRatio: Float, zNear: Float, zFar: Float): Matrix4f {
        val tanHalfFOV = tan((fov / 2).toDouble()).toFloat()
        val zRange = zNear - zFar

        matrix[0]!![0] = 1.0f / (tanHalfFOV * aspectRatio)
        matrix[0]!![1] = 0f
        matrix[0]!![2] = 0f
        matrix[0]!![3] = 0f
        matrix[1]!![0] = 0f
        matrix[1]!![1] = 1.0f / tanHalfFOV
        matrix[1]!![2] = 0f
        matrix[1]!![3] = 0f
        matrix[2]!![0] = 0f
        matrix[2]!![1] = 0f
        matrix[2]!![2] = (-zNear - zFar) / zRange
        matrix[2]!![3] = 2 * zFar * zNear / zRange
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 1f
        matrix[3]!![3] = 0f


        return this
    }

    fun initOrthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
        val width = right - left
        val height = top - bottom
        val depth = far - near

        matrix[0]!![0] = 2 / width
        matrix[0]!![1] = 0f
        matrix[0]!![2] = 0f
        matrix[0]!![3] = -(right + left) / width
        matrix[1]!![0] = 0f
        matrix[1]!![1] = 2 / height
        matrix[1]!![2] = 0f
        matrix[1]!![3] = -(top + bottom) / height
        matrix[2]!![0] = 0f
        matrix[2]!![1] = 0f
        matrix[2]!![2] = -2 / depth
        matrix[2]!![3] = -(far + near) / depth
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun initRotation(forward: Vector4f, up: Vector4f): Matrix4f {
        val f: Vector4f = forward.normalise()

        var r: Vector4f = up.normalise()
        r = r.cross(f)

        val u: Vector4f = f.cross(r)

        return initRotation(f, u, r)
    }


    fun initRotation(forward: Vector4f, up: Vector4f, right: Vector4f): Matrix4f {
        val f: Vector4f = forward
        val r: Vector4f = right
        val u: Vector4f = up

        matrix[0]!![0] = r.x
        matrix[0]!![1] = r.y
        matrix[0]!![2] = r.z
        matrix[0]!![3] = 0f
        matrix[1]!![0] = u.x
        matrix[1]!![1] = u.y
        matrix[1]!![2] = u.z
        matrix[1]!![3] = 0f
        matrix[2]!![0] = f.x
        matrix[2]!![1] = f.y
        matrix[2]!![2] = f.z
        matrix[2]!![3] = 0f
        matrix[3]!![0] = 0f
        matrix[3]!![1] = 0f
        matrix[3]!![2] = 0f
        matrix[3]!![3] = 1f

        return this
    }

    fun transform(r: Vector4f): Vector4f {
        return Vector4f(
            matrix[0]!![0] * r.x + matrix[0]!![1] * r.y + matrix[0]!![2] * r.z + matrix[0]!![3] * r.w,
            matrix[1]!![0] * r.x + matrix[1]!![1] * r.y + matrix[1]!![2] * r.z + matrix[1]!![3] * r.w,
            matrix[2]!![0] * r.x + matrix[2]!![1] * r.y + matrix[2]!![2] * r.z + matrix[2]!![3] * r.w,
            matrix[3]!![0] * r.x + matrix[3]!![1] * r.y + matrix[3]!![2] * r.z + matrix[3]!![3] * r.w
        )
    }

    fun Mul(r: Matrix4f): Matrix4f {
        val res = Matrix4f()

        for (i in 0..3) {
            for (j in 0..3) {
                res.set(
                    i, j, matrix[i]!![0] * r.get(0, j) + matrix[i]!![1] * r.get(1, j) + matrix[i]!![2] * r.get(
                        2, j
                    ) + matrix[i]!![3] * r.get(
                        3, j
                    )
                )
            }
        }

        return res
    }

    fun getM(): Array<FloatArray?> {
        val res = Array<FloatArray?>(4) { FloatArray(4) }

        for (i in 0..3) for (j in 0..3) res[i]!![j] = matrix[i]!![j]

        return res
    }

    fun get(x: Int, y: Int): Float {
        return matrix[x]!![y]
    }

    fun setM(m: Array<FloatArray?>) {
        this.matrix = m
    }

    fun set(x: Int, y: Int, value: Float) {
        matrix[x]!![y] = value
    }
}