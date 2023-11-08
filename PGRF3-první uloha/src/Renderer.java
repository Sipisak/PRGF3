import lwjglutils.OGLTexture2D;
import lwjglutils.ShaderUtils;
import org.lwjgl.BufferUtils;
import solids.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.*;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL20.*;

public class Renderer extends AbstractRenderer{

	private int shaderProgamTriangle, shaderProgamAxis, shaderProgamGrid, shaderProgamSphere, shaderProgamLight;

	private Triangle triangle;
	private Axis axisX, axisY, axisZ;
	private Grid grid, light, sphere;

	private List<SPRenderer> spRenderers = new ArrayList<>();
	private Camera camera;
	private Mat4 proj;
	private float time;
	private boolean timeUp = true;
	private double ox, oy;
	private boolean mouseButton1 = false;
	private OGLTexture2D texture;
	private Vec3D lightPosition;

	@Override
	public void init() {

		camera = new Camera()
				.withPosition(new Vec3D(-2.f ,-2.5f, 3.f))
				.withAzimuth(Math.toRadians(45))
				.withZenith(Math.toRadians(-40))
				.withFirstPerson(true);
		proj = new Mat4PerspRH(Math.PI / 4 ,height / (float)width, 0.1f, 100.f);

		spRenderers.addAll(
				Arrays.asList(
						new SPRenderer(new Triangle(), "triangle", camera, proj),
						new SPRenderer(new Grid(25, 25), "sphere", camera, proj),
						new SPRenderer(new Grid(10, 10), "light",camera,proj),
						new SPRenderer(new Grid(50, 50), "grid", camera, proj,"bricks.jpg"),
//						new SPRenderer(new Grid(25, 30), "elephant", camera, proj),
//						new SPRenderer(new Grid(30, 30), "donut", camera, proj),
//						new SPRenderer(new Grid(30, 30), "spider", camera, proj),
//						new SPRenderer(new Grid(20, 20), "pipe", camera, proj),
//						new SPRenderer(new Grid(25, 25), "morph", camera, proj),
						new SPRenderer(new Axis(1.f, 0.f, 0.f, new Col(1.f, 0, 0)), "axis", camera, proj),
						new SPRenderer(new Axis(0.f, 1.f, 0.f, new Col(0, 1.f, 0)), "axis", camera, proj),
						new SPRenderer(new Axis(0.f, 0.f, 1.f, new Col(0, 0, 1.f)), "axis", camera, proj)
				)
		);
		sphere = new Grid(25,25);
		shaderProgamSphere = ShaderUtils.loadProgram("/sphere");

		light = new Grid(10,10);
		shaderProgamLight = ShaderUtils.loadProgram("/light");
		lightPosition = new Vec3D(0.f, 0.f, 1000.1f);


		spRenderers.get(0).setTransform(new Mat4Transl(5, 0, 3));
		spRenderers.get(1).setTransform(new Mat4Transl(10, 5, 0));
		spRenderers.get(2).setTransform(new Mat4Transl(5, 10, 0));
		spRenderers.get(3).setTransform(new Mat4Transl(10, 5, 0));
		spRenderers.get(4).setTransform(new Mat4Transl(10, 0, 2));
		spRenderers.get(5).setTransform(new Mat4Transl(5, 10, 2));
		
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	@Override
	public void display() {

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//		drawTriangle();
//		drawLight();
//		drawAxis();
//		drawGrid();

		uTime();
		spRenderers.forEach(r -> r.setTime(time));
		spRenderers.forEach(SPRenderer::render);

	}
	private void uTime() {
		float step = 0.01f;
		time += timeUp ? step : -step;

		if (time > 1.f || time < 0.f) {
			timeUp = !timeUp;
			time = Math.min(1.f, Math.max(0.f, time));
		}
	}


//	private void drawTriangle() {
//		glUseProgram(shaderProgamTriangle);
//		setGlobalUniforms(shaderProgamTriangle);
//		triangle.getBuffers().draw(GL_TRIANGLES, shaderProgamTriangle);
//	}
//
//	private void drawAxis() {
//		glUseProgram(shaderProgamAxis);
//		setGlobalUniforms(shaderProgamAxis);
//		axisX.getBuffers().draw(GL_LINES, shaderProgamAxis);
//		axisY.getBuffers().draw(GL_LINES, shaderProgamAxis);
//		axisZ.getBuffers().draw(GL_LINES, shaderProgamAxis);
//	}
//	private void drawGrid() {
//		glUseProgram(shaderProgamGrid);
//		setGlobalUniforms(shaderProgamGrid);
//		int locUTime = glGetUniformLocation(shaderProgamGrid, "uTime");
//		glUniform1f(locUTime,time);
//		texture.bind(shaderProgamGrid, "textureBricks");
//		int locULightPos = glGetUniformLocation(shaderProgamGrid, "u_LightPos");
//		glUniform3f(locULightPos, (float)lightPosition.getX(), (float)lightPosition.getY(), (float)lightPosition.getZ());
//		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamGrid);
//	}
//	private void drawLight() {
//		glUseProgram(shaderProgamLight);
//		setGlobalUniforms(shaderProgamLight);
//		light.getBuffers().draw(GL_TRIANGLES, shaderProgamLight);
//	}
//
//	private void setGlobalUniforms(int shaderProgram) {
//		int locUView = glGetUniformLocation(shaderProgram, "uView");
//		int locUProj = glGetUniformLocation(shaderProgram, "uProj");
//		glUniformMatrix4fv(locUView, false, camera.getViewMatrix().floatArray());
//		glUniformMatrix4fv(locUProj, false, proj.floatArray());


	private GLFWKeyCallback  keyCallback = new GLFWKeyCallback() {
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
				case GLFW_KEY_O -> { // O
					for (SPRenderer spRenderer : spRenderers) {
						spRenderer.setTransform(spRenderer.getTransform().mul(new Mat4RotX(0.1f)));
					}
				}
				case GLFW_KEY_P -> { // P
					for (SPRenderer spRenderer : spRenderers) {
						spRenderer.setTransform(spRenderer.getTransform().mul(new Mat4RotX(-0.1f)));
					}
				}
			}
			spRenderers.forEach(r -> r.setCamera(camera));
			if (action == GLFW_PRESS) {
				switch (key) {
					case GLFW_KEY_Z -> { // Y
						proj = new Mat4OrthoRH(Math.PI / 4, height / (float) width, 0.f, 100);
						spRenderers.forEach(r -> r.setProj(proj));
					}
					case GLFW_KEY_C -> { // C
						proj = new Mat4PerspRH(Math.PI / 4, height / (float) width, 0.f, 100);
						spRenderers.forEach(r -> r.setProj(proj));
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
			mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

			if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
				mouseButton1 = true;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				ox = xBuffer.get(0);
				oy = yBuffer.get(0);
			}

			if (button==GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE){
				mouseButton1 = false;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				double x = xBuffer.get(0);
				double y = yBuffer.get(0);
				camera = camera.addAzimuth((double) Math.PI * (ox - x) / width)
        				.addZenith((double) Math.PI * (oy - y) / width);
				spRenderers.forEach(r->r.setCamera(camera));
				ox = x;
				oy = y;
        	}
		}

	};

	private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
		@Override
		public void invoke(long window, double x, double y) {
			if (mouseButton1) {
				camera = camera.addAzimuth((double) Math.PI * (ox - x) / width)
						.addZenith((double) Math.PI * (oy - y)  / width);
				spRenderers.forEach(r->r.setCamera(camera));
				ox = x;
				oy = y;


    		}
		}

	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		@Override public void invoke (long window, double dx, double dy) {
			  if (dy<0)
        	camera = camera.mulRadius(0.9f);
        else
        	camera = camera.mulRadius(1.1f);
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
