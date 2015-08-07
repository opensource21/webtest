package de.ppi.selenium.logevent.api;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class EventLoggerFactory
 *
 */
// TODO JAVADOC und detailierte Config.
public class EventLoggerFactory {

    /**
     * The Logger for the controller.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(EventLoggerFactory.class);

    private final Map<String, Priority> GLOBAL_PRIORITIES =
            new HashMap<String, Priority>();

    private final Map<String, Priority> GLOBAL_SCREENSHOT_PRIORITIES =
            new HashMap<String, Priority>();

    private static EventStorage STORAGE;

    private final EventSource eventSource;

    /**
     * Initiates an object of type EventLoggerFactory.
     *
     * @param eventSource
     */
    public EventLoggerFactory(EventSource eventSource) {
        super();
        this.eventSource = eventSource;
    }

    public EventLogger onDebug(String group, String categegory) {
        return on(Priority.DEBUG, group, categegory);
    }

    public EventLogger onDoku(String group, String categegory) {
        return on(Priority.DOKU, group, categegory);
    }

    public EventLogger onFailure(String group, String categegory) {
        return on(Priority.FAILURE, group, categegory);
    }

    public EventLogger onException(String group, String categegory) {
        return on(Priority.EXCEPTION, group, categegory);
    }

    public EventLogger on(Priority priority, String group, String categegory) {
        if (priority.isMoreImportantThan(getPriority(GLOBAL_PRIORITIES,
                eventSource, group, categegory))) {
            return new EventLoggerImpl(STORAGE, priority, getPriority(
                    GLOBAL_SCREENSHOT_PRIORITIES, eventSource, group,
                    categegory), eventSource, group, categegory);
        } else {
            return new EmptyLogger();
        }
    }

    /**
     * Gets the priority from the map.
     *
     * @param map
     * @param eventSource
     * @param group
     * @param item
     * @return
     */
    private Priority getPriority(Map<String, Priority> map,
            EventSource eventSource, String group, String item) {
        final String key1 = eventSource.name() + "." + group + "." + item;
        Priority priority = map.get(key1);
        if (priority == null) {
            synchronized (map) {
                final String key2 = eventSource.name() + "." + group;
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

    public static EventLoggerFactory getInstance(EventSource source) {
        return new EventLoggerFactory(source);
    }

    /**
     * @param storage the {@link EventStorage} to set.
     */
    public static synchronized void setSTORAGE(EventStorage storage) {
        STORAGE = storage;
    }

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

    public void setDefaultScreenPriority(Priority priority) {
        for (EventSource source : EventSource.values()) {
            GLOBAL_SCREENSHOT_PRIORITIES.put(source.name(), priority);
        }
    }

}
