import solids.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

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

	private int shaderProgamTriangle, shaderProgamAxis; shaderProgamGrid;
	private Triangle triangle;
	private Axis axis;


    @Override
    public void init() {
		triangle = new Triangle();
		shaderProgamTriangle = lwjglutils.ShaderUtils.loadProgram("/triangle");

		axis = new Axis(1.f, 0.f);
		shaderProgamAxis = lwjglutils.ShaderUtils.loadProgram("/axis");

		grid = new Grid(15,15);
		shaderProgamGrid = lwjglutils.ShaderUtils.loadProgram("/grid");
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
		triangle.getBuffers().draw(GL_TRIANGLES, shaderProgamTriangle);
	}

	private void drawAxis() {
		glUseProgram(shaderProgamAxis);
		axis.getBuffers().draw(GL_LINES, shaderProgamAxis);
	}
	private void drawGrid() {
		glUseProgram(shaderProgamGrid);
		grid.getBuffers().draw(GL_TRIANGLES, shaderProgamGrid);
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