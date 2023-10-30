import solids.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.*;

import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL20.*;

public class Renderer extends AbstractRenderer{

	private int shaderProgamTriangle, shaderProgamAxis, shaderProgamGrid;
	//ArrayList<shaderProgamGrid> shaderProgamGridList = new ArrayList<>;
	private Triangle triangle;
	private Axis axisX, axisY, axisZ;
	private Grid grid;
	private Camera camera;
	private Mat4 proj;
	private float time;
	private double lastX = 0;
	private double lastY = 0;





    @Override
    public void init() {
		triangle = new Triangle();
		shaderProgamTriangle = lwjglutils.ShaderUtils.loadProgram("/triangle");

		axisX = new Axis(1.f, 0.f, 0.f,new Col(1.f, 0.f, 0.f));
		axisY = new Axis(0.f, 1.f, 0.f,new Col(0.f,1.f,0.f));
		axisZ = new Axis(0.f, 0.f, 1.f,new Col(0.f, 0.f, 1.f));
		shaderProgamAxis = lwjglutils.ShaderUtils.loadProgram("/axis");

		grid = new Grid(50,50);
		shaderProgamGrid = lwjglutils.ShaderUtils.loadProgram("/grid");

		//camera a projekce
		camera = new Camera()
				.withPosition(new Vec3D(-2.f ,-2.5f, 3.f))
				.withAzimuth(Math.toRadians(45))
				.withZenith(Math.toRadians(-40))
				.withFirstPerson(true);
		proj = new Mat4PerspRH(Math.PI / 4 ,height / (float)width, 0.1f, 100.f);

		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    @Override
    public void display() {
		time += 0.01f;

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
		int locUTime = glGetUniformLocation(shaderProgamGrid, "uTime");
		glUniform1f(locUTime,time);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamGrid);
	}

	private void setGlobalUniforms(int shaderProgram) {
		int locUView = glGetUniformLocation(shaderProgram, "uView");
		int locUProj = glGetUniformLocation(shaderProgram, "uProj");
		glUniformMatrix4fv(locUView, false, camera.getViewMatrix().floatArray());
		glUniformMatrix4fv(locUProj, false, proj.floatArray());

	}
	private GLFWKeyCallback   keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			switch (key) {
				case GLFW_KEY_W -> // W
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(0.05, 0, 0)));
				case GLFW_KEY_A -> // A
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(0, 0.05, 0)));
				case GLFW_KEY_S -> // S
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(-0.05, 0, 0)));
				case GLFW_KEY_D -> // D
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(0, -0.05, 0)));
				case GLFW_KEY_Q -> // Q
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(0, 0, 0.05)));
				case GLFW_KEY_E -> // E
						camera = camera.withPosition(camera.getPosition().add(new Vec3D(0, 0, -0.05)));
			}

			if (action == GLFW_PRESS) {
				switch (key) {
					case GLFW_KEY_Z -> { // Y
						proj = new Mat4OrthoRH(Math.PI / 4, height / (float) width, 0.f, 100);

					}
					case GLFW_KEY_C -> { // C
						proj = new Mat4PerspRH(Math.PI / 4, height / (float) width, 0.f, 100);

					}
					case GLFW_KEY_B -> // B
							glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
					case GLFW_KEY_N -> // N
							glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
					case GLFW_KEY_M -> { // M
						glEnable(GL_POINT_SMOOTH);
						glPointSize(5.0f);
						glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
					}
				}
				System.out.println("Key pressed " + key);
			}
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
			double deltaX = x - lastX;
			double deltaY = y - lastY;
			lastX = x;
			lastY = y;
			double azimuthChange = deltaX * 0.005;
			double zenithChange = deltaY * 0.005;
           // camera = camera.addAzimuth(azimuthChange);
           // camera = camera.addZenith(zenithChange);

           // System.out.println("Cursor position [" + x + ", " + y + "]");
			//renderedSolids.forEach(r -> r.setCamera(camera));
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
