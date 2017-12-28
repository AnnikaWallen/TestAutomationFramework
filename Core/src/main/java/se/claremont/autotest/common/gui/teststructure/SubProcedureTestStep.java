package se.claremont.autotest.common.gui.teststructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.LinkedList;
import java.util.List;

public class SubProcedureTestStep extends TestStep {

    @JsonProperty
    public List<TestStep> testSteps = new LinkedList<>();

    public SubProcedureTestStep(){}

    public SubProcedureTestStep(String name, String description) {
        super(name, description);
    }

    @Override
    public String asCode() {
        StringBuilder sb = new StringBuilder();
        for(TestStep testStep : testSteps){
            sb.append(testStep.asCode()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public void setAssociatedData(Object data) {
        this.data = data;
    }

    public void addTestStep(TestStep testStep){
        testSteps.add(testStep);
    }

    @Override
    public String getTestStepTypeShortName() {
        return "Sub";
    }

    @Override
    public TestStepResult execute() {
        TestCaseManager.startTestStep();
        TestCaseManager.setToBePartOfSubProcedureTestStep();
        TestStepResult testStepResult = new TestStepResult(this, TestStepResult.Result.NOT_RUN);
        for(TestStep testStep : testSteps){
            testStepResult.merge(testStep.execute());
        }
        TestCaseManager.wrapUpTestCase();
        TestCaseManager.setNotToBePartOfSubProcedureTestStep();
        return testStepResult;
    }
}
