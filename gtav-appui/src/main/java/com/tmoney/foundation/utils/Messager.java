package com.tmoney.foundation.utils;

/**
 * 〈错误日志内容〉
 *
 * @author zhzh.yin
 * @create 2021/12/22
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.platform.engine.reporting.ReportEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ReportMessage is used for reporting informational and error messages both
 * using logger and testsNG Reporter.
 *
 * @author akhursevich
 */
@Slf4j
public enum Messager
{
    TEST_STARTED("INFO: TEST [%s] STARTED at [%s]"),

    TEST_PASSED("INFO: TEST [%s] PASSED at [%s]"),

    TEST_FAILED("INFO: TEST [%s] FAILED at [%s] - %s"),

    TEST_RESULT("RESULT #%s: TEST [%s] %s [%s]"),

    OPEN_URL("INFO: url '%s' is opened."),

    VALIDATION_FAIL("FAIL: '%s' !"),

    INROMATION("INFO: '%s'."),

    ERROR("ERROR: '%s'!"),

    PAUSE("INFO: pause for '%s' seconds."),

    BACK("INFO: navigate to previous page performed."),

    REFRESH("INFO: refresh performed."),

    EXPECTED_URL("PASS: url '%s' is correct."),

    UNEXPECTED_URL("FAIL: wrong URL, expected '%s' but actual '%s'!"),

    ELEMENT_PRESENT("PASS: element '%s' presents."),

    ELEMENT_NOT_PRESENT("FAIL: element '%s' does not present!"),

    ELEMENT_NOT_PRESENT_PASS("PASS: element '%s' does not present"),

    ELEMENT_WITH_ATTRIBUTE_PRESENT("PASS: element '%s' with attribute '%s' = '%s' presents."),

    ELEMENT_WITH_ATTRIBUTE_NOT_PRESENT("FAIL: element '%s' with attribute '%s' = '%s' does not present!"),

    ELEMENT_WITH_TEXT_PRESENT("PASS: element '%s' with text '%s' presents."),

    ELEMENT_WITH_TEXT_NOT_PRESENT("FAIL: element '%s' with text: '%s' does not present!"),

    UNEXPECTED_ELEMENT_PRESENT("FAIL: unexpected element '%s' presents!"),

    UNEXPECTED_ELEMENT_WITH_TEXT_PRESENT("FAIL: unexpected element '%s' with text '%s' presents!"),

    ELEMENT_CLICKED("PASS: element '%s' is clicked."),

    ELEMENT_NOT_CLICKED("FAIL: element '%s' is not clicked!"),

    ELEMENT_HOVERED("PASS: element '%s' is hovered."),

    ELEMENT_NOT_HOVERED("FAIL: element '%s' is not hovered!"),

    ELEMENTS_DRAGGED_AND_DROPPED("PASS: element '%s' is dragged and dropped to '%s'."),

    ELEMENTS_NOT_DRAGGED_AND_DROPPED("FAIL: element '%s' is not dragged and dropped to '%s'!"),

    KEYS_SEND_TO_ELEMENT("PASS: keys '%s' are sent to element '%s'."),

    KEYS_NOT_SEND_TO_ELEMENT("FAIL: keys '%s' are not sent to element '%s'!"),

    TITLE_CORERECT("PASS: page '%s' has correct title '%s'."),

    TITLE_NOT_CORERECT("FAIL: unexpected title for page '%s', expected '%s' but actual '%s'!"),

    TITLE_DOES_NOT_MATCH_TO_PATTERN("FAIL: unexpected title for page '%s', expected pattern is '%s' but actual '%s'!"),

    STEPS_TO_REPRODUCE("Steps to reproduce test failure: \r\n%s"),

    FILE_ATTACHED("PASS: file '%s' is attached."),

    FILE_NOT_ATTACHED("FAIL: file '%s' is not attached."),

    SELECT_TEXT("PASS: text '%s' was selected in %s."),

    SELECT_INDEX("PASS: index '%s' was selected in %s."),

    TEST_FAILURES("Test failures: \r\n%s"),

    TEST_CONFIGURATION("INFO: Test configuration: Browser='%s'; Base URL='%s'; Grid host='%s'."),

    HOVER_IMG("PASS: img '%s' was hovered"),

    ALERT_ACCEPTED("PASS: alert was accepted."),

    ALERT_NOT_ACCEPTED("FAIL: alert was not accepted!"),

    ALERT_CANCELED("PASS: alert was cancelled."),

    ALERT_NOT_CANCELED("FAIL: alert was not cancelled!"),

    SELECT_BY_TEXT_PERFORMED("PASS: text '%s' was selected in '%s'."),

    SELECT_BY_TEXT_NOT_PERFORMED("FAIL: text '%s' was NOT selected in '%s'."),

    SELECT_BY_MATCHER_TEXT_PERFORMED("PASS: value by matcher '%s' was selected in '%s'."),

    SELECT_BY_MATCHER_TEXT_NOT_PERFORMED("FAIL: value by matcher '%s' was NOT selected in '%s'."),

    SELECT_BY_INDEX_PERFORMED("PASS: index '%s' was selected in '%s'."),

    SELECT_BY_INDEX_NOT_PERFORMED("FAIL: index '%s' was NOT selected in '%s'."),

    CHECKBOX_CHECKED("PASS: checkbox '%s' was checked."),

    CHECKBOX_UNCHECKED("PASS: index '%s' was unchecked."),

    SLIDER_MOVED("PASS: silder '%s' was moved by offset X:'%s' Y:'%s'."),

    SLIDER_NOT_MOVED("FAIL: silder '%s' was moved by offset X:'%s' Y:'%s'!");


    private String pattern;

    Messager(String pattern)
    {
        this.pattern = pattern;
    }

    public String getMessage(String... args)
    {
        return create(args);
    }

    /**
     * Logs info message using message pattern and incoming parameters.
     *
     * @param args
     *            for insert into patterns
     * @return generated message
     */
    public String info(String... args)
    {
        String message = create(args);
        log.info(message);
        return message;
    }

    /**
     * Logs error message and adds message to TestNG report.
     *
     * @param args
     *            for insert into patterns
     * @return generated message
     */
    public String error(String... args)
    {
        String message = create(args);
//        ReportEntry.log(message);
        log.error(message);
        return message;
    }

    /**
     * Logs info message and adds message to TestNG report.
     *
     * @param args
     *            for insert into patterns
     * @return generated message
     */
/*    public String report(String... args)
    {
        String message = create(args);
        log.info(message);
        return message;
    }*/

    /**
     * Generates error message using message pattern and incoming parameters.
     *
     * @param args
     *            for insert into pattern
     * @return generated message
     */
    private String create(String... args)
    {
        String message = "";
        try
        {
            message = String.format(pattern, (Object[]) args);
        }
        catch (Exception e)
        {
            log.error("Report message creation error!");
        }
        return message;
    }
}
