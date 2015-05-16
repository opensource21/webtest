/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package de.ppi.selenium.assertj;

import org.assertj.core.api.AbstractAssert;
import org.selophane.elements.base.Element;

import de.ppi.selenium.util.CSSHelper;

public class ElementAssert extends AbstractAssert<ElementAssert, Element> {

    public ElementAssert(Element actual) {
        super(actual, ElementAssert.class);
    }

    private void failIsEnabled() {
        super.failWithMessage("Object not enabled");
    }

    private void failIsNotEnabled() {
        super.failWithMessage("Object is enabled");
    }

    /**
     * check if the element is enabled
     *
     * @return
     */
    public ElementAssert isEnabled() {
        if (!actual.isEnabled()) {
            failIsEnabled();
        }
        return this;
    }

    /**
     * check if the element is not enabled
     *
     * @return
     */
    public ElementAssert isNotEnabled() {
        if (actual.isEnabled()) {
            failIsNotEnabled();
        }
        return this;
    }

    /**
     * check if the element is displayed
     *
     * @return
     */
    public ElementAssert isDisplayed() {
        if (!actual.isDisplayed()) {
            failIsNotDisplayed();
        }

        return this;
    }

    /**
     * check if the element is not displayed
     *
     * @return
     */
    public ElementAssert isNotDisplayed() {
        if (actual.isDisplayed()) {
            failIsDisplayed();
        }
        return this;
    }

    private void failIsDisplayed() {
        super.failWithMessage("Object not displayed");
    }

    private void failIsNotDisplayed() {
        super.failWithMessage("Object is displayed");
    }

    /**
     * check if the element is selected
     *
     * @return
     */
    public ElementAssert isSelected() {
        if (!actual.isSelected()) {
            failIsSelected();
        }
        return this;
    }

    /**
     * check if the element is not selected
     *
     * @return
     */
    public ElementAssert isNotSelected() {
        if (actual.isSelected()) {
            failIsNotSelected();
        }
        return this;
    }

    /**
     * check if the element contains the text
     *
     * @return
     */
    public ElementAssert hasText(String textToFind) {
        if (!actual.getText().contains(textToFind)) {
            super.failWithMessage("The element does not contain the text: "
                    + textToFind + " . Actual text found : " + actual.getText());

        }

        return this;
    }

    /**
     * check if the element matches the given regex
     *
     * @return
     */
    public ElementAssert hasTextMatching(String regexToBeMatched) {
        if (!actual.getText().matches(regexToBeMatched)) {
            super.failWithMessage("The element does not match the regex: "
                    + regexToBeMatched + " . Actual text found : "
                    + actual.getText());

        }

        return this;
    }

    /**
     * check if the element does not contain the text
     *
     * @return
     */
    public ElementAssert hasNotText(String textToFind) {
        if (actual.getText().contains(textToFind)) {
            super.failWithMessage("The element contain the text: " + textToFind);
        }

        return this;
    }

    private void failIsSelected() {
        super.failWithMessage("Object not selected");
    }

    private void failIsNotSelected() {
        super.failWithMessage("Object is selected");
    }

    /**
     * check if the element has the given id
     *
     * @param id to check
     * @return
     */
    public ElementAssert hasId(String id) {
        if (!actual.getAttribute("id").equals(id)) {
            super.failWithMessage("The element does not have the id: " + id
                    + " . Actual id found : " + actual.getAttribute("id"));
        }
        return this;
    }

    /**
     * check if the element has the not the class
     *
     * @param classToFind
     * @return this
     */
    public ElementAssert hasNotClass(String classToFind) {
        if (CSSHelper.getClasses(actual).contains(classToFind)) {
            super.failWithMessage("The element has the class: " + classToFind
                    + " . Actual class found : " + actual.getAttribute("class"));
        }
        return this;
    }

    /**
     * check if the element has the class
     *
     * @param classToFind
     * @return this
     */
    public ElementAssert hasClass(String classToFind) {
        if (!CSSHelper.getClasses(actual).contains(classToFind)) {
            super.failWithMessage("The element does not have the class: "
                    + classToFind + " . Actual class found : "
                    + actual.getAttribute("class"));
        }
        return this;
    }

}
