package de.ppi.selenium.logevent.api;

/**
 * Interface which defines all Event-Actions. Defined as an Interface so that
 * concrete tests could extend it.
 *
 */
// CSOFF: InterfaceIsType Defined as an Interface so that concrete tests could
// extend it.
public interface EventActions {

    /**
     * Test cancelled with exception.
     */
    String TEST_FINISHED_WITH_EXCEPTION = "TEST_FINISHED_WITH_EXCEPTION";

    /**
     * An exception occurs during test.
     */
    String EXCEPTION_OCCURS = "TEST_EXCEPTION";

    /**
     * Test ends with failures.
     */
    String TEST_FINISHED_WITH_FAILURES = "TEST_FINISHED_WITH_FAILURES";

    /**
     * Test is skipped.
     */
    String TEST_SKIPPED = "TEST_SKIPPED";

    /**
     * Test finished successfully.
     */
    String TEST_FINISHED = "TEST_FINISHED";

    /**
     * Test start.
     */
    String TEST_START = "TEST_START";

    /** Take a screenshot. */
    String TEST_SCREENSHOT = "TEST_SCREENSHOT";

    /** Documentation for the test. */
    String TEST_DOCUMENTATION = "TEST_DOCUMENATION";

    /**
     * An assertion has failed.
     */
    String ASSERTION_FAILED = "ASSERTION_FAILED";

    /**
     * A new webdriver instance is created.
     */
    String WEBDRIVER_CREATE_INSTANCE = "WEBDRIVER_CREATE_INSTANCE";

    /** A CLEAR on a webelement. */
    String ELEMENT_CLEAR = "ELEMENT_CLEAR ";
    /** A CLICK on a webelement. */
    String ELEMENT_CLICK = "ELEMENT_CLICK";
    /** A ELEMENT_WIRED on a webelement. */
    String ELEMENT_ELEMENT_WIRED = "ELEMENT_ELEMENT_WIRED";
    /** A FIND_ELEMENT on a webelement. */
    String ELEMENT_FIND_ELEMENT = "ELEMENT_FIND_ELEMENT";
    /** A FIND_ELEMENTS on a webelement. */
    String ELEMENT_FIND_ELEMENTS = "ELEMENT_FIND_ELEMENTS";
    /** A GET_ATTRIBUTE on a webelement. */
    String ELEMENT_GET_ATTRIBUTE = "ELEMENT_GET_ATTRIBUTE";
    /** A GET_COORDINATES on a webelement. */
    String ELEMENT_GET_COORDINATES = "ELEMENT_GET_COORDINATES ";
    /** A GET_CSS_VALUE on a webelement. */
    String ELEMENT_GET_CSS_VALUE = "ELEMENT_GET_CSS_VALUE";
    /** A GET_ELEMENT on a webelement. */
    String ELEMENT_GET_ELEMENT = "ELEMENT_GET_ELEMENT";
    /** A GET_LOCATION on a webelement. */
    String ELEMENT_GET_LOCATION = "ELEMENT_GET_LOCATION";
    /** A GET_SIZE on a webelement. */
    String ELEMENT_GET_SIZE = "ELEMENT_GET_SIZE";
    /** A GET_TAG_NAME on a webelement. */
    String ELEMENT_GET_TAG_NAME = "ELEMENT_GET_TAG_NAME";
    /** A GET_TEXT on a webelement. */
    String ELEMENT_GET_TEXT = "ELEMENT_GET_TEXT ";
    /** A GET_WRAPPED_ELEMENT on a webelement. */
    String ELEMENT_GET_WRAPPED_ELEMENT = "ELEMENT_GET_WRAPPED_ELEMENT";
    /** A IS_DISPLAYED on a webelement. */
    String ELEMENT_IS_DISPLAYED = "ELEMENT_IS_DISPLAYED";
    /** A IS_ENABLED on a webelement. */
    String ELEMENT_IS_ENABLED = "ELEMENT_IS_ENABLED";
    /** A IS_SELECTED on a webelement. */
    String ELEMENT_IS_SELECTED = "ELEMENT_IS_SELECTED";
    /** A SEND_KEYS on a webelement. */
    String ELEMENT_SEND_KEYS = "ELEMENT_SEND_KEYS ";
    /** A SUBMIT on a webelement. */
    String ELEMENT_SUBMIT = "ELEMENT_SUBMIT";
}
// CSON: InterfaceIsType
