package de.ppi.selenium.logevent;

/**
 * Storage for the events with the following workflow:
 * <ol>
 * <li>open - should be called at the beginning</li>
 * <li>startInsert - should be called at the beginning of a testmethod.</li>
 * <li>insert - should be called to insert some testdata.</li>
 * <li>write - persist the data, at end of testmethod.</li>
 * <li>close - should be called at the end of all tests.</li>
 * </ol>
 *
 */
public interface EventStorage {

    /**
     * Open the storage system.
     *
     * @param testrunId the id of the testrun.
     */
    void open(String testrunId);

    /**
     * Start inserting data of a test.
     *
     * @param testclassname name of the test-class.
     * @param testmethodname name of the test-method.
     */
    void startInsert(String testclassname, String testmethodname);

    /**
     * Collect the testdata.
     *
     * @param eventType the type of an event.
     * @param browserId the id of the browser.
     * @param description the description.
     * @param screenshot the screenshot (optional)
     */
    void insert(String eventType, String browserId, String description,
            byte[] screenshot);

    /**
     * Writes the collected data.
     */
    void write();

    /**
     * Close the storage system.
     */
    void close();

    /**
     * Returns the browser-id of the last insert.
     *
     * @return the browser-id of the last insert.
     */
    String getLastBrowserId();
}
