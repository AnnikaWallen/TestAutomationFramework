package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests for the KioskMode
 *
 * Created by jordam on 2016-12-22.
 */
public class KioskModeReportTest extends UnitTestClass{

    @Test
    public void evaluateTestCase(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    @Ignore
    public void reportBeingCreated(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
        kioskDisplay.create("C:\\temp\\reportTestRun.html", "Dummy tests", 4);
        kioskDisplay.openInDefaultBrowser();
    }
}
