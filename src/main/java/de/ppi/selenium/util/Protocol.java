/**
 *
 */
package de.ppi.selenium.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with helps to create a protocol of the test.
 * @author niels
 *
 */
public class Protocol extends InheritableThreadLocal<Protocol> {

	private static final Logger LOG = LoggerFactory.getLogger(Protocol.class);

	private static final String protocolStart = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());

	private static final Protocol INSTANCE = new Protocol();

	private final File protocolDir;

	 /** The constant for the regular expresion which is replaced in the export filename. */
    private static final String REGEXP_FILENAME_TO_REPLACE = "\\W+";


	public Protocol() {
		this.protocolDir = createDefaultProtocolDir();
	}


	private File createDefaultProtocolDir() {
		File baseDir = new File(System.getProperty("webtest.protocoldir", ""));
		if (!baseDir.exists()) {
			if (!baseDir.mkdirs()) {
				LOG.error("Couldn't create basedDir " + baseDir.getAbsolutePath());
				baseDir = new File("");
			}
		}
		if (!baseDir.isDirectory()) {
			LOG.error("basedDir " + baseDir.getAbsolutePath() + " isn't a directory");
			baseDir = new File("");
		}
		final File tempProtocolDir = new File(baseDir, protocolStart);
		tempProtocolDir.mkdir();
		return tempProtocolDir;
	}


	/**
	 * Creates a new instance.
	 * @param baseDir - the base-directory where the protocol is written.
	 */
	public Protocol(File baseDir) {
		if (baseDir.exists() && baseDir.isDirectory() && baseDir.canWrite()) {
			this.protocolDir = baseDir;
		} else {
			LOG.error(baseDir.getAbsolutePath() + "is not an existing writeable directory.");
			this.protocolDir = createDefaultProtocolDir();
		}
	}



	@Override
	protected Protocol initialValue() {
		return new Protocol();
	}


   /**
    * Save a screenshot.
    *
    * @param description become part of the filename.
    * @param webDriver the webdriver.
    */
   public void saveScreenshot(String description, WebDriver webDriver) {
	   final StringBuilder screenshotFileName = new StringBuilder(protocolDir.getAbsolutePath());
	   screenshotFileName.append(File.separatorChar);
	   screenshotFileName.append(new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS'-'").format(new Date()));
	   screenshotFileName.append(description.replaceAll(REGEXP_FILENAME_TO_REPLACE, "_")).append('-');

	   ScreenshotUtils.saveScreenshot(screenshotFileName.toString(), webDriver);
   }


   /**
    * Protocolized the description.
    *
    * @param title a short description title
    * @param description a longer description
    * @param webDriver the webdriver.
    */
   public static void log(String title, String description, WebDriver webDriver) {
	   INSTANCE.get().saveScreenshot(title, webDriver);
   }

   /**
    * Set the protocol-instance for the current thread.
    * @param newProtocol the new instance.
    */
   public static void setCurrentProtocol(Protocol newProtocol) {
	   INSTANCE.set(newProtocol);
   }

}
