package se.claremont.autotest.common.testrun;

import org.junit.*;
import org.junit.runners.model.InitializationError;
import se.claremont.autotest.common.testset.UnitTestClass;

public class TafTestRunner_Tests {
    long startTime;
    FakeTestRunReporter fakeTestRunReporter;
    int bufferTimeAccepted = 4;
    int numberOfTestCases = 4;
    int timePerTestCase = 3;
    int getNumberOfTestClasses = 2;

    public void setup(){
        fakeTestRunReporter = new FakeTestRunReporter();
        TestRun.isInitialized = false;
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.reporters.clear();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeTestRunReporter);
        startTime = System.currentTimeMillis();
    }

    private void assertReporting(){
        Assert.assertTrue("Expected one test run report, but found " + fakeTestRunReporter.numberOfReportsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.numberOfReportsPerformed == 1);
        Assert.assertTrue("Expected four test evaluations performed, but found " + fakeTestRunReporter.numberOfTestCaseEvaluationsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.numberOfTestCaseEvaluationsPerformed == 4);
        //Assert.assertTrue("Expected two test set evaluations performed, but found " + fakeTestRunReporter.numberOfTestSetEvaluationsPerformed + "." + System.lineSeparator() + fakeTestRunReporter.toString(), fakeTestRunReporter.numberOfTestSetEvaluationsPerformed == 2);
    }

    private void assertDuration(int lowestExpectedDurationInSeconds, int highestExpectedDurationInSeconds){
        Assert.assertTrue("Expected more than " + lowestExpectedDurationInSeconds + " seconds. Took: " + (System.currentTimeMillis()-startTime)/1000, System.currentTimeMillis()-startTime > lowestExpectedDurationInSeconds * 1000);
        Assert.assertTrue("Expected less than " + highestExpectedDurationInSeconds + " seconds. Took: " + (System.currentTimeMillis()-startTime)/1000, System.currentTimeMillis()-startTime < highestExpectedDurationInSeconds * 1000);
    }

    @Test
    public void parallelExecutionOneThread(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=1", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*numberOfTestCases, timePerTestCase*numberOfTestCases+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionTwoThreads(){
        setup();
        int numberOfThreads = 2;
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=" + numberOfThreads, ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName(), "-Dtestprop=yay"};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*numberOfTestCases/numberOfThreads, timePerTestCase*numberOfTestCases/numberOfThreads+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSetToTrueShouldRunInParallel(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=true", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/getNumberOfTestClasses, numberOfTestCases*timePerTestCase/getNumberOfTestClasses + bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSetToFalseShouldRunSequential(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=false", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionExcessiveThreads() throws InitializationError {
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=15", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase, timePerTestCase + bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSameAmountOfThreadsAsTests() throws InitializationError {
        setup();
        int numberOfThreads = 2;
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=" + numberOfThreads, ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/numberOfThreads, numberOfTestCases*timePerTestCase/numberOfThreads+ bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionSingleThread(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=1", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionClasses(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=classes", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase/getNumberOfTestClasses, numberOfTestCases*timePerTestCase/getNumberOfTestClasses+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionMethods(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=methods", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(timePerTestCase*getNumberOfTestClasses, timePerTestCase*getNumberOfTestClasses+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionNone(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=none", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionGibberish(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=sdgrsry", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionZero(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=0", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertReporting();
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
    }

    @Test
    public void parallelExecutionNegative(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=-3", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    public void parallelExecutionDefault(){
        setup();
        String[] args = new String[]{ ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

    @Test
    @Ignore
    public void parallelExecutionEmtpyString(){
        setup();
        String[] args = new String[]{ "PARALLEL_TEST_EXECUTION_MODE=", ParallelExecution_TestClass1.class.getName(), ParallelExecution_TestClass2.class.getName()};
        CliTestRunner.runInTestMode(args);
        assertDuration(numberOfTestCases*timePerTestCase, numberOfTestCases*timePerTestCase+bufferTimeAccepted);
        assertReporting();
    }

}
