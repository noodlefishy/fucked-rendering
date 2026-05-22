package io.cuttlefish

class Gradients(miny: Vertex, midy: Vertex, maxy: Vertex) {
    val textureCords = arrayOf(miny.textureCords, midy.textureCords, maxy.textureCords)

    //    private val oneOverDX =
//        1f / ((miny.pos.x - maxy.pos.x) * (miny.pos.y - maxy.pos.y)) - ((miny.pos.x - maxy.pos.x) * (midy.pos.y - maxy.pos.y))
    private val oneOverDX = 1f /
            (((midy.x - maxy.x) * (miny.y - maxy.y)) - ((miny.x - maxy.x) * (midy.y - maxy.y)))


    val oneOverDY = oneOverDX.unaryMinus()
    private val dColour =
        ((textureCords[1] - textureCords[2]) * (miny.pos.y - maxy.pos.y)) - ((textureCords[0] - textureCords[2]) * (midy.pos.y - maxy.pos.y))

    val colourXStep =
        (((textureCords[1].minus(textureCords[2])).times(
            r = (miny.y - maxy.y)
        )).minus(
            r = ((textureCords[0].minus(textureCords[2])).times(
                r = (midy.y - maxy.y)
            ))
        )).times(oneOverDX)

    val colourYStep = (((textureCords[1].minus(textureCords[2])).times(
        (miny.x - maxy.x)
    )).minus(
        ((textureCords[0].minus(textureCords[2])).times(
            (midy.x - maxy.x)
        ))
    )).times(oneOverDY)

}