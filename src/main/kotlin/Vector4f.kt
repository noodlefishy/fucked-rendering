package io.cuttlefish

import kotlin.math.*

class Vector4f
    (val x: Float, val y: Float, val z: Float, val w: Float) {
    fun length(): Float {
        return sqrt(x = (x * x + y * y + z * z + w * w).toDouble()).toFloat()
    }

    fun max(): Float {
        return max(a = max(x, y), b = max(z, w))
    }

    fun dot(r: Vector4f): Float {
        return x * r.x + y * r.y + z * r.z + w * r.w
    }

    fun cross(r: Vector4f): Vector4f {
        val inX: Float = y * r.z - z * r.y
        val inY: Float = z * r.x - x * r.z
        val inZ: Float = x * r.y - y * r.x

        return Vector4f(x = inX, y = inY, z = inZ, w = 0f)
    }

    fun normalise(): Vector4f {
        val length: Float = length()

        return Vector4f(x = x / length, y = y / length, z = z / length, w = w / length)
    }

    fun rotate(axis: Vector4f, angle: Float): Vector4f {
        val sinAngle: Float = sin(x = -angle.toDouble()).toFloat()
        val cosAngle: Float = cos(x = -angle.toDouble()).toFloat()

        return this.cross(r = axis.times(r = sinAngle)).plus( //Rotation on local X
            (this.times(cosAngle)).plus( //Rotation on local Z
                axis.times(r = this.dot(r = axis.times(r = 1 - cosAngle)))
            )
        ) //Rotation on local Y
    }


    fun lerp(dest: Vector4f, lerpFactor: Float): Vector4f {
        return dest.minus(r = this).times(r = lerpFactor).plus(this)
    }

    operator fun plus(r: Vector4f): Vector4f {
        return Vector4f(x = x + r.x, y = y + r.y, z = z + r.z, w = w + r.w)
    }

    operator fun plus(r: Float): Vector4f {
        return Vector4f(x = x + r, y = y + r, z = z + r, w = w + r)
    }

    operator fun minus(r: Vector4f): Vector4f {
        return Vector4f(x = x - r.x, y = y - r.y, z = z - r.z, w = w - r.w)
    }

    operator fun minus(r: Float): Vector4f {
        return Vector4f(x = x - r, y = y - r, z = z - r, w = w - r)
    }

    operator fun times(r: Vector4f): Vector4f {
        return Vector4f(x = x * r.x, y = y * r.y, z = z * r.z, w = w * r.w)
    }

    operator fun times(r: Float): Vector4f {
        return Vector4f(x = x * r, y = y * r, z = z * r, w = w * r)
    }

    operator fun div(r: Vector4f): Vector4f {
        return Vector4f(x / r.x, y / r.y, z / r.z, w / r.w)
    }

    operator fun div(r: Float): Vector4f {
        return Vector4f(x / r, y / r, z / r, w / r)
    }

    fun abs(): Vector4f {
        return Vector4f(x = abs(x), y = abs(y), z = abs(z), w = abs(w))
    }

    override fun toString(): String {
        return "($x, $y, $z, $w)"
    }


    override fun equals(other: Any?): Boolean {
        if (other !is Vector4f) throw IllegalStateException("`other` should be a Vector4f not ${other!!::class.qualifiedName}")
        return x == other.x && y == other.y && z == other.z && w == other.w
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }
}