package glfrontend.components;

import glfrontend.ScreenFrame;
import glfrontend.ScreenFrame.MouseEvent;

import org.lwjgl.util.glu.GLU;
//camera class to model the look interactions expected for the user on mouse events in the scene 


public class Camera {

	private Vector4 _eye, _look, _up, _focus;
	private String _type; 
	private float _theta, _phi;	//correspond to rotation in xy and yz planes
	
	public Camera(){}
	
	//ctor for a custom camera (all vectors and type/name)
	public Camera(Vector4 position, Vector4 orientation, String name){}
	
	public Camera(String type){	//default cameras
		if(type.equalsIgnoreCase("orthographic")){	//more descriptive
			_eye = new Vector4(0, 0, 10, 1);
		    _look = new Vector4(-_eye.x, -_eye.y, -_eye.z, 0).getNormalized();
		    _up = new Vector4(0, 1, 0, 0);
		    _type = "orthographic";
		    //theta and phi not applicible for orthographic (no rotation)
		}
		else if(type.equalsIgnoreCase("perspective")){
		    _theta = (float)(7*Math.PI)/4; 	//start at the fourth quadrant 
		    _phi = 0; 
		    _focus = new Vector4(0,0,0,0);
			
		    _eye = new Vector4((float)(5*Math.sqrt(2)*Math.cos(_theta)), (float)(5*Math.sqrt(2)*Math.sin(_theta)), 5, 1);
		    _look = new Vector4(-_eye.x, -_eye.y, -_eye.z, 0).getNormalized();
		    _up = new Vector4(0, 0, 1, 0);	    
		    _type = "perspective";
		}
	}
	
    public Vector4 getEye(){ 
    	return _eye;
    }
    public Vector4 getLook(){
    	return _look;
    }
    public Vector4 getUp(){ 
    	return _up; }

    public Vector4 getU(){ 
    	return _look.getCrossProd(_up).getNormalized();
    }
    public Vector4 getV(){ 
    	Vector4 lookCopy = new Vector4(_look.x, _look.y, _look.z, _look.w);
    	return getU().getCrossProd(lookCopy).getNormalized();
    }
    public Vector4 getW(){ 
    	Vector4 lookCopy = new Vector4(_look.x, _look.y, _look.z, _look.w); 	//make this better
    	return lookCopy.getNormalized().uniformScale(-1.0);
    }

	public void multMatrix(){
	    GLU.gluLookAt(_eye.x, _eye.y, _eye.z,
	              (_eye.x + _look.x), (_eye.y + _look.y), (_eye.z + _look.z),
	              _up.x, _up.y, _up.z);
	}

	public void lookAt(Vector4 eye, Vector4 look, Vector4 up){
	    _eye = eye;
	    _look = look;
	    _up = up;
	}
	
	public String getType(){
		return _type; 
	}

	public void mouseMove(double deltaX, double deltaY, MouseEvent e)
	{
		if(e.button==ScreenFrame.MouseButton.LEFT)
			this.filmPlaneTranslate(deltaX, deltaY);
		else if(e.button==ScreenFrame.MouseButton.RIGHT)
			this.lookVectorRotate(deltaX, deltaY);
	}

	public void mouseWheel(float delta){
	    this.lookVectorTranslate(delta*10);
	}

	public void filmPlaneTranslate(double deltaX, double deltaY){
		Vector4 v1 = getU().uniformScale(deltaX);
		Vector4 v2 = getV().uniformScale(deltaY);
		Vector4 toAdd = (v1.subtract(v2)).uniformScale(.01);
		_eye = _eye.add(toAdd);
	}

	/**
	 *given change in x and y
	 */
	public void lookVectorRotate(double deltaX, double deltaY)
	{
		//camera's position rotated around the origin
		if(!_type.equalsIgnoreCase("orthographic")){
			//first update with respect to theta
			_theta += (deltaX/2)*Math.PI/180.0;
			float r1 = (float)(_eye.getHorizontalDistance(_eye, _focus));		
			_eye.updateVals((float)(r1*Math.cos(_theta)), (float)(r1*Math.sin(_theta)), _eye.z, 0);
			_look.updateVals(-_eye.x, -_eye.y, -_eye.z, 0);
			_look = _look.getNormalized();
			//then update with respect to phi
		}	
	}

	public void lookVectorTranslate(float delta){
		Vector4 toAdd = getW().uniformScale(delta * -0.005);
		_eye = _eye.add(toAdd);
	}
}
