package solids;

import lwjglutils.OGLBuffers;
import transforms.Col;

public class Axis extends Solid {
    public Axis(float x, float y, float z, Col color) {
        // vertex buffer
        float[] vertices = {
                0.f, 0.f, 0.f,
                x, y, z,
        };

        // index buffer
        int[] indices = {
                0, 1
        };

        // popsat kartě data
        lwjglutils.OGLBuffers.Attrib[] attributes = {
                new lwjglutils.OGLBuffers.Attrib("inPosition", 2), // 2 floats
                new lwjglutils.OGLBuffers.Attrib("inPosition", 3)
        };

        buffers = new lwjglutils.OGLBuffers(vertices, attributes, indices);
    }
}