package glfrontend.components;

import org.lwjgl.util.glu.GLU;
//camera class to model the look interactions expected for the user on mouse events in the scene 


public class GLCamera {

	private Vector4 _eye, _look, _up;
	
	public GLCamera(){
		_eye = new Vector4(4, 4, 8, 1);
	    _look = new Vector4(-_eye.x, -_eye.y, -_eye.z, 0).getNormalized();
	    _up = new Vector4(0, 1, 0, 0);
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
    	return getU().getCrossProd(_look).getNormalized();
    }
    public Vector4 getW(){ 
    	return _look.getNormalized().uniformScale(-1);
    }

	public void multMatrix(){
	    GLU.gluLookAt(_eye.x, _eye.y, _eye.z,
	              _eye.x + _look.x, _eye.y + _look.y, _eye.z + _look.z,
	              _up.x, _up.y, _up.z);
	}

	public void lookAt(Vector4 eye, Vector4 look, Vector4 up){
	    _eye = eye;
	    _look = look;
	    _up = up;
	}

	//public void mouseMove(Vector2 delta, MouseButtons buttons, double deltaX, double deltaY)
//	public void mouseMove(MouseButtons buttons, double deltaX, double deltaY)
//	{
//		
//		//may only need vector 4 and can use delta x and y for the rest 
//		
//		//mouse events to figure out which button pressed 
//		
//		
//	    if (buttons == Qt::RightButton)
//	    {
//	        lookVectorRotate(delta);
//	    }
//	    else if (buttons == Qt::MidButton)
//	    {
//	        filmPlaneTranslate(delta);
//	    }
//	}

	public void mouseWheel(float delta){
	    this.lookVectorTranslate(delta);
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
