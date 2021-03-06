package se.claremont.autotest.mqsupport;

import com.ibm.mq.MQChannelDefinition;
import se.claremont.autotest.common.testcase.TestCase;

public class Channel {

    private TestCase testCase;
    private String name;
    MQChannelDefinition channel;

    public Channel(TestCase testCase, String name, MQChannelDefinition channel){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        this.name = name;
        this.channel = channel;
    }

    public ChannelVerification verify(){
        return new ChannelVerification(testCase, this);
    }
}
