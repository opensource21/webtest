package de.ppi.selenium.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

/**
 * Helper to handle css-characteristics.
 *
 */
public class CSSHelper {

    /**
     * Get a list of name of classes, which stands in the attribut class.
     *
     * @param element the webelement which has the css-classes.
     *
     * @return a list of css-classes or an empty-list.
     */
    public static final List<String> getClasses(WebElement element) {
        final String classAttribute = element.getAttribute("class");
        if (StringUtils.isEmpty(classAttribute)) {
            return new ArrayList<>();
        }
        final String[] primitiveList = classAttribute.split(" ");
        return Arrays.asList(primitiveList);
    }

}
