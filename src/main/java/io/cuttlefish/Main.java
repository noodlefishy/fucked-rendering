package io.cuttlefish;

public class Main
{
    public static void main(String[] args)
    {
        Display display = new Display(800, 600, "Software Rendering");
        RenderContext target = display.GetFrameBuffer();

        Vertex minYVert = new Vertex(-1, -1, 0);
        Vertex midYVert = new Vertex(0, 1, 0);
        Vertex maxYVert = new Vertex(1, -1, 0);

        Matrix4f projection = new Matrix4f().initPerspective((float)Math.toRadians(70.0f),
                (float)target.getWidth()/(float)target.getHeight(), 0.1f, 1000.0f);

        float rotCounter = 0.0f;
        long previousTime = System.nanoTime();
        while(true)
        {
            long currentTime = System.nanoTime();
            float delta = (float)((currentTime - previousTime)/1000000000.0);
            previousTime = currentTime;

            rotCounter += delta;
            Matrix4f translation = new Matrix4f().initTranslation(0.0f, 0.0f, 3.0f);
            Matrix4f rotation = new Matrix4f().initRotation(0.0f, rotCounter, 0.0f);
            Matrix4f transform = projection.Mul(translation.Mul(rotation));

            target.clear((byte)0x00);
            target.fillTriangle(maxYVert.transform(transform),
                    midYVert.transform(transform), minYVert.transform(transform));

            display.swapBuffers();
        }
    }
}