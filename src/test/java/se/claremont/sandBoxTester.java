package se.claremont;

import org.fest.swing.core.MouseButton;
import org.fest.swing.core.TypeMatcher;
import org.fest.swing.fixture.FrameFixture;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
//import qtpUsageAnalysis.GUI;
import se.claremont.autotest.common.*;
import se.claremont.autotest.filetestingsupport.FileTester;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.SwingApplication;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.SwingElement;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.SwingWindow;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.ApplicationManager;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.SwingInteractionMethods;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.ResponsiveAnalysis;
import se.claremont.autotest.guidriverpluginstructure.websupport.ResponsiveAnalysis2;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebDriverManager;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.restsupport.RestSupport;
import se.claremont.autotest.support.PerformanceTimer;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * SandBox (UnitTest) containing various help tools for Test Automation Engineers.
 *
 * Created by magnusolsson on 2016-10-06.
 */
public class sandBoxTester extends TestSet{

    private static String OUTPUT_FILE_PATH = "";
    private static String ENDPOINT_TARGET_URL = "https://www.typeandtell.com/sv/";
    private static String LOCAL_MOCH_HTML_FILE = "";
    @SuppressWarnings("CanBeFinal")
    @Rule
    public TestName currentTestName = new TestName();

    @BeforeClass
    public static void classSetup(){
    }

    @Before
    public void testSetup(){
        startUpTestCase(currentTestName.getMethodName());
        name = this.getClass().getSimpleName();
    }

