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
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Objects;

import de.ppi.selenium.browser.WebBrowser;

public class WebbrowserAssert extends AbstractAssert<WebbrowserAssert, WebBrowser> {

	private final WritableAssertionInfo info =  new WritableAssertionInfo();;

    public WebbrowserAssert(WebBrowser actual) {
        super(actual, WebbrowserAssert.class);
    }

    /**
     * check if it is at the current page. Call the page.isAt() methods
     *
     * @return
     */
    public WebbrowserAssert hasRelativeUrl(String relativeUrl) {
    	Objects.instance().assertEqual(info, actual.getCurrentRelativeUrl(), relativeUrl);
        return this;
    }

}
