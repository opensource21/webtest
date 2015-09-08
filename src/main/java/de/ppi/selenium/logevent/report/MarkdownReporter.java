package de.ppi.selenium.logevent.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ppi.selenium.logevent.api.ClosableIterable;
import de.ppi.selenium.logevent.api.EventActions;
import de.ppi.selenium.logevent.api.EventData;
import de.ppi.selenium.logevent.api.EventStorage;
import de.ppi.selenium.logevent.api.Priority;

/**
 * Reports to markdown-files.
 *
 */
public class MarkdownReporter implements LogReporter {

    /** Logger instance. */
    private static final Logger LOG = LoggerFactory
            .getLogger(MarkdownReporter.class);

    /** Parent-Directory for the report. */
    private final File reportDestinationParent;

    /** True if only test with errors should be logged. */
    private final boolean logOnlyOnError;

    /** Priority which is the minimum priority which should be reported. */
    private final Priority priority;

    /**
     * The constant for the regular expresion which is replaced in the export
     * filename.
     */
    private static final String REGEXP_FILENAME_TO_REPLACE = "\\W+";

    /** MessageSource. */
    private final MessageSource messageSource = new MessageSourceImpl();

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
            String testCase = null;
            String testMethod = null;
            File currentReportDir = reportDestination;
            File markdownFile = null;
            PrintWriter markdown = null;
            for (EventData eventData : logdata) {
                final String action = eventData.getAction();
                if (EventActions.TEST_START.equals(action)) {
                    testCase = eventData.getGroupId();
                    testMethod = eventData.getItem();
                    currentReportDir =
                            getTestProtocolDir(reportDestination, testCase,
                                    testMethod);
                    currentReportDir.mkdirs();
                    markdownFile =
                            new File(currentReportDir,
                                    getCleanFilename(eventData.getArgument1()
                                            .toString()) + ".md");
                    markdown = new PrintWriter(markdownFile);
                } else if (EventActions.ASSERTION_FAILED.equals(action)) {
                    FileUtils.writeByteArrayToFile(new File(currentReportDir,
                            eventData.getId()
                                    + ".Assertion "
                                    + getCleanFilename(testMethod + "_"
                                            + eventData.getArgument1()) + "."
                                    + eventData.getScreenShotType()), eventData
                            .getScreenshot());
                } else {
                    if (priority == null
                            || eventData.getPriority().isMoreImportantThan(
                                    priority)) {
                        printToMarkdown(currentReportDir, markdown, testMethod,
                                eventData);
                    }
                }
                if (EventActions.TEST_FINISHED.equals(action)
                        || EventActions.TEST_SKIPPED.equals(action)
                        || EventActions.TEST_FINISHED_WITH_FAILURES
                                .equals(action)
                        || EventActions.TEST_FINISHED_WITH_EXCEPTION
                                .equals(action)) {
                    markdown.close();
                    if (logOnlyOnError) {
                        markdownFile.delete();
                    }
                    markdownFile = null;
                    markdown = null;
                }
                if (EventActions.TEST_FINISHED.equals(action)
                        || EventActions.TEST_SKIPPED.equals(action)) {
                    deleteDirIfEmpty(currentReportDir);
                    deleteDirIfEmpty(currentReportDir.getParentFile());
                }

            }
        } catch (Exception e) {
            throw new IllegalStateException("Problems during report.", e);
        } finally {
            LOG.info("Write to {}.", reportDestination.getAbsolutePath());
        }
        deleteDirIfEmpty(reportDestination);
    }

    /**
     * Create a log to the markdown file.
     *
     * @param currentReportDir the current report-directory for screenshots.
     * @param markdown the markdown-output.
     * @param testMethod the name of the test-method.
     * @param eventData the event-data.
     * @throws IOException error writing data.
     */
    private void printToMarkdown(File currentReportDir, PrintWriter markdown,
            String testMethod, EventData eventData) throws IOException {
        markdown.print("- " + eventData.getTs() + "@" + eventData.getThreadId()
                + "  " + eventData.getPriority() + " " + eventData.getSource()
                + "@" + eventData.getGroupId() + "." + eventData.getItem()
                + ":");
        String message = eventData.getDescription();
        markdown.print(messageSource.getMessage(message, Locale.getDefault(),
                eventData.getSource(), eventData.getGroupId(),
                eventData.getItem(), eventData.getAction(),
                eventData.getArgument1(), eventData.getArgument2(),
                eventData.getArgument3(), eventData.getArgument4()));
        if (ArrayUtils.isNotEmpty(eventData.getScreenshot())) {
            final String screenshotName =
                    eventData.getId()
                            + "."
                            + eventData.getPriority()
                            + "."
                            + getCleanFilename(testMethod + "_"
                                    + eventData.getDescription()) + "."
                            + eventData.getScreenShotType();
            final File screenshotFile =
                    new File(currentReportDir, screenshotName);
            FileUtils.writeByteArrayToFile(screenshotFile,
                    eventData.getScreenshot());
            markdown.print("![Screenshot](" + screenshotName + ")");
        }
        markdown.println();
    }

    /**
     * Create the name of the protocol-directory.
     *
     * @param baseDir the base-directory
     * @param testClass the name of the test-class.
     * @param testMethod the name of the testMethod.
     * @return a protocol directory.
     */
    private File getTestProtocolDir(File baseDir, String testClass,
            String testMethod) {
        File protDir = new File(baseDir, getCleanFilename(testClass));
        protDir = new File(protDir, getCleanFilename(testMethod));
        return protDir;
    }

    /**
     * Deletes the directory it is empty.
     *
     * @param protocolDir the directory.
     */
    private void deleteDirIfEmpty(File protocolDir) {
        if (protocolDir.exists() && protocolDir.listFiles().length == 0) {
            protocolDir.delete();
        }
    }

    /**
     * Replace all problematic characters from the filename with "_".
     *
     * @param filename filename
     * @return a cleaned filename.
     */
    private String getCleanFilename(String filename) {
        return filename.replaceAll(REGEXP_FILENAME_TO_REPLACE, "_");
    }
}
