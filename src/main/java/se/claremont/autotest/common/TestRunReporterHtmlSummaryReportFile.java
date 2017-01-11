package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterHtmlSummaryReportFile implements TestRunReporter {
    private HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        writeReport();
    }

    @Override
    public void evaluateTestCase(TestCase testCase) {
        htmlSummaryReport.evaluateTestCase(testCase);

    }

    public void evaluateTestSet(TestSet testSet){
        htmlSummaryReport.evaluateTestSet(testSet);
    }


    /**
     * Check to see if the reportTestRun should be written at all
     * @return Return true if the number of test cases exceeds one
     */
    private boolean reportShouldBeWritten(){
        return (htmlSummaryReport.numberOfTestCases() > 1);
    }

    /**
     * Writes the compiled summary reportTestRun to a file in the test run catalogue.
     */
    private void writeReport(){
        if(reportShouldBeWritten()){
            SupportMethods.saveToFile(htmlSummaryReport.createReport(), LogFolder.testRunLogFolder + "_summary.html");
            LogPost logPost = new LogPost(LogLevel.EXECUTED, "Summary reportTestRun saved as '" + LogFolder.testRunLogFolder + "_summary.html'.");
            System.out.println(SupportMethods.LF + logPost.toString());
        }
    }

}