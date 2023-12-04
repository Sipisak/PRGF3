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

import java.awt.*;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL20.*;


/**
 *
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer{

	private int shaderProgamTriangle, shaderProgamAxis, shaderProgamGrid, shaderProgramLight, shaderProgamCylinder, shaderProgamDome, shaderProgamDonut, shaderProgamElephant,shaderProgamExplosion,shaderProgamSphere,shaderProgamMorph, shaderProgamCube;
	private Triangle triangle;
	private Axis axisX, axisY, axisZ;
	private Grid grid, light, elephant, explosion, dome, donut,cylinder, morph, sphere;
	private Cube cube;

	private Camera camera;
	private Mat4 projection;
	private float time;
	private Vec3D lightPosition;
	private Vec3D lightDir;
	private Vec3D viewDir;
	private	OGLTexture2D texture;
	private double ox, oy;
	private boolean mouseButton1 = false;
	private Mat4 model = new Mat4Identity();
	private int currentObjectIndex = 0;
	private List<Runnable> renderTasks = new ArrayList<>();
	Vec3D objectPosition = new Vec3D(0.0f, 0.0f, 0.0f);




	@Override
	public void init() {
		triangle = new Triangle();
		shaderProgamTriangle = lwjglutils.ShaderUtils.loadProgram("/triangle");

		axisX = new Axis(1.f, 0.f, 0.f, new Col(1.f, 0.f, 0.f));
		axisY = new Axis(0.f, 1.f, 0.f, new Col(0.f, 1.f, 0.f));
		axisZ = new Axis(0.f, 0.f, 1.f, new Col(0.f, 0.f, 1.f));
		shaderProgamAxis = lwjglutils.ShaderUtils.loadProgram("/axis");

		grid = new Grid(50, 50);
		shaderProgamGrid = lwjglutils.ShaderUtils.loadProgram("/grid");
		cylinder = new Grid(50, 50);
		shaderProgamCylinder = lwjglutils.ShaderUtils.loadProgram("/cylinder");
		dome = new Grid(50, 50);
		shaderProgamDome = lwjglutils.ShaderUtils.loadProgram("/dome");
		donut = new Grid(50, 50);
		shaderProgamDonut = lwjglutils.ShaderUtils.loadProgram("/donut");
		elephant = new Grid(50, 50);
		shaderProgamElephant = lwjglutils.ShaderUtils.loadProgram("/elephant");
		explosion = new Grid(50, 50);
		shaderProgamExplosion = lwjglutils.ShaderUtils.loadProgram("/explosion");
		sphere = new Grid(50, 50);
		shaderProgamSphere = lwjglutils.ShaderUtils.loadProgram("/sphere");
		morph = new Grid(50, 50);
		shaderProgamMorph = lwjglutils.ShaderUtils.loadProgram("/morph");
		cube = new Cube();
		shaderProgamCube = lwjglutils.ShaderUtils.loadProgram("/cube");

		// cam, proj
		camera = new Camera()
				.withPosition(new Vec3D(-1.5f, -1.5f, 3.f))
				.withAzimuth(Math.toRadians(45))
				.withZenith(Math.toRadians(-45))

				.withFirstPerson(true);
		projection = new Mat4PerspRH(Math.PI / 4, height / (float)width, 0.1f, 100.f);

		// texture
		try {
			texture = new OGLTexture2D("textures/bricks.jpg");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// světlo
		light = new Grid(2, 2);
		shaderProgramLight = ShaderUtils.loadProgram("/light");
		lightPosition = new Vec3D(0.f, 0.f, 1000.1f);
		lightDir = new Vec3D(0.f,0.f,100.f);
		viewDir = new Vec3D(0.f,0.f,100.f);

		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		renderTasks.addAll(
				Arrays.asList(
				this::drawAxis,
				this::drawGrid,
				this::drawTriangle,
				this::drawCylinder,
				this::drawDome,
				this::drawDonut,
				this::drawExplosion,
				this::drawElephant,
				this::drawSphere,
				this::drawMorph,
				this::drawCube
		));
		glfwSetKeyCallback(window, keyCallback);

	}

	private void glfwSetKeyCallback(SystemColor window, GLFWKeyCallback keyCallback) {
	}

	@Override
	public void display() {
		time += 0.01f;

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

		renderTasks.get(currentObjectIndex).run();

		glfwSwapBuffers(window);
		glfwPollEvents();

		Vec3D forward = camera.getForward().mul(0.1f);
		objectPosition = objectPosition.add(forward);
		lightPosition = objectPosition;
		drawLight();

	}

	private void glfwSwapBuffers(SystemColor window) {
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
		texture.bind(shaderProgamGrid, "textureBricks");

		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamGrid);
	}
	private void drawCylinder() {
		glUseProgram(shaderProgamCylinder);
		setGlobalUniforms(shaderProgamCylinder);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamCylinder);
	}
	private void drawCube() {
		glUseProgram(shaderProgamCube);
		setGlobalUniforms(shaderProgamCube);
		cube.getBuffers().draw(GL_TRIANGLES, shaderProgamCube);
	}

	private void drawDome() {
		glUseProgram(shaderProgamDome);
		setGlobalUniforms(shaderProgamDome);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamDome);
	}
	private void drawDonut() {
		glUseProgram(shaderProgamDonut);
		setGlobalUniforms(shaderProgamDonut);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamDonut);
	}
	private void drawElephant() {
		glUseProgram(shaderProgamElephant);
		setGlobalUniforms(shaderProgamElephant);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamElephant);
	}
	private void drawExplosion() {
		glUseProgram(shaderProgamExplosion);
		setGlobalUniforms(shaderProgamExplosion);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamExplosion);
	}
	private void drawSphere() {
		glUseProgram(shaderProgamSphere);
		setGlobalUniforms(shaderProgamSphere);
		texture.bind(shaderProgamSphere, "textureEarth");
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamSphere);
	}
	private void drawMorph() {
		glUseProgram(shaderProgamMorph);
		setGlobalUniforms(shaderProgamMorph);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamMorph);
	}



	private void drawLight() {
		glUseProgram(shaderProgramLight);
		setGlobalUniforms(shaderProgramLight);
		light.getBuffers().draw(GL_TRIANGLES, shaderProgramLight);
	}

	private void setGlobalUniforms(int shaderProgram) {
		int locUView = glGetUniformLocation(shaderProgram, "uView");
		int locUProj = glGetUniformLocation(shaderProgram, "uProj");
		int locUTime = glGetUniformLocation(shaderProgamMorph, "uTime");
		int locULightPos = glGetUniformLocation(shaderProgamMorph, "u_LightPos");
		int locUTransform = glGetUniformLocation(shaderProgram, "uTransform");
		int locLightDir = glGetUniformLocation(shaderProgram, "lightDir");
		glUniform3f(locLightDir, (float)lightDir.getX(), (float)lightDir.getY(), (float)lightDir.getZ());
		int locViewDir = glGetUniformLocation(shaderProgram, "viewDir");
		glUniform3f(locViewDir, (float)viewDir.getX(), (float)viewDir.getY(), (float)viewDir.getZ());
		glUniform3f(locULightPos, (float)lightPosition.getX(), (float)lightPosition.getY(), (float)lightPosition.getZ());
		glUniform1f(locUTime, time);
		glUniformMatrix4fv(locUView, false, camera.getViewMatrix().floatArray());
		glUniformMatrix4fv(locUProj, false, projection.floatArray());
		glUniformMatrix4fv(locUTransform, false, model.floatArray());
	}

	private GLFWKeyCallback   keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			switch (key) {
				case GLFW_KEY_LEFT->
						objectPosition = objectPosition.add(new Vec3D(-0.1f, 0, 0));
				case GLFW_KEY_RIGHT->
						objectPosition = objectPosition.add(new Vec3D(0.1f, 0, 0));
				case GLFW_KEY_UP->
						objectPosition = objectPosition.add(new Vec3D(0, 0.1f, 0));
				case GLFW_KEY_DOWN->
						objectPosition = objectPosition.add(new Vec3D(0, -0.1f, 0));
				case GLFW_KEY_W -> // W
						camera = camera.forward(0.1f);
				case GLFW_KEY_A -> // A
						camera = camera.left(0.1f);
				case GLFW_KEY_S -> // S
						camera = camera.backward(0.1f);
				case GLFW_KEY_D -> // D
						camera = camera.right(0.1f);
				case GLFW_KEY_Q -> // Q
						camera = camera.up(0.1f);
				case GLFW_KEY_E -> // E
						camera = camera.down(0.1f);

				case GLFW_KEY_O -> {
				}
				case GLFW_KEY_P -> {
					 }
			}

			if (action == GLFW_PRESS) {
				switch (key) {

					case GLFW_KEY_Z -> {}
					case GLFW_KEY_C -> {}
					case GLFW_KEY_B -> // B
							glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
					case GLFW_KEY_N -> // N
							glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
					case GLFW_KEY_M -> { // M
						glEnable(GL_POINT_SMOOTH);
						glPointSize(5.0f);
						glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);

					}
					case GLFW_KEY_SPACE -> {
						renderNextObject();
						break;
					}
				}

			}
		}
	};
	private void renderNextObject() {
		// Update the current object index for the next key press
		currentObjectIndex = (currentObjectIndex + 1) % renderTasks.size();
	}

	private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
		@Override
		public void invoke(long window, int w, int h) {
		}
	};

	private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback() {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

			if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
				mouseButton1 = true;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				ox = xBuffer.get(0);
				oy = yBuffer.get(0);
			}

			if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
				mouseButton1 = false;
				DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
				DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
				glfwGetCursorPos(window, xBuffer, yBuffer);
				double x = xBuffer.get(0);
				double y = yBuffer.get(0);
				camera = camera.addAzimuth((double) Math.PI * (ox - x) / width)
						.addZenith((double) Math.PI * (oy - y) / width);

				ox = x;
				oy = y;
			}
		}

	};

	private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
		@Override
		public void invoke(long window, double x, double y) {
			if (mouseButton1) {
				camera = camera.addAzimuth(Math.PI * (ox - x) / width)
						.addZenith(Math.PI * (oy - y) / width);

				ox = x;
				oy = y;
			}
		}
	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
		@Override
		public void invoke(long window, double dx, double dy) {
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