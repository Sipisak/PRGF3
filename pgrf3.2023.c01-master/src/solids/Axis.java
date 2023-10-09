package solids;

public class Axis extends Solid {
    public Axis(float x, float y) {
        // vertex buffer
        float[] vertices = {
                0.f,  0.f,
                x,  y
        };

        // index buffer
        int[] indices = {
                0, 1
        };

        // popsat kartě data
        lwjglutils.OGLBuffers.Attrib[] attributes = {
                new lwjglutils.OGLBuffers.Attrib("inPosition", 2), // 2 floats
        };

        buffers = new lwjglutils.OGLBuffers(vertices, attributes, indices);
    }
}
