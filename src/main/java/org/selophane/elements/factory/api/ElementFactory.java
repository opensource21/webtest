package org.selophane.elements.factory.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

/**
 * Element factory for wrapped elements. Similar to
 * {@link org.openqa.selenium.support.PageFactory}
 */
public final class ElementFactory {

    private ElementFactory() {
        // UTILITY-Konstruktor
    }

    /**
     * Instantiate an instance of the given class, and set a lazy proxy for each
     * of the WebElement and List&lt;WebElement&gt; fields that have been
     * declared, assuming that the field name is also the HTML element's "id" or
     * "name". This means that for the class:
     *
     * <code> public class Page { private WebElement submit; } </code>
     *
     * there will be an element that can be located using the xpath expression
     * "//*[@id='submit']" or "//*[@name='submit']"
     *
     * By default, the element or the list is looked up each and every time a
     * method is called upon it. To change this behaviour, simply annotate the
     * field with the {@link CacheLookup}. To change how the element is located,
     * use the {@link FindBy} annotation.
     *
     * This method will attempt to instantiate the class given to it, preferably
     * using a constructor which takes a WebDriver instance as its only argument
     * or falling back on a no-arg constructor. An exception will be thrown if
     * the class cannot be instantiated.
     *
     * @param driver The driver that will be used to look up the elements
     * @param pageClassToProxy A class which will be initialised.
     * @param pageName name of the page where the element is.
     * @param contextDescription description of the context.
     *
     * @return An instantiated instance of the class with WebElement and
     *         List&lt;WebElement&gt; fields proxied
     * @see FindBy
     * @see CacheLookup
     */
    public static <T> T initElements(WebDriver driver,
            Class<T> pageClassToProxy, String pageName,
            String contextDescription) {
        T page = instantiatePage(driver, pageClassToProxy);
        return initElements(driver, page, pageName, contextDescription);
    }

    /**
     * As {@link ElementFactory#initElements(WebDriver, Class, String, String)}
     * but will only replace the fields of an already instantiated Page Object.
     *
     * @param webDriver The driver that will be used to look up the elements
     * @param page The object with WebElement and List&lt;WebElement&gt; fields
     *            that should be proxied.
     * @param pageName name of the page where the element is.
     * @param contextDescription description of the context.
     */
    public static <T> T initElements(WebDriver webDriver, T page,
            String pageName, String contextDescription) {
        PageFactory.initElements(new ElementDecorator(webDriver,
                new DefaultElementLocatorFactory(webDriver), pageName,
                contextDescription), page);
        return page;
    }

    /**
     * Similar to the other "initElements" methods, but takes an
     * {@link ElementLocatorFactory} which is used for providing the mechanism
     * for fniding elements. If the ElementLocatorFactory returns null then the
     * field won't be decorated.
     *
     * @param factory The factory to use
     * @param page The object to decorate the fields of
     * @param pageName name of the page where the element is.
     * @param contextDescription description of the context.
     */
    public static void initElements(WebDriver webDriver,
            ElementLocatorFactory factory, Object page, String pageName,
            String contextDescription) {
        final ElementLocatorFactory factoryRef = factory;
        PageFactory.initElements(new ElementDecorator(webDriver, factoryRef,
                pageName, contextDescription), page);
    }

    /**
     * see
     * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.ElementLocatorFactory, Object)}
     */
    public static void initElements(FieldDecorator decorator, Object page) {
        PageFactory.initElements(decorator, page);
    }

    /**
     * Copy of
     * {@link org.openqa.selenium.support.PageFactory#instantiatePage(org.openqa.selenium.WebDriver, Class)}
     */
    private static <T> T instantiatePage(WebDriver driver,
            Class<T> pageClassToProxy) {
        try {
            try {
                Constructor<T> constructor =
                        pageClassToProxy.getConstructor(WebDriver.class);
                return constructor.newInstance(driver);
            } catch (NoSuchMethodException e) {
                return pageClassToProxy.newInstance();
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
