/**
 *
 */
package org.selophane.elements.base;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A uniqueElementlocator which finds the element with a {@link By} Definition.
 *
 * @author niels
 *
 */
public class ByUniqueElementLocator implements UniqueElementLocator {

    /**
     * The underlying webdriver.
     */
    private final WebDriver webDriver;

    private final SearchContext searchContext;

    /**
     * Name of the page where the element is.
     */
    private final String pageName;

    /**
     * Description of the field, including the context.
     */
    private final String fieldDescription;

    /**
     * A {@link By} expression to find the element, which should be unique.
     */
    private final By locator;

    /**
     * Create a new instance.
     *
     * @param webDriver the underlying webdriver.
     * @param locator A {@link By} expression to find the element, which should
     *            be unique.
     * @param pageName name of the page where the element is.
     * @param fieldDescription description of the field, including the context.
     *
     */
    public ByUniqueElementLocator(WebDriver webDriver, By locator,
            String pageName, String fieldDescription) {
        this(webDriver, webDriver, locator, pageName, fieldDescription);
    }

    /**
     * create a new instance.
     *
     * @param webDriver the webdriver
     * @param searchContext a special {@link SearchContext}, on which the
     *            locator should work.
     * @param locator the locator
     * @param pageName name of the page where the element is.
     * @param fieldDescription description of the field, including the context.
     */
    public ByUniqueElementLocator(WebDriver webDriver,
            SearchContext searchContext, By locator, String pageName,
            String fieldDescription) {
        super();
        this.webDriver = webDriver;
        this.searchContext = searchContext;
        this.locator = locator;
        this.pageName = pageName;
        this.fieldDescription = fieldDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebElement findElement() {
        return searchContext.findElement(locator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageName() {
        return pageName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldDescription() {
        return fieldDescription;
    }

}
