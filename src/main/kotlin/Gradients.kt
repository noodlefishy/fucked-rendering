package io.cuttlefish

class Gradients(miny: Vertex, midy: Vertex, maxy: Vertex) {
    val colour = arrayOf(miny.colour, midy.colour, maxy.colour)

    //    private val oneOverDX =
//        1f / ((miny.pos.x - maxy.pos.x) * (miny.pos.y - maxy.pos.y)) - ((miny.pos.x - maxy.pos.x) * (midy.pos.y - maxy.pos.y))
    private val oneOverDX = 1f /
            (((midy.x - maxy.x) * (miny.y - maxy.y)) - ((miny.x - maxy.x) * (midy.y - maxy.y)))


    val oneOverDY = oneOverDX.unaryMinus()
    private val dColour =
        ((colour[1] - colour[2]) * (miny.pos.y - maxy.pos.y)) - ((colour[0] - colour[2]) * (midy.pos.y - maxy.pos.y))

    val colourXStep =
        (((colour[1].minus(colour[2])).times(
            r = (miny.y - maxy.y)
        )).minus(
            r = ((colour[0].minus(colour[2])).times(
                r = (midy.y - maxy.y)
            ))
        )).times(oneOverDX)

    val colourYStep = (((colour[1].minus(colour[2])).times(
        (miny.x - maxy.x)
    )).minus(
        ((colour[0].minus(colour[2])).times(
            (midy.x - maxy.x)
        ))
    )).times(oneOverDY)

}