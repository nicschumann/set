package glfrontend.components;

import java.util.HashSet;
import java.util.Set;
import static org.lwjgl.opengl.GL11.*;

public class Line implements GeometricElement{

	private Set<GeometricElement> _points; 
	private Point _pt1, _pt2; 
	
	public Line(){}
	
	public Line(Point start, Point end){
		_pt1 = start; 
		_pt2 = end; 
		_points = new HashSet<GeometricElement>();
	}
	
	public void setPoints(Point pt1, Point pt2){
		_points.clear(); 
		_points.add(pt1);
		_points.add(pt2);
		_pt1 = pt1; 
		_pt2 = pt2; 
	}
	
	public Set<GeometricElement> getValues() {
		return _points; 
	}
	
	@Override
	public void render() {
		glBegin(GL_LINES);
		glVertex2d(_pt1.getValues().getX(), _pt1.getValues().getY());
		glVertex2d(_pt2.getValues().getX(), _pt2.getValues().getY());
		glEnd();
	}
}
