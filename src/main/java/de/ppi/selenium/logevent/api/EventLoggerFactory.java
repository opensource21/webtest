package de.ppi.selenium.logevent.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create {@link EventLogger}.
 *
 */
public final class EventLoggerFactory {

    /**
     * Map which defines priorities on which the event should be logged. If the
     * logevent has a priotity higher or equal then the defined priority, the
     * event will be logged.
     */
    private static final Map<String, Priority> PRIORITIES =
            new HashMap<String, Priority>();

    /**
     * Map which defines priorities on which a screenshot should be created. If
     * the logevent has a priotity higher or equal then the defined priority, a
     * screenshot will maked.
     */
    private static final Map<String, Priority> SCREENSHOT_PRIORITIES =
            new HashMap<String, Priority>();

    /** The storage system. */
    private static EventStorage storage;

    /** The source of the event. */
    private final EventSource eventSource;

    /**
     * Initiates an object of type EventLoggerFactory.
     *
     * @param eventSource the source of the event.
     */
    private EventLoggerFactory(EventSource eventSource) {
        super();
        this.eventSource = eventSource;
    }

    /**
     * Creates an {@link EventLogger} with priority {@link Priority#DEBUG}.
     *
     * @param group the group of the event.
     * @param item the item of the event.
     * @return a {@link EventLogger}.
     */
    public EventLogger onDebug(String group, String item) {
        return on(Priority.DEBUG, group, item);
    }

    /**
     * Creates an {@link EventLogger} with priority
     * {@link Priority#DOCUMENTATION}.
     *
     * @param group the group of the event.
     * @param item the item of the event.
     * @return a {@link EventLogger}.
     */
    public EventLogger onDoku(String group, String item) {
        return on(Priority.DOCUMENTATION, group, item);
    }

    /**
     * Creates an {@link EventLogger} with priority {@link Priority#FAILURE}.
     *
     * @param group the group of the event.
     * @param item the item of the event.
     * @return a {@link EventLogger}.
     */
    public EventLogger onFailure(String group, String item) {
        return on(Priority.FAILURE, group, item);
    }

    /**
     * Creates an {@link EventLogger} with priority {@link Priority#EXCEPTION}.
     *
     * @param group the group of the event.
     * @param item the item of the event.
     * @return a {@link EventLogger}.
     */
    public EventLogger onException(String group, String item) {
        return on(Priority.EXCEPTION, group, item);
    }

    /**
     * Creates an {@link EventLogger} with given priority.
     *
     * @param priority the priority of the {@link EventLogger}.
     * @param group the group of the event.
     * @param item the item of the event.
     * @return a {@link EventLogger}.
     */
    public EventLogger on(Priority priority, String group, String item) {
        if (priority.isMoreImportantThan(
                getPriority(PRIORITIES, eventSource, group, item))) {
            return new EventLoggerImpl(
                    storage, priority, getPriority(SCREENSHOT_PRIORITIES,
                            eventSource, group, item),
                    eventSource, group, item);
        } else {
            return new EmptyLogger();
        }
    }

    /**
     * Gets the priority from the map.
     *
     * @param map the map with priorities.
     * @param eventSource the event source.
     * @param group the name of the group.
     * @param item the item of the group.
     * @return the priority.
     */
    private Priority getPriority(Map<String, Priority> map,
            EventSource eventSource, String group, String item) {
        final String key1 = createKey(eventSource, group, item);
        Priority priority = map.get(key1);
        if (priority == null) {
            synchronized (map) {
                final String key2 = createKey(eventSource, null, null);
                priority = map.get(key2);
                if (priority == null) {
                    priority = map.get(eventSource.name());
                    map.put(key2, priority);
                }
                map.put(key1, priority);
            }
        }
        return priority;
    }

    /**
     * Creates a key for the given data.
     *
     * @param eventSource the event source.
     * @param group the name of the group.
     * @param item the item of the group.
     * @return the key.
     */
    private String createKey(EventSource eventSource, String group,
            String item) {
        final StringBuilder keyBuilder = new StringBuilder(eventSource.name());
        if (group != null) {
            keyBuilder.append('.').append(group);
            if (item != null) {
                keyBuilder.append('.').append(item);
            }
        }
        return keyBuilder.toString();
    }

    /**
     * Create an instance, so it's more the common logging feeling.
     *
     * @param source the event-source.
     * @return the factory.
     */
    public static EventLoggerFactory getInstance(EventSource source) {
        return new EventLoggerFactory(source);
    }

    /**
     * @param storage the {@link EventStorage} to set.
     */
    public static synchronized void setStorage(EventStorage storage) {
        EventLoggerFactory.storage = storage;
    }

    /**
     * Define the default priority.
     *
     * @param priority the default priority.
     */
    public void setDefaultPriority(Priority priority) {
        for (EventSource source : EventSource.values()) {
            PRIORITIES.put(source.name(), priority);
        }
        if (SCREENSHOT_PRIORITIES.isEmpty()) {
            for (EventSource source : EventSource.values()) {
                SCREENSHOT_PRIORITIES.put(source.name(), priority);
            }
        }
    }

    /**
     * Define the normal priority and if no other value exist for the
     * screenshot-priority.
     *
     * @param priority the priority.
     * @param eventSource the event source.
     * @param group the name of the group.
     * @param item the item of the group.
     */
    public void setPriority(Priority priority, EventSource eventSource,
            String group, String item) {
        final String key = createKey(eventSource, group, item);
        PRIORITIES.put(key, priority);
        if (!SCREENSHOT_PRIORITIES.containsKey(key)) {
            SCREENSHOT_PRIORITIES.put(key, priority);
        }
    }

    /**
     * Define the default priority for screenshots.
     *
     * @param priority the default priority for screenshots.
     */
    public void setDefaultScreenPriority(Priority priority) {
        for (EventSource source : EventSource.values()) {
            SCREENSHOT_PRIORITIES.put(source.name(), priority);
        }
    }

    /**
     * Define the normal priority and if no other value exist for the
     * screenshot-priority.
     *
     * @param priority the priority.
     * @param eventSource the event source.
     * @param group the name of the group.
     * @param item the item of the group.
     */
    public void setScreenshotPriority(Priority priority,
            EventSource eventSource, String group, String item) {
        final String key = createKey(eventSource, group, item);
        SCREENSHOT_PRIORITIES.put(key, priority);
    }

}
