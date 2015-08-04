package org.selophane.elements.widget;

import org.selophane.elements.base.ElementImpl;
import org.selophane.elements.base.UniqueElementLocator;

/**
 * Wrapper class like Select that wraps basic checkbox functionality.
 */
public class CheckBoxImpl extends ElementImpl implements CheckBox {

    /**
     * Wraps a WebElement with checkbox functionality.
     *
     * @param elementLocator the locator of the webelement.
     */
    public CheckBoxImpl(final UniqueElementLocator elementLocator) {
        super(elementLocator);
    }

    @Override
    public void toggle() {
        getWrappedElement().click();
    }

    @Override
    public void check() {
        if (!isChecked()) {
            toggle();
        }
    }

    @Override
    public void uncheck() {
        if (isChecked()) {
            toggle();
        }
    }

    @Override
    public boolean isChecked() {
        return getWrappedElement().isSelected();
    }
}
