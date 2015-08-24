package de.ppi.selenium.logevent.api;

import java.sql.Timestamp;

/**
 * A data-container with event-data.
 *
 */
public class EventData {

    /**
     * Technical unique-id.
     */
    private Long id;

    /**
     * when this event occurs.
     */
    private Timestamp ts;

    /**
     * The id of a testrun.
     */
    private String testrunId;

    /**
     * The id of the thread @see {@link Thread#getId()}.
     */
    private long threadId;

    /**
     * The source of the event.
     */
    private EventSource source;

    /**
     * Identifier which defines a group of items for the given source, i.e.:
     * <ul>
     * <li>Unittest-Class groups unittest methods</li>
     * <li>A webelement could be part of page ./li>
     * </ul>
     * If no group exists insert the item.
     */
    private String group;

    /**
     * The item which sends the event.
     */
    private String item;

    /**
     * The action, so what has happens.
     */
    private String action;

    /**
     * The Priority.
     */
    private Priority priority;

    /** A description of what has happens. */
    private String description;

    /**
     * A list of possible arguments for the description.
     */
    private Object[] arguments;

    /**
     * Screenshottype png or html.
     */
    private String screenShotType;

    /**
     * A screenshot as byte-array.
     */
    private byte[] screenshot;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the ts
     */
    public Timestamp getTs() {
        return ts;
    }

    /**
     * @param ts the ts to set
     */
    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    /**
     * @return the testrunId
     */
    public String getTestrunId() {
        return testrunId;
    }

    /**
     * @param testrunId the testrunId to set
     */
    public void setTestrunId(String testrunId) {
        this.testrunId = testrunId;
    }

    /**
     * @return the source
     */
    public EventSource getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(EventSource source) {
        this.source = source;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the arguments
     */
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    /**
     * @return the screenshot
     */
    public byte[] getScreenshot() {
        return screenshot;
    }

    /**
     * @param screenshot the screenshot to set
     */
    public void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot;
    }

    /**
     * @return the threadId
     */
    public long getThreadId() {
        return threadId;
    }

    /**
     * @param threadId the threadId to set
     */
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    /**
     * @return the type
     */
    public String getScreenShotType() {
        return screenShotType;
    }

    /**
     * @param type the type to set
     */
    public void setScreenShotType(String type) {
        this.screenShotType = type;
    }

}
