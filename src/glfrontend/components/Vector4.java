package glfrontend.components;

public class Vector4 {
	
	public float x,y,z,w; 
	
	public Vector4(float X, float Y, float Z, float W){
		x = X; 
		y = Y; 
		z = Z; 
		w = W; 
	}
	
	public Vector4 updateVals(float newX, float newY, float newZ, float newW){
		x = newX; 
		y = newY; 
		z = newZ; 
		w = newW;
		return this; 
	}
	
	/**
	 * Returns the normalized form of this vector
	 */
	public Vector4 getNormalized(){
		double base = 1.0 / Math.sqrt(x*x + y*y + z*z + w*w);
		x*=base; 
		y*=base; 
		z*=base; 
		w*=base;
		return this; 
	}
	
	/**
	 * Returns cross product of this vector with the passed in vector
	 */
	public Vector4 getCrossProd(Vector4 toCross){
		return new Vector4(this.y*toCross.z - this.z*toCross.y, this.z*toCross.x - this.x*toCross.z, this.x*toCross.y - this.y*toCross.x, 0);
	}
	
	/**
	 * Uniformly scales this vector by the scale factor
	 */
	public Vector4 uniformScale(double scaleFactor){
    	this.x*=scaleFactor; 
    	this.y*=scaleFactor; 
    	this.z*=scaleFactor; 
    	this.w*=scaleFactor;
    	return this; 
	}
	
	/**
	 * Adds the given vector to this vector
	 */
	public Vector4 add(Vector4 toAdd){
		this.x +=toAdd.x; 
		this.y += toAdd.y; 
		this.z += toAdd.z; 
		this.w += toAdd.w; 
		return this; 
	}
	
	public Vector4 subtract(Vector4 toSubtract){
		this.x -=toSubtract.x; 
		this.y -= toSubtract.y; 
		this.z -= toSubtract.z; 
		this.w -= toSubtract.w; 
		return this; 
	}
	
}
