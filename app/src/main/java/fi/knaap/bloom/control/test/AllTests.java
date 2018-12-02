package fi.knaap.bloom.control.test;

import junit.framework.Test;
import android.test.suitebuilder.TestSuiteBuilder;

public class AllTests extends junit.framework.TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }

}
