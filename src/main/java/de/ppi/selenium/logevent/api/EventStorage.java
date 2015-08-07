package de.ppi.selenium.logevent.api;

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
     * @param eventData the eventdata.
     */
    void startBatch(EventData eventData);

    /**
     * Insert the event-data.
     *
     * @param eventData the eventdata.
     */
    void insert(EventData eventData);

    /**
     * Writes the collected data.
     */
    void write();

    /**
     * Close the storage system.
     */
    void close();

    /**
     * Delivers all Events of a testrun as an Iterable.
     *
     * @param testrunId the id of the testrun.
     * @return an Iterable of {@link EventData}.
     */
    Iterable<EventData> getAllEvents(String testrunId);

    /**
     * Returns the browser-id of the last insert.
     *
     * @return the browser-id of the last insert.
     */
    String getLastBrowserId();
}
