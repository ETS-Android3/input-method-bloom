package fi.knaap.bloom.control;

import android.content.res.XmlResourceParser;
import android.util.Log;
import fi.knaap.bloom.calc.RawStroke;

public class StrokeDescription implements Comparable<StrokeDescription> {
	public static double MIN_AREA = .125;

	public enum Curve {
		LEFT(-Double.MAX_VALUE, -MIN_AREA, 3000), RIGHT(MIN_AREA,
				Double.MAX_VALUE, 2000), NONE(-MIN_AREA, MIN_AREA, 1000);

		private double minValue;
		private double maxValue;
		private int rotateCount = 0;
		private int sortValue;

		public int getRotateCount() {
			return rotateCount;
		}

		private Curve(double minValue, double maxValue, int sortValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.sortValue = sortValue;
		}

		int getSortValue() {
			return sortValue;
		}

		public static Curve getCurve(double curveRad, double relArea) {
			for (Curve curve : values()) {
				if (curve.minValue < relArea && relArea <= curve.maxValue) {
					curve.rotateCount = (int) Math.floor(Math.abs(curveRad
							/ (2 * Math.PI)));
					return curve;
				}
			}
			return NONE;
		}
	}

	public enum RelArea {
		NONE(0, 1, 100), LARGE(1, Double.MAX_VALUE, 300);

		private double minValue;
		private double maxValue;
		private int sortValue;

		private RelArea(double minValue, double maxValue, int sortValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.sortValue = sortValue;
		}

		int getSortValue() {
			return sortValue;
		}

		public static RelArea getRelArea(double relAreaSign) {
			double relAreaNoSign = Math.abs(relAreaSign);
			for (RelArea relArea : values()) {
				if (relArea.minValue < relAreaNoSign
						&& relAreaNoSign <= relArea.maxValue) {
					return relArea;
				}
			}
			return NONE;
		}
	}

	public enum Direction {
		N(1), NE(2), E(3), SE(4), S(5), SW(6), W(7), NW(8), ALL(0);
		private int sortValue;

		Direction(int sortValue) {
			this.sortValue = sortValue;
		}

		int getSortValue() {
			return sortValue;
		}

		int getIndex() {
			return sortValue - 1;
		}

		public static Direction getDirection(double directionRad) {
			int value = (int) Math.round(4 * (1 - (directionRad / Math.PI))) % 8;
			Log.i("getDirection", "Getting direction for value!: " + value
					+ ". RAD dir = " + directionRad);
			return values()[value];
		}

		/**
		 * get value in range
		 * 
		 * @param directionValue
		 *            Value, possibly outside of range
		 * @return
		 */
		public static Direction getDirection(int directionValue) {
			int value = (8 + directionValue) % 8;
			return values()[value];
		}
	}

	public enum Length {
		DOT(0, .20, 20), SHORT(.20, 1.1, 30), LINE(1.1, Double.MAX_VALUE, 40);
		private int sortValue;
		private double minValue;
		private double maxValue;

		private Length(double minValue, double maxValue, int sortValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.sortValue = sortValue;
		}

		int getSortValue() {
			return sortValue;
		}

		public static Length getLength(double lengthPxl, double radius) {
			double relLength = lengthPxl / radius;
			for (Length length : values()) {
				if (length.minValue < relLength && relLength <= length.maxValue) {
					return length;
				}
			}
			return DOT;
		}
	}

	private RelArea relArea;
	private Curve curve;
	private Direction direction;
	private Length length;
	private BloomController bloomController;

	public StrokeDescription() {
	}

	public void setStroke(RawStroke stroke) {
		double distance = stroke.getDistance();
		double area = stroke.getArea();
		double curve = stroke.getCurve();
		double direction = stroke.getOrigDirection();
		double relAreaDbl = area / (distance * distance);

		this.length = Length.getLength(stroke.length(), getBloomController()
				.getConfig().getBloomRadius());
		if (this.length.compareTo(Length.DOT) == 0) {
			relArea = RelArea.NONE;
			this.curve = Curve.NONE;
			this.direction = Direction.ALL;
		} else if (this.length.compareTo(Length.SHORT) == 0) {
			relArea = RelArea.NONE;
			this.curve = Curve.NONE;
			this.direction = Direction.getDirection(direction);
		} else {
			relArea = RelArea.getRelArea(relAreaDbl);
			this.curve = Curve.getCurve(curve, relAreaDbl);
			this.direction = Direction.getDirection(direction);
			// Finally, invert the stroke curve and the direction.
			if (RelArea.NONE.equals(relArea)) {
				if (this.curve.equals(Curve.LEFT)) {
					this.curve = Curve.RIGHT;
					this.direction = Direction.getDirection(this.direction
							.getIndex() - 1);
				} else if (this.curve.equals(Curve.RIGHT)) {
					this.curve = Curve.LEFT;
					this.direction = Direction.getDirection(this.direction
							.getIndex() + 1);
				}
			}
		}
	}

	public BloomController getBloomController() {
		return bloomController;
	}

	public void setBloomController(BloomController bloomController) {
		this.bloomController = bloomController;
	}

	public String toString() {
		return relArea.name() + ", " + curve.name() + ", " + direction.name()
				+ ", " + length.name();
	}

	public StrokeDescription(RelArea relArea, Curve curve, Direction direction,
			Length length) {
		super();
		this.relArea = relArea;
		this.curve = curve;
		this.direction = direction;
		this.length = length;
	}

	public RelArea getRelArea() {
		return relArea;
	}

	public Curve getCurve() {
		return curve;
	}

	public Direction getDirection() {
		return direction;
	}

	public Length getLength() {
		return length;
	}

	public static StrokeDescription createDescription(XmlResourceParser parser) {
		RelArea relArea = RelArea.valueOf(parser
				.getAttributeValue(null, "Area"));
		Curve curve = Curve.valueOf(parser.getAttributeValue(null, "Curve"));
		Direction direction = Direction.valueOf(parser.getAttributeValue(null,
				"Direction"));
		Length length = Length
				.valueOf(parser.getAttributeValue(null, "Length"));
		return new StrokeDescription(relArea, curve, direction, length);
	}

	private int getSortValue() {
		return this.getCurve().getSortValue()
				+ this.getDirection().getSortValue()
				+ this.getLength().getSortValue()
				+ this.getRelArea().getSortValue();
	}

	@Override
	public int compareTo(StrokeDescription another) {
		// Special case:
		if (getDirection() == Direction.ALL
				|| another.getDirection() == Direction.ALL) {
			return (int) Math.floor(getSortValue() / 10)
					- (int) Math.floor(another.getSortValue() / 10);
		}
		return getSortValue() - another.getSortValue();
	}

}
