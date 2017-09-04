package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FakeTestRunReporter implements TestRunReporter {
    int numberOfReportsPerformed = 0;
    int numberOfTestCaseEvaluationsPerformed = 0;
    int numberOfTestSetEvaluationsPerformed = 0;
    List<String> testSetNames = new ArrayList<>();
    List<String> testCaseNames = new ArrayList<>();

    public FakeTestRunReporter(){
        TafParallelTestCaseRunner.testSets = new HashSet<>();
    }

    @Override
    public void report() {
        numberOfReportsPerformed++;
    }

    @Override
    public void evaluateTestCase(TestCase testCase) {
        numberOfTestCaseEvaluationsPerformed++;
        testCaseNames.add(testCase.testSetName + "." + testCase.testName);
    }

    @Override
    public void evaluateTestSet(TestSet testSet) {
        numberOfTestSetEvaluationsPerformed++;
        testSetNames.add(testSet.name);
    }

    @Override
    public String toString(){
        return "[FakeTestRunReporter:" + System.lineSeparator() +
                "   numberOfReportsPerformed=" + numberOfReportsPerformed + System.lineSeparator() +
                "   numberOfTestCaseEvaluationsPerformed=" + numberOfTestCaseEvaluationsPerformed + System.lineSeparator() +
                "   numberOfTestSetEvaluationsPerformed=" + numberOfTestSetEvaluationsPerformed + System.lineSeparator() +
                "   TestSets registered for evaluation:" + System.lineSeparator() +
                "      [" + String.join("]" + System.lineSeparator() + "      [", testSetNames) + "]" + System.lineSeparator() +
                "   TestCases registered for evaluation:" + System.lineSeparator() +
                "      [" + String.join("]" + System.lineSeparator() + "      [", testCaseNames) + "]" + System.lineSeparator() +
                "]";

    }
}