import solids.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.Camera;
import transforms.Col;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL20.*;

public class Renderer extends AbstractRenderer{

	private int shaderProgamTriangle, shaderProgamAxis, shaderProgamGrid;
	private Triangle triangle;
	private Axis axisX, axisY, axisZ;
	private Grid grid;
	private Camera camera;
	private Mat4PerspRH project;




    @Override
    public void init() {
		triangle = new Triangle();
		shaderProgamTriangle = lwjglutils.ShaderUtils.loadProgram("/triangle");

		axisX = new Axis(1.f, 1.f, 1.f,new Col(1.f, 0.f, 0.f));
		axisY = new Axis(0.f, 1.f, 1.f,new Col(0.f,1.f,0.f));
		axisZ = new Axis(0.f, 0.f, 1.f,new Col(0.f, 0.f, 0.f));
		shaderProgamAxis = lwjglutils.ShaderUtils.loadProgram("/axis");

		grid = new Grid(15,15);
		shaderProgamGrid = lwjglutils.ShaderUtils.loadProgram("/grid");

		//camera a projekce
		camera = new Camera()
				.withPosition(new Vec3D(-1.f ,-1.5f, 1.f))
				.withAzimuth(Math.toRadians(15))
				.withZenith(Math.toRadians(20))
				.withFirstPerson(true);
		project = new Mat4PerspRH(Math.PI / 4 ,height / (float)width, 0.1f, 1000);

		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    @Override
    public void display() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		//drawTriangle();
		//drawAxis();
		drawGrid();
	}

	private void drawTriangle() {
		glUseProgram(shaderProgamTriangle);
		setGlobalUniforms(shaderProgamTriangle);
		triangle.getBuffers().draw(GL_TRIANGLES, shaderProgamTriangle);
	}

	private void drawAxis() {
		glUseProgram(shaderProgamAxis);
		setGlobalUniforms(shaderProgamAxis);
		axisX.getBuffers().draw(GL_LINES, shaderProgamAxis);
		axisY.getBuffers().draw(GL_LINES, shaderProgamAxis);
		axisZ.getBuffers().draw(GL_LINES, shaderProgamAxis);
	}
	private void drawGrid() {
		glUseProgram(shaderProgamGrid);
		setGlobalUniforms(shaderProgamGrid);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamGrid);
	}

	private void setGlobalUniforms(int shaderProgram) {
		int locUView = glGetUniformLocation(shaderProgram, "uView");
		int locUProject = glGetUniformLocation(shaderProgram, "uProject");
		glUniformMatrix4fv(locUView, false, camera.getViewMatrix().floatArray());

	}
	private GLFWKeyCallback   keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
		}
	};
    
    private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
        }
    };
    
    private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback () {
		@Override
		public void invoke(long window, int button, int action, int mods) {
		}
		
	};
	
    private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
    	}
    };
    
    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override public void invoke (long window, double dx, double dy) {
        }
    };
 

	@Override
	public GLFWKeyCallback getKeyCallback() {
		return keyCallback;
	}

	@Override
	public GLFWWindowSizeCallback getWsCallback() {
		return wsCallback;
	}

	@Override
	public GLFWMouseButtonCallback getMouseCallback() {
		return mbCallback;
	}

	@Override
	public GLFWCursorPosCallback getCursorCallback() {
		return cpCallbacknew;
	}

	@Override
	public GLFWScrollCallback getScrollCallback() {
		return scrollCallback;
	}
}