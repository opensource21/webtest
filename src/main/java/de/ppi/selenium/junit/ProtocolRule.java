/**
 *
 */
package de.ppi.selenium.junit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.util.Protocol;

/**
 * @author niels
 *
 */
public class ProtocolRule extends TestWatcher {
	private static final Logger LOG = LoggerFactory.getLogger(ProtocolRule.class);

	private final File baseDir;

	private static final String protocolStart = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());

	/**
	 * Creates a new instance.
	 * @param baseDir - the base-directory where the protocol is written.
	 */
	public ProtocolRule(String baseDir) {
		this(new File(baseDir));
	}

	/**
	 * Creates a new instance.
	 * @param baseDir - the base-directory where the protocol is written.
	 */
	public ProtocolRule(File baseDir) {
		super();
		boolean baseDirOk = true;
		if (!baseDir.exists()) {
			if (!baseDir.mkdirs()) {
				baseDirOk = false;
				LOG.error("Couldn't create basedDir " + baseDir.getAbsolutePath());
			}
		}
		if (!baseDir.isDirectory()) {
			baseDirOk = false;
			LOG.error("basedDir " + baseDir.getAbsolutePath() + " isn't a directory");
		}
		if (baseDirOk) {
			this.baseDir = new File(baseDir, protocolStart);
		} else {
			this.baseDir = new File(protocolStart);
		}
		this.baseDir.mkdirs();
	}

	@Override
	protected void succeeded(Description description) {
		// TODO Auto-generated method stub
		super.succeeded(description);
	}

	@Override
	protected void failed(Throwable e, Description description) {
		Protocol.log("Failed", "The test " + description.getDisplayName() + " failed!",
				SessionManager.getSession());
	}

	@Override
	protected void skipped(AssumptionViolatedException e,
			Description description) {
		// TODO Auto-generated method stub
		super.skipped(e, description);
	}


	@Override
	protected void starting(Description description) {
		final File testProtocolDir = getTestProtocolDir(description);
		testProtocolDir.mkdirs();
		Protocol.setCurrentProtocol(new Protocol(testProtocolDir));
	}

	@Override
	protected void finished(Description description) {
		final File protocolDir = getTestProtocolDir(description);
		if (protocolDir.exists() && protocolDir.listFiles().length == 0) {
			protocolDir.delete();
		}
	}

	private File getTestProtocolDir(Description description) {
		File protDir = new File(baseDir, description.getClassName().replaceAll("\\W+", "_"));
		protDir = new File(protDir, description.getMethodName().replaceAll("\\W+", "_"));
		return protDir;
	}

}
