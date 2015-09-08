package de.ppi.selenium.logevent.api;

/**
 * Possible Priorities.
 *
 */
public enum Priority {

    /** Lowest Prio. */
    DEBUG(10),
    /** Documenation. */
    DOCUMENTATION(20),
    /** Assertion happens. */
    FAILURE(30),
    /** Exception happens. */
    EXCEPTION(40);

    /**
     * Internal representation.
     */
    private final int prio;

    /**
     *
     * Initiates an object of type Priority.
     *
     * @param prio the internal number.
     */
    private Priority(int prio) {
        this.prio = prio;
    }

    /**
     * Checks if this priority is more important or equal than the other.
     *
     * @param other a other priority.
     * @return true if this priority is more important or equal than the other.
     */
    public boolean isMoreImportantThan(Priority other) {
        return this.prio >= other.prio;
    }
}
