package de.ppi.selenium.logevent.report;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Default-Implementation.
 *
 */
public class MessageSourceImpl implements MessageSource {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(String key, Locale locale, Object... arguments) {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("WebTestMessages", locale);
        final String message =
                resourceBundle.containsKey(key) ? resourceBundle.getString(key)
                        : key;
        MessageFormat messageFormat = new MessageFormat(message, locale);

        return messageFormat.format(arguments);
    }
}
