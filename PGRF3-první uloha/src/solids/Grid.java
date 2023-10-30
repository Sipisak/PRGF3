package solids;

import lwjglutils.OGLBuffers;

public class Grid extends Solid {
    private int shaderProgram;

    /**
     * @param m - počet vrcholů v řádku
     * @param n - počet vrcholů ve sloupci
     */
    public Grid(final int m, final int n) {
        // vb
        float[] vb = new float[2 * m * n];
        // ib
        int[] ib = new int[3 * 2 * (m - 1) * (n - 1)];

        //generování vb
        int index = 0;
        // rozsah od 0 od 1
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                vb[index++] = j / (float) (m - 1);
                vb[index++] = i / (float) (n - 1);
            }
        }

        index = 0;
        for (int i = 0; i < n - 1; i++) {
            int offset = i * m;
            for (int j = 0; j < m - 1; j++) {
                // j=0, m=4
                ib[index++] = j + offset;
                ib[index++] = j + m + offset;
                ib[index++] = j + 1 + offset;

                ib[index++] = j + 1 + offset;
                ib[index++] = j + m + offset;
                ib[index++] = j + m + 1 + offset;

            }
        }

        lwjglutils.OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 2)
        };

        buffers = new OGLBuffers(vb, attributes, ib);
    }
}
