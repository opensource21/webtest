package de.ppi.selenium.logevent.report;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.ppi.selenium.logevent.api.ClosableIterable;
import de.ppi.selenium.logevent.api.EventData;
import de.ppi.selenium.logevent.api.EventStorage;
import de.ppi.selenium.logevent.api.Priority;

/**
 * Reports to markdown-files.
 *
 */
public class MarkdownReporter implements LogReporter {

    private final Map<String, List<EventData>> testResults =
            new LinkedHashMap<String, List<EventData>>();

    private final File reportDestinationParent;

    private final boolean logOnlyOnError;

    private final Priority priority;

    /**
     * Initiates an object of type MarkdownReporter.
     *
     * @param reportDestination target directory
     * @param logOnlyOnError true if a report should only create if an error has
     *            happened.
     * @param priority Lowest {@link Priority} which should be reported.
     */
    public MarkdownReporter(String reportDestination, boolean logOnlyOnError,
            Priority priority) {
        super();
        this.reportDestinationParent = new File(reportDestination);
        if (!this.reportDestinationParent.exists()
                && !this.reportDestinationParent.mkdirs()) {
            throw new IllegalStateException(
                    this.reportDestinationParent.getAbsolutePath()
                            + " can't be created.");
        }
        this.logOnlyOnError = logOnlyOnError;
        this.priority = priority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReport(EventStorage storage, String testrunId) {
        final File reportDestination =
                new File(reportDestinationParent, testrunId);
        try (ClosableIterable<EventData> logdata =
                storage.getAllEvents(testrunId)) {
            for (EventData eventData : logdata) {

            }

        } catch (Exception e) {
            throw new IllegalStateException("Problems during report.", e);
        }
    }
}
