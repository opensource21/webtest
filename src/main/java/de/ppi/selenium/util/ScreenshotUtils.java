/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.ppi.selenium.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Utilities to take and compare screenshots of elements
 *
 * Based on JTafExtWebdriver.
 */
public final class ScreenshotUtils {

    /** The LOG-Instance. */
    private static final Logger LOG = LoggerFactory
            .getLogger(ScreenshotUtils.class);

    /**
     * Maximal number of retries to get a screenshot.
     */
    private static final int MAXIMAL_NR_OF_RETRIES = 10;

    /**
     * Default threshold so that 2 pictures are similar.
     */
    private static final double DEFAULT_THRESHOLD = .85;

    /**
     *
     * Initiates an object of type ScreenshotUtils.
     */
    private ScreenshotUtils() {

    }

    /**
     * Save a screenshot.
     *
     * @param screenshotFileName name of the file, without ending.
     * @param driver the webdriver.
     */
    public static void saveScreenshot(String screenshotFileName,
            WebDriver driver) {
        try {
            WebDriver wrappedDriver = driver;
            while (wrappedDriver instanceof WrapsDriver) {
                wrappedDriver =
                        ((WrapsDriver) wrappedDriver).getWrappedDriver();
            }
            if (wrappedDriver instanceof TakesScreenshot) {
                File screenshot =
                        ((TakesScreenshot) wrappedDriver)
                                .getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, new File(screenshotFileName
                        + ".png"));
            } else if (wrappedDriver instanceof HtmlUnitDriver) {
                FileUtils.write(new File(screenshotFileName + ".html"),
                        wrappedDriver.getPageSource());
            } else {
                LOG.warn("The current driver doesn't make screenshots");
            }
        } catch (IOException e) {
            LOG.error("IO-Error during creating of the screenshot ", e);
        }
    }

    /***
     * You can use this method to take your control pictures. Note that the file
     * should be a png.
     *
     * @param element the webelement
     * @param toSaveAs the file where the screenshot should be save.
     * @param wd the webdriver.
     * @throws IOException if something goes wrong writing the result.
     */
    public static void takeScreenshotOfElement(WebElement element,
            File toSaveAs, WebDriver wd) throws IOException {

        for (int i = 0; i < MAXIMAL_NR_OF_RETRIES; i++) { // Loop up to 10x to
                                                          // ensure a clean
            // screenshot was taken
            LOG.info("Taking screen shot of locator " + element
                    + " ... attempt #" + (i + 1));

            // Scroll to element
            // TODO element.scrollTo();

            // Take picture of the page
            File screenshot;
            boolean isRemote = false;
            if (!(wd instanceof RemoteWebDriver)) {
                screenshot =
                        ((TakesScreenshot) wd).getScreenshotAs(OutputType.FILE);
            } else {
                Augmenter augmenter = new Augmenter();
                screenshot =
                        ((TakesScreenshot) augmenter.augment(wd))
                                .getScreenshotAs(OutputType.FILE);
                isRemote = true;
            }
            BufferedImage fullImage = ImageIO.read(screenshot);

            // Parse out the picture of the element
            Point point = element.getLocation();
            int eleWidth = element.getSize().getWidth();
            int eleHeight = element.getSize().getHeight();
            int x;
            int y;
            if (isRemote) {
                x = ((Locatable) element).getCoordinates().inViewPort().getX();
                y = ((Locatable) element).getCoordinates().inViewPort().getY();
            } else {
                x = point.getX();
                y = point.getY();
            }
            LOG.debug("Screenshot coordinates x: " + x + ", y: " + y);
            BufferedImage eleScreenshot =
                    fullImage.getSubimage(x, y, eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "png", screenshot);

            // Ensure clean snapshot (sometimes WebDriver takes bad pictures and
            // they turn out all black)
            if (!isBlack(ImageIO.read(screenshot))) {
                FileUtils.copyFile(screenshot, toSaveAs);
                break;
            }
        }
    }

    /***
     * Prereq: The page on which you are taking the screenshot is fully loaded
     *
     * Take a screenshot of the element identified by element and save the file
     * as toSaveAs (Note that this file should be saved as a png). Test that the
     * control picture, controlPicture, is both the same size as, and, has a
     * similarity value greater than or equal to the threshold.
     *
     * @param wd the webdriver.
     * @param element - the element to be tested
     * @param controlPicture - the file of the picture that will serve as the
     *            control
     * @param toSaveAs - for example, save the file at
     *            "testData/textFieldWidget/screenshot.png"
     * @param threshold - you are asserting that the similarity between the two
     *            pictures is a double greater than or equal to this double
     *            (between 0.0 and 1.0)
     * @return true is the pictures are similar.
     * @throws IOException if something goes wrong writing or reading.
     */
    public static boolean isSimilarToScreenshot(WebDriver wd,
            WebElement element, File controlPicture, File toSaveAs,
            double threshold) throws IOException {

        takeScreenshotOfElement(element, toSaveAs, wd);

        LOG.info("Screenshot was successful. Comparing against control...");

        BufferedImage var = ImageIO.read(toSaveAs);
        BufferedImage cont = ImageIO.read(controlPicture);

        return isSimilar(var, cont, threshold);
    }

    /***
     * Prereq: The page on which you are taking the screenshot is fully loaded
     *
     * Take a screenshot of the element identified by element and save the file
     * as toSaveAs (Note that this file should be saved as a png). Test that the
     * control picture, controlPicture, is both the same size as, and, has a
     * similarity value greater than or equal to the default threshold of .85.
     *
     * @param wd the webdriver.
     * @param element - the element to be tested
     * @param controlPicture - the file of the picture that will serve as the
     *            control
     * @param toSaveAs - for example, save the file at
     *            "testData/textFieldWidget/screenshot.png"
     * @return true if the screenshots are similar.
     * @throws IOException if something goes wrong reading or writing.
     */
    public static boolean isSimilarToScreenshot(WebDriver wd,
            WebElement element, File controlPicture, File toSaveAs)
            throws IOException {
        return isSimilarToScreenshot(wd, element, controlPicture, toSaveAs,
                DEFAULT_THRESHOLD);
    }

    /**
     * Checks if a screenshot is complete black.
     *
     * @param var the image.
     * @return true if it is black.
     */
    private static boolean isBlack(BufferedImage var) {
        final int scale = 3;
        double[] varArr = new double[var.getWidth() * var.getHeight() * scale];
        // unroll pixels
        for (int i = 0; i < var.getHeight(); i++) {
            for (int j = 0; j < var.getWidth(); j++) {
                varArr[i * var.getWidth() + j + 0] =
                        new Color(var.getRGB(j, i)).getRed();
                varArr[i * var.getWidth() + j + 1] =
                        new Color(var.getRGB(j, i)).getGreen();
                varArr[i * var.getWidth() + j + 2] =
                        new Color(var.getRGB(j, i)).getBlue();
            }
        }

        // test if all are black
        for (int i = 0; i != varArr.length; i++) {
            if (varArr[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if tw pictures are similar.
     *
     * @param var picture1
     * @param cont picture2
     * @param threshold - you are asserting that the similarity between the two
     *            pictures is a double greater than or equal to this double
     *            (between 0.0 and 1.0)
     *
     * @return true if the pictures are similar.
     */
    private static boolean isSimilar(BufferedImage var, BufferedImage cont,
            double threshold) {
        return similarity(var, cont) >= threshold;
    }

    /**
     * Calculate how similar the 2 immages are.
     * 
     * @param var picture1
     * @param cont picture2
     * @return a value between 0 and 1.
     */
    private static double similarity(BufferedImage var, BufferedImage cont) {

        final int scale = 3;
        double[] varArr = new double[var.getWidth() * var.getHeight() * scale];
        double[] contArr = new double[cont.getWidth() * cont.getHeight() * scale];

        if (varArr.length != contArr.length) {
            throw new IllegalStateException("The pictures are different sizes!");
        }

        // unroll pixels
        for (int i = 0; i < var.getHeight(); i++) {
            for (int j = 0; j < var.getWidth(); j++) {
                varArr[i * var.getWidth() + j + 0] =
                        new Color(var.getRGB(j, i)).getRed();
                contArr[i * cont.getWidth() + j + 0] =
                        new Color(cont.getRGB(j, i)).getRed();
                varArr[i * var.getWidth() + j + 1] =
                        new Color(var.getRGB(j, i)).getGreen();
                contArr[i * cont.getWidth() + j + 1] =
                        new Color(cont.getRGB(j, i)).getGreen();
                varArr[i * var.getWidth() + j + 2] =
                        new Color(var.getRGB(j, i)).getBlue();
                contArr[i * cont.getWidth() + j + 2] =
                        new Color(cont.getRGB(j, i)).getBlue();
            }
        }

        double mins = 0;
        double maxs = 0;
        for (int i = 0; i != varArr.length; i++) {
            if (varArr[i] > contArr[i]) {
                mins += contArr[i];
                maxs += varArr[i];
            } else {
                mins += varArr[i];
                maxs += contArr[i];
            }
        }
        return mins / maxs;
    }

}
