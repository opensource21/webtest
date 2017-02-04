package org.selophane.elements.base;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import de.ppi.selenium.logevent.api.EventActions;
import de.ppi.selenium.logevent.api.EventLogger;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;
import de.ppi.selenium.logevent.api.Priority;

/**
 * An implementation of the Element interface. Delegates its work to an
 * underlying WebElement instance for custom functionality.
 */
// TODO LOG alle Events loggen
public class ElementImpl implements Element {

    private final UniqueElementLocator uniqueElementLocator;

    /** Instance of the {@link EventLoggerFactory}. */
    private static final EventLoggerFactory BEFORE_EVENT_LOGGER_FACTORY =
            EventLoggerFactory.getInstance(EventSource.WEBELEMENT_BEFORE);

    /** Instance of the {@link EventLoggerFactory}. */
    private static final EventLoggerFactory AFTER_EVENT_LOGGER_FACTORY =
            EventLoggerFactory.getInstance(EventSource.WEBELEMENT_AFTER);

    /**
     * {@link EventLogger} which is specific for the elememt and log before the
     * action.
     */
    private final EventLogger eventLoggerBefore;

    /**
     * {@link EventLogger} which is specific for the elememt and log after the
     * action.
     */
    private final EventLogger eventLoggerAfter;

    /**
     * Creates a Element for a given {@link UniqueElementLocator}.
     *
     * @param elementLocator the locator of the webelement.
     */
    public ElementImpl(final UniqueElementLocator elementLocator) {
        this.uniqueElementLocator = elementLocator;
        eventLoggerBefore =
                BEFORE_EVENT_LOGGER_FACTORY.onDebug(
                        elementLocator.getPageName(),
                        elementLocator.getFieldDescription());
        eventLoggerAfter =
                AFTER_EVENT_LOGGER_FACTORY.onDebug(
                        elementLocator.getPageName(),
                        elementLocator.getFieldDescription());
    }

    /**
     * @return the element
     */
    private WebElement getElement() {
        return uniqueElementLocator.findElement();
    }

    @Override
    public void click() {
        final String method = "element.click";
        eventLoggerBefore.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_CLICK, method,
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription());
        getElement().click();
        eventLoggerAfter.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_CLICK, method,
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription());

    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        final String method = "element.sendKeys";
        eventLoggerBefore.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_SEND_KEYS, method,
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription(),
                StringUtils.join(keysToSend));
        getElement().sendKeys(keysToSend);
        eventLoggerAfter.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_SEND_KEYS, method,
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription(),
                StringUtils.join(keysToSend));
    }

    @Override
    public Point getLocation() {
        final String method = "element.getLocation";
        final Point result = getElement().getLocation();
        eventLoggerAfter.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_SEND_KEYS, method,
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription(), result);
        return result;
    }

    @Override
    public void submit() {
        eventLoggerBefore.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_SUBMIT, "element.submit",
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription());
        getElement().submit();
        eventLoggerAfter.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_SUBMIT, "element.submit",
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription());
    }

    @Override
    public String getAttribute(String name) {
        final String result = getElement().getAttribute(name);
        eventLoggerAfter.withScreenshot(Priority.DEBUG,
                uniqueElementLocator.getWebDriver()).log(
                EventActions.ELEMENT_GET_ATTRIBUTE, "element.getAttribute",
                uniqueElementLocator.getPageName(),
                uniqueElementLocator.getFieldDescription(), name, result);
        return result;
    }

    @Override
    public String getCssValue(String propertyName) {
        return getElement().getCssValue(propertyName);
    }

    @Override
    public Dimension getSize() {
        return getElement().getSize();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getElement().findElements(by);
    }

    @Override
    public String getText() {
        return getElement().getText();
    }

    @Override
    public String getTagName() {
        return getElement().getTagName();
    }

    @Override
    public boolean isSelected() {
        return getElement().isSelected();
    }

    @Override
    public WebElement findElement(By by) {
        return getElement().findElement(by);
    }

    @Override
    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    @Override
    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    @Override
    public void clear() {
        getElement().clear();
    }

    @Override
    public WebElement getWrappedElement() {
        return getElement();
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) getElement()).getCoordinates();
    }

    @Override
    public boolean elementWired() {
        return (getElement() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
            throws WebDriverException {
        return getElement().getScreenshotAs(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getRect() {
        return getElement().getRect();
    }

}
