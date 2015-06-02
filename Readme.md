# WebTest

WebTest provides some base-classes which makes Selenium test more comfortable.
It's inspired by [Jtaf-EXTWebdriver](http://finraos.github.io/JTAF-ExtWebDriver)
and [FluentLenium](https://github.com/FluentLenium/FluentLenium).
It used selophane in a special version.

## Advantages you get with WebTest
- Protocol
- Screenshots
- AssertJ-Extensions, which makes a screenshot on assertion-failure.
- JUnit-Rules
    - ProtocolRule: This rule configures the Protocol-class and creates
      for each test a directory (removed it if it is empty). Makes a screenshot
      if the test fails.
    - WebDriverRule: Creates at the beginning a WebBrowser in the SessionManager.
      If selenium loose the connection to the browser or more than 10 test
      use the webbrowser a new instance is created.
    - Starts at the beginning the webserver.
- SessionManager which handles different session and create webdriver instances.

## Configuration
You can influence the behavior with the following system-properties, which
must be set via `-D<propertyName>=value`.

- `webtest.logBeforeGet` - if `true` a screenshot is created before a new page is requested via get.
- `webtest.logAfterGet` - if `true` a screenshot is created after a new page is requested via get.
- `webtest.protocoldir` - defines the default-dir for webtest-logs.

- `webtest.baseurl` - defines the base-url.
- `phantomjs.binary.path` - defines the path to phantomjs for example
   `C:\\RegFreeProgs\\phantomjs\\phantomjs.exe`

### Browser
The browser is configured by a file `client.properties`. The properties are explained at
[Jtaf-EXTWebdriver](http://finraos.github.io/JTAF-ExtWebDriver/clientproperties.html)
which is the base of my code.
Additional you can define:

- `webdriver.phantomjs.driver` - the path to phantom.js
- `browser.accept_languages` - to set the accepted languages, doesn't work for IE.
   For HTMLUnit it sets the browser language.

## Usage
First add a maven-dependcy

     <dependency>
        <groupId>de.ppi.oss</groupId>
        <artifactId>webtest</artifactId>
        <version>0.1</version>
       <scope>test</scope>
    </dependency>
and add the following repository

    <repositories>
        <repository>
            <id>opensource21</id>
            <url> http://opensource21.github.com/releases</url>
        </repository>
    </repositories>

The Rules are more effectiv if you only instantiate them once. So the best way is
to declare a constant-class, like:

    public interface WebTestConstants {

        /**
         * Standard_Rule for WebTests.
         */
        RuleChain WEBTEST_WITHOUT_AUTHENTICATION = RuleChain
                .outerRule(
                        new WebServerRule(new DelegatingWebServer(
                                new JettyWebServer("/fuwesta"))))
                .around(new WebDriverRule()).around(new ProtocolRule("weblog"));
        /**
         * Standard_Rule for WebTests.
         */
        RuleChain WEBTEST = RuleChain.outerRule(WEBTEST_WITHOUT_AUTHENTICATION)
                .around(new AuthRule());
    }

Then you can use it in your test-class with

    @Rule
    public RuleChain webTest = WebTestConstants.WEBTEST;

You can see a simple example at [FuWeSta-Sample](https://github.com/opensource21/fuwesta).


## TODOs
- The code must be tested and specially the code in `de.ppi.selenium.browser`
  should be refactored.
