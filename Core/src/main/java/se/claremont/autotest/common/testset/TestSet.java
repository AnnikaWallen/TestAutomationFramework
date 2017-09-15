package se.claremont.autotest.common.testset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import se.claremont.autotest.common.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

/**
 * A test set is a set of test cases
 *
 * Created by jordam on 2016-08-17.
 */
//@RunWith(se.claremont.autotest.common.testrun.TafTestRunner.class)
@JsonIgnoreProperties({"currentTestCase", "currentTestNameInternal"})
public abstract class TestSet { //non-abstract although it should be, to enable JSON object mapping with ease
    List<TestCase> currentTestCases = new ArrayList<>();
    @JsonProperty public String name;
    @JsonProperty public final KnownErrorsList knownErrorsList = new KnownErrorsList();
    static Object testSet;

    @Rule
    final public TestRule traceTestWatcher = new TestWatcher() {
        @Override
        protected void starting(Description d) {
            addTestSetToRunnerIfNotAlreadyThere();
            startUpTestCase(d.getMethodName(), d.getClassName());
        }
    };

    /**
     * Setting up a new test set instance
     */
    public TestSet(){
        testSet = this;
        TestRun.initializeIfNotInitialized();
        name = SupportMethods.classNameAtStacktraceLevel(3);
    }

    public TestCase currentTestCase(){
        for(TestCase testCase : currentTestCases){
            if(testCase.testName.equals(new TestName().getMethodName())) return testCase;
        }
        if(currentTestCases.size() == 1) return currentTestCases.get(0);
        return null;
    }

    private void addTestSetToRunnerIfNotAlreadyThere(){
        boolean testSetRegisteredInRunner = TafParallelTestCaseRunner.testSetNames.contains(name);
        for(String testSetName : TafParallelTestCaseRunner.testSetNames){
            if(testSetName.equals(this.name)) testSetRegisteredInRunner = true;
        }
        if(!testSetRegisteredInRunner) {
            TafParallelTestCaseRunner.testSetNames.add(name);
            TafParallelTestCaseRunner.testSets.add(this);
        }
    }

    @After
    public void testSetClassInternalTearDown(){
        wrapUpTestCase();
    }

    public void setCurrentTestCaseTestName(String name){
        if(currentTestCase() == null)return;
        currentTestCase().setName(name);
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     * @param description The text string describing the error
     * @param regexPatternsForLogRowsThatMustOccur TestCaseLog patterns to find in the test case execution testCaseLog
     */
    @SuppressWarnings("unused")
    public void addKnownError(String description, String[] regexPatternsForLogRowsThatMustOccur){
        knownErrorsList.add(new KnownError(description, regexPatternsForLogRowsThatMustOccur));
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     * @param description The text string describing the error
     * @param regexPatternForLogRow TestCaseLog pattern to find in the test case execution testCaseLog
     */
    public void addKnownError(String description, String regexPatternForLogRow){
        knownErrorsList.add(new KnownError(description, regexPatternForLogRow));
    }

    /**
     * Procedures common for all test cases
     * @param testName The name of the test, for reporting purposes.
     */
    public void startUpTestCase(String testName, String testSetName){
        currentTestCases.add(new TestCase(knownErrorsList, testName, testSetName));
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    /**
     * Closes down test case execution.
     * Test case tear down procedure at the test set level
      */
    protected void wrapUpTestCase(){
        if(currentTestCase() == null)return;
        currentTestCase().report();
        currentTestCases.remove(currentTestCase());
    }



}
