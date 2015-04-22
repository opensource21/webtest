# WebTest

WebTest provides some base-classes which makes Selenium test more comfortable.

## Configuration
You can influence the behavior with the following system-properties, which
must be set via `-D<propertyName>=value`.

- `webtest.logBeforeGet` - if `true` a screenshot is created before a new page is requested via get.
- `webtest.logAfterGet` - if `true` a screenshot is created after a new page is requested via get.
- `webtest.protocoldir` - defines the default-dir for webtest-logs.

- `webtest.baseurl` - defines the base-url.
- `phantomjs.binary.path` - defines the path to phantomjs for example
   `C:\\RegFreeProgs\\phantomjs\\phantomjs.exe`
