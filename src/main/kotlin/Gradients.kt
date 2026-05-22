package io.cuttlefish

class Gradients(miny: Vertex, midy: Vertex, maxy: Vertex) {
    val colour = arrayOf(miny.colour, midy.colour, maxy.colour)

    private val oneOverDX =
        1f / ((miny.pos.x - maxy.pos.x) * (miny.pos.y - maxy.pos.y)) - ((miny.pos.x - maxy.pos.x) * (midy.pos.y - maxy.pos.y))
    private val oneOverDY = oneOverDX.unaryMinus()
    private val dColour =
        ((colour[1] - colour[2]) * (miny.pos.y - maxy.pos.y)) - ((colour[0] - colour[2]) * (midy.pos.y - maxy.pos.y))

     val colourXStep = dColour / oneOverDX
     val colourYStep = dColour / oneOverDY

}