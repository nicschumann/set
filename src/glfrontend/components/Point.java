package glfrontend.components;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class Point implements GeometricElement{

	private float _x,_y, _z; 
	private float[] _loc = new float[2]; 
	private Sphere _shape = new Sphere();
	
	public Point(){}
	
	//constructor for a 2d point 
	public Point(float x, float y){
		_loc[0] = x; 
		_loc[1] = y; 
		_x = x; 
		_y = y; 
	}
	
	public void setValues(float newX, float newY){
		_x = newX; 
		_y = newY; 
		_loc[0] = newX; 
		_loc[1] = newY; 
	}
	
	public float[] getValues() {
		return _loc;
	}
	
	@Override
	public void render() {
		//render a sphere at set location 
		glTranslatef(_y, 0, _x);
		_shape.draw(.08f, 10, 10);
		glTranslatef(-_y, 0, -_x);
	}

}
