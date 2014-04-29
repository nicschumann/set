package glfrontend.components;

import org.lwjgl.util.glu.GLU;
//camera class to model the look interactions expected for the user on mouse events in the scene 


public class Camera {

	private Vector4 _eye, _look, _up;
	private String _type; 
	
	public Camera(){}
	
	//ctor for a custom camera (all vectors and type/name)
	
	public Camera(String type){	//default cameras
		if(type.equalsIgnoreCase("orthographic")){	//more descriptive
			_eye = new Vector4(0, 0, 10, 1);
		    _look = new Vector4(-_eye.x, -_eye.y, -_eye.z, 0).getNormalized();
		    _up = new Vector4(0, 1, 0, 0);
		    _type = "orthographic";
		}
		else if(type.equalsIgnoreCase("perspective")){
			_eye = new Vector4(5, -5, 5, 1);
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
    	Vector4 lookCopy = new Vector4(_look.x, _look.y, _look.z, _look.w);
    	Vector4 upCopy = new Vector4(_up.x, _up.y, _up.z, _up.w);
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

	public void mouseMove(double deltaX, double deltaY)
	{
		//mouse events to figure out which button pressed 
//	    if (RightButton)
//	    {
//	        lookVectorRotate(delta);
//	    }
	    this.filmPlaneTranslate(deltaX, deltaY);
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

	public void lookVectorRotate(double deltaX, double deltaY)
	{
	    Vector4 w = this.getW();
	    float angleX = (float)(Math.asin(-w.y) - deltaY * 0.0025);
	    float angleY = (float)(Math.atan2(-w.z, -w.x) + deltaX * 0.0025);
	    angleX = (float)(Math.max(-Math.PI / 2 + 0.001, Math.min(Math.PI / 2 - 0.001, (double)angleX)));
	    _look = new Vector4((float)(Math.cos(angleY) * Math.cos(angleX)), (float)(Math.sin(angleX)), (float)(Math.sin(angleY) * Math.cos(angleX)), 0);
	}

	public void lookVectorTranslate(float delta){
		Vector4 toAdd = getW().uniformScale(delta * -0.005);
		_eye = _eye.add(toAdd);
	}
}
