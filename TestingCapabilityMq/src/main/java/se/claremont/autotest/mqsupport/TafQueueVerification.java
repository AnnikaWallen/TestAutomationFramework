package se.claremont.autotest.mqsupport;

import com.ibm.mq.MQQueue;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

public class TafQueueVerification {

    private MQQueue queue;
    private TestCase testCase;

    public TafQueueVerification(TestCase testCase, MQQueue queue){
        this.testCase = testCase;
        this.queue = queue;
    }

    public TafQueueVerification exist(){
        if(queue == null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected queue to exist, but it did not.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Queue existed, as expected.");
        }
        return this;
    }

    public TafQueueVerification doesNotExist(){
        if(queue != null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected queue to not exist, but it did.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Queue was expected not to exist, and it did not.");
        }
        return this;
    }
}
