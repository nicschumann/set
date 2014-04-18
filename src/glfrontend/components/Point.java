package glfrontend.components;

import java.awt.geom.Point2D;
import static org.lwjgl.opengl.GL11.*;

public class Point implements GeometricElement{

	private Point2D _location; 
	
	public Point(){}
	
	public Point(double x, double y){
		_location = new Point2D.Double(x,y);
	}
	
	public void setValues(double newX, double newY){
		_location.setLocation(newX, newY);
	}
	
	public Point2D getValues() {
		return _location;
	}
	
	@Override
	public void render() {
		//for now, draw a little square around the point
	}

}
