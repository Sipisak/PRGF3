import lwjglutils.OGLTexture2D;
import lwjglutils.ShaderUtils;
import org.lwjgl.opengl.GL11;
import solids.Axis;
import solids.Solid;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Vec3D;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class SPRenderer {

    private final Solid solid;
    private final String shaderProgramFileName;
    private int shaderProgram;
    private boolean isLines = false;
    private Camera camera;
    private Mat4 proj;
    private Vec3D lightPosition;
    private Mat4 transform = new Mat4Identity();
    private float time = 0.01f;
    private OGLTexture2D texture = null;


    public SPRenderer(Solid solid, String shaderProgramFileName, Camera camera, Mat4 proj, String textureFileName) {
        this.solid = solid;
        this.shaderProgramFileName = shaderProgramFileName;
        this.camera = camera;
        this.proj = proj;
        try {
            texture = new OGLTexture2D("textures/" + textureFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }
    public SPRenderer(Solid solid, String shaderProgramFileName, Camera camera, Mat4 proj) {
        this.solid = solid;
        this.shaderProgramFileName = shaderProgramFileName;
        this.camera = camera;
        this.proj = proj;
    }

    public SPRenderer(Solid solid, String shaderProgramFileName, Camera camera, Mat4 projection, Mat4 transform) {
        this.solid = solid;
        this.shaderProgramFileName = shaderProgramFileName;
        this.camera = camera;
        this.proj = projection;
        this.transform = transform;
    }



    public void init() {
        shaderProgram = ShaderUtils.loadProgram("/" + shaderProgramFileName);
        if (solid instanceof Axis) {
            useLines();
        }
        if (texture != null){
            texture.bind(shaderProgram, "texture" + shaderProgram,0);
        }
    }

    public void useLines() {
        isLines = true;
    }

    public void render() {
        glUseProgram(shaderProgram);
        setUniformTransform();
        int renderingMode = isLines ? GL_LINES : GL_TRIANGLES;
        solid.getBuffers().draw(renderingMode, shaderProgram);
    }

    private void setUniformTransform() {
        int locView = glGetUniformLocation(shaderProgram, "uView");
        int locUProj = glGetUniformLocation(shaderProgram, "uProj");
        int locUTransform = glGetUniformLocation(shaderProgram, "uTransform");
        int locUTime = glGetUniformLocation(shaderProgram, "uTime");

        glUniformMatrix4fv(locView, false, camera.getViewMatrix().floatArray());
        glUniformMatrix4fv(locUProj, false, proj.floatArray());
        glUniformMatrix4fv(locUTransform, false, transform.floatArray());
        glUniform1f(locUTime, time);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setTransform(Mat4 transform) {
        this.transform = transform;
    }
    public Mat4 getTransform() {
        return transform;
    }

    public void setTime(float time) {
        this.time += 0.01f;
    }
}
