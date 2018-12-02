package fi.knaap.bloom.calc;

import junit.framework.TestCase;

public class RawStrokeTest extends TestCase {

	public void testNormalize() {
		for (int j = 0; j < 10000; j++) {
			RawStroke stroke = new RawStroke();
			for (int i = 0; i < 100; i++) {
				float x = (float) ((Math.random() - 0.5) * 1000);
				float y = (float) ((Math.random() - 0.5) * 1000);
				stroke.add(new RawPoint(x, y));
			}
			double preLength = stroke.length();
			stroke.normalize();
			double postLength = stroke.length();
			double diff = Math.abs(postLength - preLength);
			double errorRatio = diff / ((postLength + preLength) / 2);
			assertTrue(0.0000001 > Math.abs(stroke.getPoint(0).getX()));
			assertTrue(0.0000001 > Math.abs(stroke.getPoint(0).getY()));
			assertTrue(0.0000001 > Math.abs(stroke.getPoint(
					stroke.getPointCount() - 1).getY()));
			assertTrue(0.0000001 > errorRatio);
		}
	}

	public void testAres() {
		RawStroke stroke = new RawStroke();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 1));
		stroke.add(new RawPoint(1, 1));
		stroke.add(new RawPoint(1, 0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertEquals(1.0, stroke.getArea());
		assertTrue(0.0000001 > Math.abs(Math.PI - stroke.getCurve()));
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 1));
		stroke.add(new RawPoint(1, 1));
		stroke.add(new RawPoint(2, 0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertEquals(1.5, stroke.getArea());
		assertTrue(0.0000001 > Math.abs(.75 * Math.PI - stroke.getCurve()));
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(1, 1));
		stroke.add(new RawPoint(2, 2));
		stroke.add(new RawPoint(3, 1));
		stroke.add(new RawPoint(4, 0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertTrue(0.0000001 > Math.abs(.5 * Math.PI - stroke.getCurve()));
		assertEquals(4.0, stroke.getArea());
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(-1, 1));
		stroke.add(new RawPoint(-2, 2));
		stroke.add(new RawPoint(-3, 1));
		stroke.add(new RawPoint(-4, 0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertTrue(0.0000001 > Math.abs(-.5 * Math.PI - stroke.getCurve()));
		assertEquals(-4.0, stroke.getArea());
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(-1, -1));
		stroke.add(new RawPoint(-2, -2));
		stroke.add(new RawPoint(-3, -1));
		stroke.add(new RawPoint(-4, -0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertTrue(0.0000001 > Math.abs(.5 * Math.PI - stroke.getCurve()));
		assertEquals(4.0, stroke.getArea());
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 2));
		stroke.add(new RawPoint(2, 2));
		stroke.add(new RawPoint(2, 0));
		stroke.add(new RawPoint(1, 0));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertTrue(0.0000001 > Math.abs(1.5 * Math.PI - stroke.getCurve()));
		assertEquals(4.0, stroke.getArea());
		stroke.clear();
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 2));
		stroke.add(new RawPoint(2, 2));
		stroke.add(new RawPoint(2, 0));
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 2));
		stroke.add(new RawPoint(2, 2));
		stroke.add(new RawPoint(2, 0));
		stroke.add(new RawPoint(0, 0));
		stroke.add(new RawPoint(0, 2));
		stroke.normalize();
		System.out.println(stroke.getCurve());
		assertTrue(0.0000001 > Math.abs(4.0 * Math.PI - stroke.getCurve()));
		assertEquals(8.0, stroke.getArea());
	}

	/* any point y on a straight curve shouldn'd change the value of the cure */
	public void testCurve() {
		RawStroke stroke = new RawStroke();
		for (int i = 0; i < 1000; i++) {
			stroke.clear();
			stroke.add(new RawPoint(0, 0));
			stroke.add(new RawPoint(1, 0));
			for (int x = 2; x < 1000; x++) {
				float y = (float) ((Math.random() - 0.5) * 1000);
				stroke.add(new RawPoint(x, y));
			}
			stroke.add(new RawPoint(100000, 0));
			stroke.add(new RawPoint(100001, 0));
			stroke.normalize();
			assertTrue(0.0000001 > Math.abs(0 - stroke.getCurve()));
		}
		for (int i = 0; i < 1000; i++) {
			stroke.clear();
			stroke.add(new RawPoint(0, 0));
			stroke.add(new RawPoint(-1, 0));
			for (int x = -2; x > -1000; x--) {
				float y = (float) ((Math.random() - 0.5) * 1000);
				stroke.add(new RawPoint(x, y));
			}
			stroke.add(new RawPoint(-100000, 0));
			stroke.add(new RawPoint(-100001, 0));
			stroke.normalize();
			assertTrue(0.0000001 > Math.abs(0 - stroke.getCurve()));
		}
		for (int i = 0; i < 1000000; i++) {
			stroke.clear();
			stroke.add(new RawPoint(0, 0));
			stroke.add(new RawPoint(0, 1));
			for (int y = 2; y < 100; y++) {
				float x = (float) ((Math.random() - 0.5) * 1000);
				stroke.add(new RawPoint(x, y));
			}
			stroke.add(new RawPoint(0, 100000));
			stroke.add(new RawPoint(1, 100000));
			stroke.normalize();
			System.out.println(stroke.getCurve());
			assertTrue(0.00001 > Math.abs(0.5 * Math.PI - stroke.getCurve()));
		}
	}

}
