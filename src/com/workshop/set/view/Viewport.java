package com.workshop.set.view;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import glfrontend.ScreenFrame;
import glfrontend.components.GLCamera;
import glfrontend.components.GeometricElement;
import glfrontend.components.Line;
import glfrontend.components.Point;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU; 
import org.lwjgl.util.glu.Quadric; 
import org.lwjgl.util.glu.Sphere; 
import static org.lwjgl.opengl.GL11.glColor3f;


import com.workshop.set.model.interfaces.Model;

public class Viewport implements ScreenFrame {

	private Vector2f ul, lr;
	private Stage _stage;
	private GLCamera _camera;
	private Model _model;
	
	private float _scaleFactor; 
	private Vector2f _currPos; 
	private boolean _dragged; 
	private boolean _shiftDown; 
	private Point [] _linePoints = new Point [2]; 
	private int _toUpdate; 

	public Viewport(Model model, float w, float h) {
		init();
		setSize(new Vector2f(w, h));
		_camera = new GLCamera();
		_camera.setOrthographicView();
		_model = model;
	}

	private void init() {
		ul = new Vector2f(0f, 0f);
		lr = new Vector2f(50f, 50f);
		_scaleFactor = 1000; 
		_dragged = false; 
		_shiftDown = false; 
		_toUpdate = 0; 
	}

	public void setStage(Stage s) {
		_stage = s;
	}
	
	public void setScaleFactor(float increment){
		_scaleFactor += increment; 
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
		if (p.x <= ul.x)
			return false;
		else if (p.x >= lr.x - 1)
			return false;
		if (p.y <= ul.y)
			return false;
		else if (p.y >= lr.y - 1)
			return false;
		return true;
	}

	@Override
	public void render3D() {
		_camera.multMatrix();
		_stage.render3D();
		glColor3f(1,0,0);
		_model.drawGeometricElements();
	}

	@Override
	public void render2D() {}

	
	@Override
	public void mouseClicked(MouseEvent e){}
	
	
	//@Override
	public void mouseClicked(MouseEvent e, FloatBuffer f1, FloatBuffer f2, IntBuffer i) {
		
		if(!_dragged){
			
			float x = e.location.x; 
			float y = e.location.y; 
	
			FloatBuffer objPos = BufferUtils.createFloatBuffer(4); 
			FloatBuffer model = f1; 
			FloatBuffer projection = f2;
			IntBuffer viewport = i;
			
			GLU.gluUnProject(x,y,0f,model,projection,viewport,objPos);
			
			//System.out.println("x: " + objPos.get(0)*_scaleFactor + " y: " + objPos.get(1)*-_scaleFactor );
			Point p = new Point(objPos.get(0)*_scaleFactor, objPos.get(1)*-_scaleFactor, 0);
			_model.addElement(p);
			
			//if shift key down, take care of adding this point to the lines renderable queue and creating a 
			//new line as well
			if(_shiftDown){
				_linePoints[_toUpdate] = p; 
				
				if(_toUpdate==0)	
					_toUpdate=1; 
				else
					_toUpdate=0;
				
				//if a point in both locations, make a new line
				if(_linePoints[0]!=null && _linePoints[1]!=null){
					_model.addElement(new Line(_linePoints[0], _linePoints[1]));
				}
			}
		}
		_dragged = false; 
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//when this called, set the current location as the old pos
		_currPos = e.location; 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		_currPos = e.location; 
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//get difference between current location and old location and use for translate
		_dragged = true; 
		
		float deltaX = _currPos.x - e.location.x; 
		float deltaY = _currPos.y - e.location.y;
		_currPos = e.location; 
		
		//TODO reset center so that transformation still works
		//_camera.mouseMove(deltaX, deltaY);
	}

	@Override
	public void mouseWheelScrolled(Vector2f p, int amount) {
		_camera.mouseWheel(amount / 6f);
		int factor = amount/12; 
		this.setScaleFactor(factor*-10);
	}

	//TODO mouse clicked should only be called if no dragging occurred 
	
	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_SPACE) { // toggle on space bar
			String currMode = _camera.getMode();
			if (currMode.equalsIgnoreCase("orthographic")) {
				_camera.setPerspView();
			} else if (currMode.equalsIgnoreCase("perspective")) {
				_camera.setOrthographicView();
			}
		}

		if(key == 42)
			_shiftDown = true; 
		
	}

	@Override
	public void keyReleased(int key) {
		if(key == 42){	//shift
			_shiftDown = false; 
			_linePoints[0]=null;
			_linePoints[1]=null; 
			_toUpdate = 0; 
		}
	}

	@Override
	public void mouseMoved(Vector2f p) {}

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

		Vector2f.add(ul, newSize, lr);
	}

}
