package org.selophane.elements.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A locator for a {@link WebElement} which only return one element.
 *
 * @author niels
 *
 */
public interface UniqueElementLocator {

    /**
     * Returns the underlying {@link WebDriver}.
     *
     * @return the underlying {@link WebDriver}.
     */
    WebDriver getWebDriver();

    /**
     * Get the name of the page where the element is.
     * @return the name of the page where the element is.
     */
    String getPageName();

    /**
     * Get the description of the field, including the context.
     * @return the description of the field, including the context.
     */
    String getFieldDescription();

    /**
     * Finds a element.
     *
     * @return the element.
     */
    WebElement findElement();
}
