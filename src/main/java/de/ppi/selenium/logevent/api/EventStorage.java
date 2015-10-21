package de.ppi.selenium.logevent.api;

/**
 * Storage for the events with the following workflow:
 * <ol>
 * <li>open - could be called at the beginning, must be called after a close.</li>
 * <li>insert - should be called to insert some testdata.</li>
 * <li>write - persist the data, at end of testmethod.</li>
 * <li>close - should be called at the end of all tests.</li>
 * </ol>
 *
 */
public interface EventStorage {

    /**
     * Open the storage system.
     */
    void open();

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
    ClosableIterable<EventData> getAllEvents(String testrunId);

    /**
     * Delivers all Events which are a start or the end of a test as an
     * Iterable.
     *
     * @return an Iterable of {@link EventData}.
     */
    ClosableIterable<EventData> getAllStartAndFinishEvents();

}
