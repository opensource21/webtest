package de.ppi.selenium.junit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.util.Protocol;

/**
 * Junit-Rule which helps to create a protocol.
 *
 * @author niels
 *
 */
public class ProtocolRule extends TestWatcher {

    /**
     * The Log.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(ProtocolRule.class);

    /**
     * Basedir where all protocol-dirs are relative.
     */
    private final File baseDir;

    /**
     * Start-time as String.
     */
    private static final String PROTOCOL_START = new SimpleDateFormat(
            "yyyy-MM-dd_HH_mm_ss").format(new Date());

    /**
     * Creates a new instance.
     */
    public ProtocolRule() {
        this(System.getProperty("webtest.protocoldir", "."));
    }

    /**
     * Creates a new instance.
     *
     * @param baseDir - the base-directory where the protocol is written.
     */
    public ProtocolRule(String baseDir) {
        this(new File(baseDir));
    }

    /**
     * Creates a new instance.
     *
     * @param baseDir - the base-directory where the protocol is written.
     */
    public ProtocolRule(File baseDir) {
        super();
        boolean baseDirOk = true;
        if (!baseDir.exists()) {
            if (!baseDir.mkdirs()) {
                baseDirOk = false;
                LOG.error("Couldn't create basedDir "
                        + baseDir.getAbsolutePath());
            }
        }
        if (!baseDir.isDirectory()) {
            baseDirOk = false;
            LOG.error("basedDir " + baseDir.getAbsolutePath()
                    + " isn't a directory");
        }
        if (baseDirOk) {
            this.baseDir = new File(baseDir, PROTOCOL_START);
        } else {
            this.baseDir = new File(PROTOCOL_START);
        }
        this.baseDir.mkdirs();
    }

    @Override
    protected void failed(Throwable e, Description description) {
        Protocol.log("Failed", "The test " + description.getDisplayName()
                + " failed!", SessionManager.getSession());
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
        deleteDirIfEmpty(protocolDir);
        deleteDirIfEmpty(protocolDir.getParentFile());

    }

    /**
     * Deletes the directory it is empty.
     *
     * @param protocolDir the directory.
     */
    private void deleteDirIfEmpty(File protocolDir) {
        if (protocolDir.exists() && protocolDir.listFiles().length == 0) {
            protocolDir.delete();
        }
    }

    /**
     * Create the name of the protocol-directory.
     *
     * @param description the test-description.
     * @return a protocol directory.
     */
    private File getTestProtocolDir(Description description) {
        File protDir =
                new File(baseDir, description.getClassName().replaceAll("\\W+",
                        "_"));
        protDir =
                new File(protDir, description.getMethodName().replaceAll(
                        "\\W+", "_"));
        return protDir;
    }

}
