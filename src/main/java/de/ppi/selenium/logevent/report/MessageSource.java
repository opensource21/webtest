package de.ppi.selenium.logevent.report;

import java.util.Locale;

/**
 * A Message-Source.
 *
 */
public interface MessageSource {

    /**
     * Get a Message.
     *
     * @param key the key
     * @param locale the locale
     * @param arguments the argument for the message.
     * @return the message.
     */
    String getMessage(String key, Locale locale, Object... arguments);

}
