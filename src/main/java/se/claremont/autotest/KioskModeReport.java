package se.claremont.autotest;

import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.support.SupportMethods;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jordam on 2016-12-22.
 * Modified by percar on 2017-01-23
 *  added addTableRows method
 */
public class KioskModeReport {
    String html = null;
    String filePath = null;
    ArrayList<String> tableRows = new ArrayList<>();

    public void evaluateTestCase(TestCase testCase){
        if (testCase.resultStatus.equals(TestCase.ResultStatus.UNEVALUATED)){
            testCase.evaluateResultStatus();
        }
        switch (testCase.resultStatus){
            case UNEVALUATED:
                addTableRows(testCase.testSetName, testCase.testName, "unevaluated", "Unevaluated");
                break;
            case PASSED:
                addTableRows(testCase.testSetName, testCase.testName, "unevaluated", "Passed");
                break;
            case FAILED_WITH_ONLY_KNOWN_ERRORS:
                addTableRows(testCase.testSetName, testCase.testName, "unevaluated", "Known errors");
                break;
            case FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS:
                addTableRows(testCase.testSetName, testCase.testName, "unevaluated", "New and known errors");
                break;
            case FAILED_WITH_ONLY_NEW_ERRORS:
                addTableRows(testCase.testSetName, testCase.testName,"newerrors", "New errors");
                break;
        }
    }
    private void addTableRows(String testCaseTestSetName, String testCaseName, String styleClass, String userFriendlyStatus) {
        tableRows.add("      <tr class=\"" + styleClass + "\"><td>" + userFriendlyStatus + "</td><td>" + testCaseTestSetName + "</td><td>" + testCaseName + "</td></tr>" + System.lineSeparator());
    }
    public void create(String filePath, String title, int reloadIntervalInSeconds){
        this.filePath = filePath;
        html = "<!DOCTYPE html>" + System.lineSeparator();
        html += "<hmtl>" + System.lineSeparator();
        html += "  <head>" + System.lineSeparator();
        html += "    <meta http-equiv=\"refresh\" content=\"" + reloadIntervalInSeconds + "\" >" + System.lineSeparator();
        html += "    <style>" + System.lineSeparator();
        html += styleInfo();
        html += "    </style>" + System.lineSeparator();
        html += "  </head>" + System.lineSeparator();
        html += "  <body>" + System.lineSeparator();
        html += "    <h1>" + title + "</h2>" + System.lineSeparator();
        html += "    <table>" + System.lineSeparator();

        for(String resultRow : tableRows){
            html += resultRow;
        }
        html += "    </table>" + System.lineSeparator();
        html += "    <div class=\"lastruntimestamp\">Last updated " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "</div>" + System.lineSeparator();
        html += "  </body>" + System.lineSeparator();
        html += "</hmtl>" + System.lineSeparator();
        save();
    }

    private void save(){
        SupportMethods.saveToFile(html, filePath);
    }

    public void openInDefaultBrowser(){
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            System.out.println("Something went wrong trying to open the KioskModeReport in default browser. Filepath = '" + filePath + "'. Error: " + e.getMessage());
        }
    }

    private String styleInfo(){
        return
                "      body            { background-color: black; }" + System.lineSeparator() +
                "      h1              { color: lightgrey; }" + System.lineSeparator() +
                "      tr.passed       { background-color: lightgreen; }" + System.lineSeparator() +
                "      tr.newerrors    { background-color: red; color: white; }" + System.lineSeparator() +
                "      tr.newandknownerrors { background-color: orange; color: black; }" + System.lineSeparator() +
                "      tr.onlyknownerrors   { background-color: yellow; color: darkslategrey; }" + System.lineSeparator() +
                "      tr.unevaluated       { background-color: grey; color: black; }" + System.lineSeparator() +
                "      div.lastruntimestamp { color: lightgrey; }" + System.lineSeparator();
    }

}
