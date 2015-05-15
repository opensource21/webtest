package de.ppi.selenium.assertj;

import org.assertj.core.api.JUnitSoftAssertions;
import org.openqa.selenium.Alert;
import org.selophane.elements.base.Element;

import de.ppi.selenium.browser.WebBrowser;

/**
 * Selenium specific assertions in the soft-variant.
 *
 * @see JUnitSoftAssertions
 *
 */
public class SeleniumSoftAssertions extends JUnitSoftAssertions {

    /**
     * Creates a new instance of <code>{@link ElementAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public ElementAssert assertThat(Element actual) {
        return proxy(ElementAssert.class, Element.class, actual);
    }

    /**
     * Creates a new instance of <code>{@link AlertAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public AlertAssert assertThat(Alert actual) {
        return proxy(AlertAssert.class, Alert.class, actual);
    }

    /**
     * Creates a new instance of <code>{@link WebbrowserAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public WebbrowserAssert assertThat(WebBrowser actual) {
        return proxy(WebbrowserAssert.class, WebBrowser.class, actual);
    }

}
