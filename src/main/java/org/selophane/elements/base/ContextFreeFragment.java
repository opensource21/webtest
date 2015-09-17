package org.selophane.elements.base;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.selophane.elements.factory.api.ElementDecorator;

/**
 * Baseclass of a fragment which handles the initialization if there are no
 * context is necessary. This means all sub-elements are identified by id.
 *
 */
public class ContextFreeFragment extends ElementImpl {

    /**
     *
     * Initiates an fragment, which doesn't need a context.
     *
     * @param elementLocator the locator of the webelement.
     */
    public ContextFreeFragment(final UniqueElementLocator elementLocator) {
        super(elementLocator);
        PageFactory.initElements(
                new ElementDecorator(elementLocator.getWebDriver(),
                        new DefaultElementLocatorFactory(elementLocator
                                .getWebDriver()), elementLocator.getPageName(),
                        elementLocator.getFieldDescription()), this);
    }
}
