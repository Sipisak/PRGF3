package solids;
import lwjglutils.OGLBuffers;

public class Sphere extends Solid {

    /**
     * @param stacks - počet "stacks" koulí (vertikální podělení)
     * @param slices - počet "slices" koulí (horizontální podělení)
     */
    public Sphere(final int stacks, final int slices) {
        // Počet vrcholů a indexů
        int numVertices = (stacks + 1) * (slices + 1);
        int numIndices = 6 * stacks * slices;

        // Vytvoření pole pro vrcholy a indexy
        float[] vertices = new float[numVertices * 3];
        int[] indices = new int[numIndices];

        // Generace vrcholů
        int vertexIndex = 0;
        for (int i = 0; i <= stacks; i++) {
            double phi = Math.PI * (-0.5 + (double) (i) / stacks);
            for (int j = 0; j <= slices; j++) {
                double theta = 2.0 * Math.PI * (double) (j) / slices;
                float x = (float) (Math.cos(theta) * Math.sin(phi));
                float y = (float) Math.cos(phi);
                float z = (float) (Math.sin(theta) * Math.sin(phi));
                vertices[vertexIndex++] = x;
                vertices[vertexIndex++] = y;
                vertices[vertexIndex++] = z;
            }
        }

        // Generace indexů
        int index = 0;
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int p0 = i * (slices + 1) + j;
                int p1 = p0 + slices + 1;
                indices[index++] = p0;
                indices[index++] = p1;
                indices[index++] = p0 + 1;

                indices[index++] = p1;
                indices[index++] = p1 + 1;
                indices[index++] = p0 + 1;
            }
        }

        lwjglutils.OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3)
        };

        buffers = new OGLBuffers(vertices, attributes, indices);
    }

}
