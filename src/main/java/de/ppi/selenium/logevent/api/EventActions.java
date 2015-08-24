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

    /**
     * An assertion has failed.
     */
    String ASSERTION_FAILED = "ASSERTION_FAILED";

    /**
     * A new webdriver instance is created.
     */
    String WEBDRIVER_CREATE_INSTANCE = "WEBDRIVER_CREATE_INSTANCE";

}
// CSON: InterfaceIsType
