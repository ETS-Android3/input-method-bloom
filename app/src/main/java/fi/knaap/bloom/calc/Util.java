package fi.knaap.bloom.calc;

public class Util {

	public static void rotatePoint(double rotateAngle, RawPoint point) {
		float xnew = (float) (point.getX() * Math.cos(rotateAngle) - point
				.getY()
				* Math.sin(rotateAngle));
		float ynew = (float) (point.getX() * Math.sin(rotateAngle) + point
				.getY()
				* Math.cos(rotateAngle));
		point.setXY(xnew, ynew);
	}

}
