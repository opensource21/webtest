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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.Strings;
import org.openqa.selenium.Cookie;

import de.ppi.selenium.browser.WebBrowser;

/**
 * Special asserts for the {@link WebBrowser}.
 *
 */
public class WebbrowserAssert extends
        AbstractAssert<WebbrowserAssert, WebBrowser> {

    /**
     * Infos about assertions.
     */
    private final WritableAssertionInfo info = new WritableAssertionInfo();;

    /**
     *
     * Initiates an object of type WebbrowserAssert.
     *
     * @param actual the current object.
     */
    public WebbrowserAssert(WebBrowser actual) {
        super(actual, WebbrowserAssert.class);
    }

    /**
     * Check if the browser is at the relativeURL.
     *
     * @param relativeUrl the relative url.
     * @return this
     */
    public WebbrowserAssert hasRelativeUrl(String relativeUrl) {
        Objects.instance().assertEqual(info, actual.getCurrentRelativeUrl(),
                relativeUrl);
        return this;
    }

    /**
     * check if the relative url matches the given regex.
     *
     * @param regexToBeMatched the text which should be found as regeexp.
     * @return self
     */
    public WebbrowserAssert hasRalativeUrlMatching(String regexToBeMatched) {
        Strings.instance().assertMatches(info, actual.getCurrentRelativeUrl(),
                regexToBeMatched);
        return this;
    }

    /**
     * Check if a cookie with the given name exists.
     *
     * @param searchedCookies a list of cookies which should be found.
     * @return this
     */
    public WebbrowserAssert hasCookies(String... searchedCookies) {
        final Set<Cookie> cookies = actual.manage().getCookies();
        final List<String> cookieNames = new ArrayList<>();
        for (Cookie cookie : cookies) {
            cookieNames.add(cookie.getName());
        }
        // assertThat(cookieNames).contains(searchedCookies);
        Iterables.instance().assertContains(info, cookieNames, searchedCookies);
        return this;
    }

}
