package fi.knaap.bloom.calc;

import java.util.ArrayList;

public class RawStroke {
	ArrayList<RawPoint> strokePoints = new ArrayList<RawPoint>();
	private double origDirection;

	public double getOrigDirection() {
		return origDirection;
	}

	public boolean add(RawPoint object) {
		return strokePoints.add(object);
	}

	public void clear() {
		strokePoints.clear();
	}

	public double length() {
		RawPoint prevPoint = null;
		double length = 0;
		for (RawPoint nextPoint : strokePoints) {
			length = length + getDistance(prevPoint, nextPoint);
			prevPoint = nextPoint;
		}
		return length;
	}

	/*
	 * Move start to 0,0 rotate so that the end is at x,0
	 */
	public void normalize() {
		if (strokePoints.size() < 2) {
			return;
		}
		RawPoint startPoint = strokePoints.get(0);
		RawPoint endPoint = strokePoints.get(strokePoints.size() - 1);
		float moveX = -startPoint.getX();
		float moveY = -startPoint.getY();
		moveStrokeToOrigin(moveX, moveY);
		float deltaX = endPoint.getX();
		float deltaY = endPoint.getY();
		origDirection= Math.atan2(deltaX, deltaY);
		double rotateAngle = -Math.PI * 0.5 + origDirection;
		rotate(rotateAngle);
	}

	private void rotate(double rotateAngle) {
		ArrayList<RawPoint> newStrokePoints = new ArrayList<RawPoint>();
		for (RawPoint point : strokePoints) {
			Util.rotatePoint(rotateAngle, point);
			newStrokePoints.add(point);
		}
		strokePoints = newStrokePoints;
	}

	private void moveStrokeToOrigin(float moveX, float moveY) {
		ArrayList<RawPoint> newStrokePoints = new ArrayList<RawPoint>();
		for (RawPoint point : strokePoints) {
			point.setXY(point.getX() + moveX, point.getY() + moveY);
			newStrokePoints.add(point);
		}
		strokePoints = newStrokePoints;
	}

	public double getDistance(RawPoint pointA, RawPoint pointB) {
		if (pointA == null || pointB == null) {
			return 0;
		}
		double deltaX = pointB.getX() - pointA.getX();
		double deltaY = pointB.getY() - pointA.getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
	
	public double getDistance() {
		if(strokePoints.size() < 2){
			return 0;
		}
		return getDistance(strokePoints.get(0), strokePoints.get(strokePoints.size() - 1));
	}

	public double distanceSign(RawPoint pointA, RawPoint pointB) {
		if (pointA == null || pointB == null) {
			return 0;
		}
		double deltaX = pointB.getX() - pointA.getX();
		double deltaY = pointB.getY() - pointA.getY();
		double sign = 1;
		if (deltaX * deltaY != 0.0) {
			sign = (deltaX * deltaY) / Math.abs(deltaX * deltaY);
		}
		return sign * Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	public double getArea() {
		double area = 0;
		RawPoint prevPoint = null;
		for (RawPoint nextPoint : strokePoints) {
			area = area + areaUnderPoints(prevPoint, nextPoint);
			prevPoint = nextPoint;
		}
		return area;
	}

	public double getCurve() {
		double curve = 0;
		RawPoint point1 = null;
		double prevAngle = 999;
		double angle = 0;
		for (RawPoint point2 : strokePoints) {
			if (point1 != null) {
				double deltaX = point2.getX() - point1.getX();
				double deltaY = point2.getY() - point1.getY();
				angle = Math.atan2(deltaY, deltaX);
				if (prevAngle != 999) {
					curve = curve + makeMaxPi(prevAngle - angle);
				}
				prevAngle = angle;
			}
			point1 = point2;
		}
		return curve;
	}
	
	private double makeMaxPi(double angle){
		return angle % Math.PI;
	}

	public RawPoint getPoint(int i) {
		return strokePoints.get(i);
	}

	public int getPointCount() {
		return strokePoints.size();
	}

	private double areaUnderPoints(RawPoint pointA, RawPoint pointB) {
		if (pointA == null || pointB == null) {
			return 0;
		}
		float deltaX = pointB.getX() - pointA.getX();
		float avgY = (pointB.getY() + pointA.getY()) / 2;
		return avgY * deltaX;
	}
	
}
