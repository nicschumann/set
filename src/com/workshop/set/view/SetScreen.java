package com.workshop.set.view;

//import static org.lwjgl.opengl.GL11.glTranslatef;
import glfrontend.ScreenFrame;
import glfrontend.components.GLCamera;
//import glfrontend.components.GLComponent;
//import glfrontend.ScreenFrame;

//import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
//import static org.lwjgl.opengl.GL11.*;

//import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.glu.GLU;
//import org.lwjgl.BufferUtils;

public class SetScreen implements ScreenFrame {

	private Vector2f ul, lr;
	private Stage _stage; 
	private GLCamera _camera; 	

	private List<ScreenFrame> frames;
	private Map<ScreenFrame, Boolean> contained;

	public SetScreen(float w, float h) {
		init();
		setSize(new Vector2f(w, h));
		_camera = new GLCamera();
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
	}
	
	public void setStage(Stage s){
		_stage = s; 
		frames = new ArrayList<>();
		contained = new HashMap<>();
	}

	public void add(ScreenFrame frame) {
		frames.add(frame);
		contained.put(frame, false);
	}

	@Override
	public void setLocation(Vector2f loc) {
		Vector2f.sub(lr, ul, lr);
		ul = loc;
		Vector2f.add(ul, lr, lr);
	}

	@Override
	public Vector2f getLocation() {
		return ul;
	}

	@Override
	public void setSize(Vector2f size) {
		Vector2f.add(ul, size, lr);
	}

	@Override
	public Vector2f getSize() {
		Vector2f result = new Vector2f();
		Vector2f.sub(lr, ul, result);
		return result;
	}

	@Override
	public boolean contains(Vector2f p) {
		return false;
	}

	@Override
	public void mousePressed(Vector2f p, MouseButton button) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mousePressed(relativePoint, button);
			}
		}
	}

	@Override
	public void mouseReleased(Vector2f p, MouseButton button) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);
				frame.mouseReleased(relativePoint, button);
			}
		}
	}

	@Override
	public void mouseWheelScrolled(int amount) {
		
	    _camera.mouseWheel(amount);
	    this.render();
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void render() {
		
	    //itegrate rendering of ui elements
		//glTranslatef(ul.x, ul.y, 0);
		for (ScreenFrame frame : frames)
			frame.render();
		//glTranslatef(-ul.x, -ul.y, 0);
        
//		//draw white ground plane
//		glMatrixMode(GL_PROJECTION);
//	    glLoadIdentity();
//	    GLU.gluPerspective(55, (float)this.getSize().x / (float)this.getSize().y, (float)(0.01), (float)(1000));
//
//	    // set up modelview matrix
//	    glMatrixMode(GL_MODELVIEW);
//	    glLoadIdentity();
//	    _camera.multMatrix();
//
//	    // set up canvas
//	    glViewport(0, 0, (int)this.getSize().x, (int)this.getSize().y);
//	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//	    
//	    _stage.render(); 
//	    glPopMatrix();
	    
	}

	@Override
	public void mouseMoved(Vector2f p) {
		for (ScreenFrame frame : frames) {
			if (frame.contains(p)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				if (!contained.get(frame)) {
					frame.mouseEntered(relativePoint);
					contained.put(frame, true);
				}
				frame.mouseMoved(relativePoint);
			} else if (contained.get(frame)) {
				Vector2f relativePoint = new Vector2f();
				Vector2f.sub(p, frame.getLocation(), relativePoint);

				frame.mouseExited(relativePoint);
				contained.put(frame, false);
			}
		}
	}

	@Override
	public void mouseEntered(Vector2f p) {}

	@Override
	public void mouseExited(Vector2f p) {}

	@Override
	public void setResizeType(ResizeType type) {}

	@Override
	public ResizeType getResizeType() {
		return ResizeType.RATIO;
	}

	@Override
	public void resize(Vector2f newSize) {
		Vector2f size = new Vector2f();
		Vector2f.sub(lr, ul, size);

		for (ScreenFrame frame : frames) {
			Vector2f oldSize = frame.getSize();

			frame.resize(newRatio(oldSize, size, newSize));
		}

		Vector2f.add(ul, newSize, lr);
	}

	public static Vector2f newRatio(Vector2f oldTop, Vector2f oldBottom, Vector2f newBottom) {
		float x = (oldTop.x * newBottom.x) / oldBottom.x;
		float y = (oldTop.y * newBottom.y) / oldBottom.y;
		return new Vector2f(x, y);
	}

}
