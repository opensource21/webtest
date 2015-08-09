package de.ppi.selenium.logevent.api;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class EventLoggerFactory
 *
 */
// TODO JAVADOC .
public final class EventLoggerFactory {

    /**
     * The Logger for the controller.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(EventLoggerFactory.class);

    private static final Map<String, Priority> GLOBAL_PRIORITIES =
            new HashMap<String, Priority>();

    private static final Map<String, Priority> GLOBAL_SCREENSHOT_PRIORITIES =
            new HashMap<String, Priority>();

    private static EventStorage STORAGE;

    private final EventSource eventSource;

    /**
     * Initiates an object of type EventLoggerFactory.
     *
     * @param eventSource
     */
    private EventLoggerFactory(EventSource eventSource) {
        super();
        this.eventSource = eventSource;
    }

    public EventLogger onDebug(String group, String item) {
        return on(Priority.DEBUG, group, item);
    }

    public EventLogger onDoku(String group, String item) {
        return on(Priority.DOCUMENTATION, group, item);
    }

    public EventLogger onFailure(String group, String item) {
        return on(Priority.FAILURE, group, item);
    }

    public EventLogger onException(String group, String item) {
        return on(Priority.EXCEPTION, group, item);
    }

    public EventLogger on(Priority priority, String group, String item) {
        if (priority.isMoreImportantThan(getPriority(GLOBAL_PRIORITIES,
                eventSource, group, item))) {
            return new EventLoggerImpl(STORAGE, priority, getPriority(
                    GLOBAL_SCREENSHOT_PRIORITIES, eventSource, group, item),
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
    private String
            createKey(EventSource eventSource, String group, String item) {
        final StringBuilder keyBuilder = new StringBuilder(eventSource.name());
        if (group != null) {
            keyBuilder.append('.').append(group);
            if (item != null) {
                keyBuilder.append('.').append(item);
            }
        }
        return keyBuilder.toString();
    }

    public static EventLoggerFactory getInstance(EventSource source) {
        return new EventLoggerFactory(source);
    }

    /**
     * @param storage the {@link EventStorage} to set.
     */
    public static synchronized void setSTORAGE(EventStorage storage) {
        STORAGE = storage;
    }

    /**
     * Define the default priority.
     *
     * @param priority the default priority.
     */
    public void setDefaultPriority(Priority priority) {
        for (EventSource source : EventSource.values()) {
            GLOBAL_PRIORITIES.put(source.name(), priority);
        }
        if (GLOBAL_SCREENSHOT_PRIORITIES.isEmpty()) {
            for (EventSource source : EventSource.values()) {
                GLOBAL_SCREENSHOT_PRIORITIES.put(source.name(), priority);
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
        GLOBAL_PRIORITIES.put(key, priority);
        if (!GLOBAL_SCREENSHOT_PRIORITIES.containsKey(key)) {
            GLOBAL_SCREENSHOT_PRIORITIES.put(key, priority);
        }
    }

    /**
     * Define the default priority for screenshots.
     *
     * @param priority the default priority for screenshots.
     */
    public void setDefaultScreenPriority(Priority priority) {
        for (EventSource source : EventSource.values()) {
            GLOBAL_SCREENSHOT_PRIORITIES.put(source.name(), priority);
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
        GLOBAL_SCREENSHOT_PRIORITIES.put(key, priority);
    }

}
