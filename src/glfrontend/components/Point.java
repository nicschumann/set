package glfrontend.components;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.util.glu.Sphere;

public class Point implements GeometricElement{

	private float _x,_y, _z; 
	private float[] _loc = new float[3]; 
	private Sphere _shape = new Sphere();
	
	private boolean _highlighted; 
	
	public Point(){}
	
	public Point(float x, float y, float z){
		_loc[0] = x; 
		_loc[1] = y;
		_loc[2] = z; 
		_x = x; 
		_y = y; 
		_z = z; 
		this.init();
	}
	
	public void init(){
		_highlighted = false; 
	}
	
	public void setValues(float newX, float newY, float newZ){
		_x = newX; 
		_y = newY; 
		_z = newZ; 
		_loc[0] = newX; 
		_loc[1] = newY;
		_loc[2] = newZ; 
		
	}
	
	public float[] getValues() {
		return _loc;
	}
	
	@Override
	public void render() {
		//render a sphere at set location 
		if(_highlighted)
			glColor3f(0,1,0);
		else
			glColor3f(1,0,0);
		
		glTranslatef(_x, _y, 0);
		_shape.draw(.08f, 10, 10);
		glTranslatef(-_x, -_y, 0);
	}

	@Override
	public boolean checkIntersection(Point pt) {
		//check if this sphere intersects with the given point		
		
		//check if pt is within the given radius of center of this sphere
		float[] loc = pt.getValues(); 
		double dist = Math.sqrt((_x-loc[0])*(_x-loc[0]) + (_y-loc[1])*(_y-loc[1]) + (_z-loc[2])*(_z-loc[2]));
		
		if(dist<=.08){
			_highlighted=true; 
			return true; 
		}
		return false;
	}
}
