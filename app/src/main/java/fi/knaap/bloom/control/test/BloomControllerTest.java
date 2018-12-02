package fi.knaap.bloom.control.test;

import android.test.AndroidTestCase;
import fi.knaap.bloom.BloomService;
import fi.knaap.bloom.control.BloomController;

public class BloomControllerTest extends AndroidTestCase  {
	BloomController contr = new BloomController(new BloomService());
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testProcessStroke() {
//		fail("Not yet implemented");
//	}
	public void testLoadBloomLayOut() {
		contr.updateBloomLayOut();
	}
	

}
