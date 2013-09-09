package ng.tim.game.gfx;

import java.awt.geom.AffineTransform;

public class Camera
{
	private AffineTransform transformation; //This is the transformation
	
	private double rotation = 0;
	private double x = 0;
	private double y = 0;
	private double scaleX = 1;
	private double scaleY = 1;
	private int sheer = 0;
	
	public Camera()
	{
		
	}
	
	public Camera(double x, double y, double scaleX, double scaleY, double rotation)
	{
		
	}
	
	public AffineTransform getTransformation()
	{
		transformation = new AffineTransform();
		transformation.setToIdentity();
		
		AffineTransform scale = new AffineTransform();
		scale.setToScale(scaleX, scaleY);
		
		AffineTransform rotateTrans = new AffineTransform();
		rotateTrans.rotate(rotation);
		
		AffineTransform translate = new AffineTransform();
		translate.setToTranslation(x, y);
		
		transformation.concatenate(scale);
		transformation.concatenate(rotateTrans);
		transformation.concatenate(translate);
		
		return transformation;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public int getSheer() {
		return sheer;
	}

	public void setSheer(int sheer) {
		this.sheer = sheer;
	}
	
	
}
