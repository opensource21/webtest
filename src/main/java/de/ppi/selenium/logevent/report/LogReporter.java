package de.ppi.selenium.logevent.report;

import de.ppi.selenium.logevent.api.EventStorage;

/**
 * A reporter for LogEvents.
 *
 */
public interface LogReporter {

    /**
     * Creates a report.
     *
     * @param storage the storage system.
     * @param testrunId the id of the testrun.
     */
    void createReport(EventStorage storage, String testrunId);

}
