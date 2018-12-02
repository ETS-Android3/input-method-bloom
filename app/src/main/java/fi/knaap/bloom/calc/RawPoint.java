package fi.knaap.bloom.calc;

public class RawPoint {
	private float x;
	private float y;
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}
	public RawPoint(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	public void move(float x, float y){
		this.x = this.x + x;
		this.y = this.y + y;		
	}
	public void move(RawPoint other){
		move(other.x, other.y);
	}
	public void rotatePoint(double rotateAngle) {
		float xnew = (float) (getX() * Math.cos(rotateAngle) - getY()
				* Math.sin(rotateAngle));
		float ynew = (float) (getX() * Math.sin(rotateAngle) + getY()
				* Math.cos(rotateAngle));
		setXY(xnew, ynew);
	}

}