    @After
    public void testTearDown(){
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

    @Before
    public void whoIam() {
        if( Utils.getInstance().amIMacOS() ) {
            OUTPUT_FILE_PATH = Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator + "Temp" + File.separator + "Output.txt";
            LOCAL_MOCH_HTML_FILE = "";
        } else {
            OUTPUT_FILE_PATH = "C:\\Temp\\Output.txt";
            LOCAL_MOCH_HTML_FILE = "file://c:/temp/taf.html";
        }
    }


    @Ignore
    @Test
    public void w3cValidationTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate( ENDPOINT_TARGET_URL );
        web.verifyCurrentPageSourceWithW3validator(false);
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("docselect", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "text input");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTestValueDowsNotExist(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("docselect", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "nonexistingChoice");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTestNoSelectorElement(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("inputregion", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "text input");
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void timerTests(){
        PerformanceTimer timer = new PerformanceTimer("testTimer", currentTestCase);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.stopAndLogTime();
    }

    @Ignore
    @Test
    public void radioButtonTest(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement radiobutton = new DomElement("radiobutton", DomElement.IdentificationType.BY_ID);
        web.chooseRadioButton(radiobutton, " Male");
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void checkBoxTest(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement checkbox = new DomElement("//input[@type='checkbox'][@value='Bike']", DomElement.IdentificationType.BY_X_PATH);
        web.manageCheckbox(checkbox, false);
        web.makeSureDriverIsClosed();
    }

    @Ignore //Takes to much time to run
    @Test
    public void sandboxPlayground(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( ENDPOINT_TARGET_URL );
        web.mapCurrentPage( OUTPUT_FILE_PATH );
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void testResponsive(){
        WebDriverManager webDriverManager = new WebDriverManager(currentTestCase);
        WebDriver driver = webDriverManager.initializeWebDriver(WebDriverManager.WebBrowserType.CHROME);
        driver.get("http://www.claremont.se");
        List<Dimension> resolutions = new ArrayList<>();
        resolutions.add(new Dimension(750, 480));
        resolutions.add(new Dimension(2028, 900));
        ResponsiveAnalysis responsiveAnalysis = new ResponsiveAnalysis(driver, resolutions, currentTestCase);
        responsiveAnalysis.performAnalysisAndReportResults();
        driver.close();
    }


    @Ignore
    @Test
    public void testDesktopScreenshot() {
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("https://www.claremont.se");
        web.saveScreenshot();
    }

    @Ignore
    @Test
    public void testSwingApplicationStart(){
        int javaProcessesBefore = numberOfJavaProcesses();
        SwingInteractionMethods s = new SwingInteractionMethods(currentTestCase);
        List<String> arguments = new ArrayList<>();
        arguments.add("java.exe");
        arguments.add("-jar");
        arguments.add("C:\\Users\\jordam\\OneDrive\\Documents\\Claremont-jobb\\Alster\\QtpUsageAnalysis.jar");
        s.startProgram(String.join(" ", arguments));
        currentTestCase.writeProcessListDeviationsFromSystemStartToLog();
        int javaProcessesAfter = numberOfJavaProcesses();
        currentTestCase.log(LogLevel.INFO, "Number of java processes before: " +javaProcessesBefore + ". Number of java processes after: " + javaProcessesAfter + ".");
    }

    @Ignore
    @Test
    public void testApplicationStart(){
        try {
            Process p = Runtime.getRuntime().exec("java.exe -jar C:\\Users\\jordam\\OneDrive\\Documents\\Claremont-jobb\\Alster\\QtpUsageAnalysis.jar");
            SwingInteractionMethods s = new SwingInteractionMethods(currentTestCase);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p.destroyForcibly();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int numberOfJavaProcesses(){
        ApplicationManager am = new ApplicationManager(currentTestCase);
        List<String> processes = new ArrayList<>();
        int javaProcessCount = 0;
        processes.addAll(am.listActiveRunningProcessesOnLocalMachine());
        for(String string : processes){
            if(string.equals("java.exe")){
                javaProcessCount++;
            }
        }
        return javaProcessCount;

    }

    @Ignore
    @Test
    public void phantomJSdriverTest(){
        WebDriverManager wdm = new WebDriverManager(currentTestCase);
        WebDriver driver = wdm.initializeWebDriver(WebDriverManager.WebBrowserType.PHANTOMJS);
        driver.get("http://www.claremont.se");
        currentTestCase.log(LogLevel.INFO, driver.getTitle());
        currentTestCase.log(LogLevel.INFO, currentTestCase.toJson());
        driver.close();
    }

    @Ignore
    @Test
    public void testSwingFestApplicationAttach(){
        /*GUI gui = new GUI();
        gui.setVisible(true);
        SwingApplication sa = new SwingApplication(gui);
        SwingWindow mainWindow = new SwingWindow(sa, "QTP license server log file reformatter");
        SwingElement.Button browse = new SwingElement.Button(mainWindow, "Browse...");
        SwingInteractionMethods s = new SwingInteractionMethods(currentTestCase);

        s.click(browse);
        s.sleep(3000);
        mainWindow.map();*/

    }

    @Ignore
    @Test
    public void testNewElementMethods(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("http://www.claremont.se");
        currentTestCase.addTestCaseData("Test", "Testcontent");
        DomElement kontaktLink = new DomElement("KONTAKT", DomElement.IdentificationType.BY_LINK_TEXT);
        web.click(kontaktLink);
        web.verifyTextExistOnCurrentPage("Birger Jarlsgatan 7");
        web.verifyPageTitle("fsdf");
        web.makeSureDriverIsClosed();
    }


    @Ignore
    @Test
    public void fileCounterTest(){
        TestCase testCase = new TestCase(null, "dummy");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("http://www.claremont.se");
        web.saveScreenshot();
        web.saveDesktopScreenshot();
        web.saveScreenshot();
        web.saveDesktopScreenshot();
        FileTester fileTester = new FileTester(testCase);
        fileTester.verifyFileExists(LogFolder.testRunLogFolder + testCase.testName + "3.png");
        web.makeSureDriverIsClosed();
        testCase.evaluateResultStatus();
    }

    @Ignore
    @Test
    public void newResolutionAssessment(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("http://www.claremont.se");
        List<Dimension> resolutions = new ArrayList<>();
        resolutions.add(new Dimension(1024, 768));
        resolutions.add(new Dimension(2048, 1024));
        ResponsiveAnalysis2 responsiveAnalysis2 = new ResponsiveAnalysis2(resolutions, web.driver, currentTestCase);
        responsiveAnalysis2.performAnalysis();
        web.makeSureDriverIsClosed();
    }

}
