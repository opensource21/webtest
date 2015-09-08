package org.selophane.elements.factory.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.selophane.elements.base.UniqueElementLocator;

/**
 * Implementation of a {@link UniqueElementLocator} which wraps an existing
 * locator.
 *
 * @author niels
 *
 */
public class LocatorWrappingUniqueElementLocator
        implements UniqueElementLocator {
    /**
     * Underlying locator.
     */
    private final ElementLocator elementLocator;

    /**
     * The underlying {@link WebDriver}.
     */
    private final WebDriver webDriver;

    /**
     * Index which elements of {@link ElementLocator#findElements()} should be
     * used.
     */
    private final int index;

    /**
     * Name of the page where the element is.
     */
    private final String pageName;

    /**
     * Description of the field, including the context.
     */
    private final String fieldDescription;

    /**
     * Creates a new element locator.
     *
     * @param webDriver the underlying webdriver
     * @param elementLocator a elementLocator
     * @param index Index which elements of
     *            {@link ElementLocator#findElements()} should be used, if -1
     *            the method {@link ElementLocator#findElement()} is used.
     * @param pageName name of the page where the element is.
     * @param fieldDescription description of the field, including the context.     *            
     */
    public LocatorWrappingUniqueElementLocator(WebDriver webDriver,
            ElementLocator elementLocator, int index, String pageName,
            String fieldDescription) {
        this.webDriver = webDriver;
        this.elementLocator = elementLocator;
        this.index = index;
        this.pageName = pageName;
        if (index >= 0) {
            this.fieldDescription = fieldDescription + '[' + index + ']';
        } else {
            this.fieldDescription = fieldDescription;
        }
    }

    /**
     * Creates a new element locator where the method
     * {@link ElementLocator#findElement()} is used.
     *
     * @param webDriver the underlying webdriver
     * @param elementLocator a elementLocator
     * @param pageName name of the page where the element is.
     * @param fieldDescription description of the field, including the context.
     */
    public LocatorWrappingUniqueElementLocator(WebDriver webDriver,
            ElementLocator elementLocator, String pageName,
            String fieldDescription) {
        this(webDriver, elementLocator, -1, pageName, fieldDescription);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebElement findElement() {
        if (index == -1) {
            return elementLocator.findElement();
        }
        return elementLocator.findElements().get(index);
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
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
