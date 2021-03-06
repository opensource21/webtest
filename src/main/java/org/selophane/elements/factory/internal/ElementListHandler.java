package org.selophane.elements.factory.internal;

import static org.selophane.elements.factory.internal.ImplementedByProcessor.getWrapperClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.selophane.elements.base.Element;
import org.selophane.elements.base.UniqueElementLocator;

/**
 * Wraps a list of WebElements in multiple wrapped elements.
 */
public class ElementListHandler implements InvocationHandler {

    private final WebDriver webDriver;
    private final ElementLocator locator;
    private final Class<?> wrappingType;
    /**
     * Name of the page where the element is.
     */
    private final String pageName;

    /**
     * Description of the field.
     */
    private final String fieldDescription;

    private List<Object> wrappedList;
    private List<WebElement> webElementList;

    /**
     * Given an interface and a locator, apply a wrapper over a list of
     * elements.
     *
     * @param interfaceType interface type we're trying to wrap around the
     *            element.
     * @param webDriver the underlying {@link WebDriver}.
     * @param locator locator on the page for the elements.
     * @param <T> type of the interface.
     * @param pageName name of the page where the element is.
     * @param fieldDescription description of the field, including the context.
     */
    public <T> ElementListHandler(Class<T> interfaceType, WebDriver webDriver,
            ElementLocator locator, String pageName, String fieldDescription) {
        this.webDriver = webDriver;
        this.locator = locator;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new RuntimeException("interface not assignable to Element.");
        }
        this.wrappingType = getWrapperClass(interfaceType);
        this.pageName = pageName;
        this.fieldDescription = fieldDescription;
    }

    /**
     * Executed on invoke of the requested proxy. Used to gather a list of
     * wrapped WebElements.
     *
     * @param o object to invoke on
     * @param method method to invoke
     * @param objects parameters for method
     * @return return value from method
     * @throws Throwable when frightened.
     */
    @SuppressWarnings("boxing")
    @Override
    public Object invoke(Object o, Method method, Object[] objects)
            throws Throwable {
        final List<WebElement> newWebElemenList = locator.findElements();

        final String methodName = method.getName();
        if ("size".equals(methodName)) {
            return newWebElemenList.size();
        } else if ("isEmpty".equals(methodName)) {
            return Boolean.valueOf(newWebElemenList.isEmpty());
        }

        if (wrappedList == null || newWebElemenList != webElementList) {
            webElementList = newWebElemenList;
            wrappedList = new ArrayList<Object>();
            Constructor<?> cons =
                    wrappingType.getConstructor(UniqueElementLocator.class);
            final int nrOfElements = newWebElemenList.size();
            for (int index = 0; index < nrOfElements; index++) {
                final UniqueElementLocator uniqueElementLocator =
                        new LocatorWrappingUniqueElementLocator(webDriver,
                                locator, index, pageName, fieldDescription);
                Object thing = cons.newInstance(uniqueElementLocator);
                wrappedList.add(wrappingType.cast(thing));
            }
        }

        try {
            return method.invoke(wrappedList, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }

}
