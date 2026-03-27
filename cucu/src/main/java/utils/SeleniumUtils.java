package utils;

import core.DriverFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeleniumUtils {
    private static final int DEFAULT_TIMEOUT_SECONDS = 20;
    private static final int SHORT_TIMEOUT_SECONDS = 5;

    private SeleniumUtils() {
    }

    private static WebDriverWait waitFor(int timeoutSeconds) {
        return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutSeconds));
    }

    private static Actions actions() {
        return new Actions(DriverFactory.getDriver());
    }

    public static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverFactory.getDriver();
    }

    private static void fail(String message, Throwable error) {
        Log.error(message, error);
        Assert.fail(message, error);
    }

    private static void fail(String message) {
        Log.error(message);
        Assert.fail(message);
    }

    public static WebElement find(By locator) {
        try {
            return waitForElementPresent(locator);
        } catch (Exception e) {
            fail("Element not found: " + locator, e);
            return null;
        }
    }

    public static List<WebElement> findAll(By locator) {
        try {
            waitForElementPresent(locator);
            return DriverFactory.getDriver().findElements(locator);
        } catch (Exception e) {
            fail("Elements not found: " + locator, e);
            return List.of();
        }
    }

    public static WebElement getWebElement(By locator) {
        return find(locator);
    }

    public static List<WebElement> getWebElements(By locator) {
        return findAll(locator);
    }

    public static void click(By locator) {
        try {
            waitForElementClickable(locator).click();
            Log.info("Clicked: " + locator);
        } catch (Exception e) {
            fail("Click failed: " + locator, e);
        }
    }

    public static void sendKeys(By locator, String text) {
        try {
            waitForElementVisible(locator).sendKeys(text);
            Log.info("Sent keys: " + text);
        } catch (Exception e) {
            fail("SendKeys failed: " + locator, e);
        }
    }

    public static void setText(By locator, String value) {
        clearAndFillText(locator, value);
    }

    public static void setText(By locator, String value, Keys key) {
        clearText(locator);
        waitForElementVisible(locator).sendKeys(value, key);
        Log.info("Entered text with key for: " + locator);
    }

    public static void sendKeys(By locator, Keys key) {
        try {
            waitForElementVisible(locator).sendKeys(key);
            Log.info("Sent key for: " + locator);
        } catch (Exception e) {
            fail("SendKeys failed: " + locator, e);
        }
    }

    public static void clearText(By locator) {
        try {
            waitForElementVisible(locator).clear();
            Log.info("Cleared text: " + locator);
        } catch (Exception e) {
            fail("Clear text failed: " + locator, e);
        }
    }

    public static void clearAndFillText(By locator, String value) {
        clearText(locator);
        sendKeys(locator, value);
    }

    public static void clickElement(By locator) {
        click(locator);
    }

    public static void clickElement(By locator, int timeoutSeconds) {
        try {
            waitForElementClickable(locator, timeoutSeconds).click();
            Log.info("Clicked: " + locator);
        } catch (Exception e) {
            fail("Click failed: " + locator, e);
        }
    }

    public static void clickElementWithJs(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            scrollToElementAtCenter(locator);
            getJsExecutor().executeScript("arguments[0].click();", element);
            Log.info("Clicked with JS: " + locator);
        } catch (Exception e) {
            fail("JavaScript click failed: " + locator, e);
        }
    }

    public static void clickLinkText(String linkText) {
        click(By.linkText(linkText));
    }

    public static String getText(By locator) {
        try {
            String text = waitForElementVisible(locator).getText().trim();
            Log.info("Read text from: " + locator + " => " + text);
            return text;
        } catch (Exception e) {
            fail("Get text failed: " + locator, e);
            return null;
        }
    }

    public static String getTextElement(By locator) {
        return getText(locator);
    }

    public static String getAttribute(By locator, String attributeName) {
        try {
            return waitForElementPresent(locator).getAttribute(attributeName);
        } catch (Exception e) {
            fail("Get attribute failed: " + locator + ", attribute: " + attributeName, e);
            return null;
        }
    }

    public static String getAttributeElement(By locator, String attributeName) {
        return getAttribute(locator, attributeName);
    }

    public static String getCssValueElement(By locator, String cssName) {
        try {
            return waitForElementPresent(locator).getCssValue(cssName);
        } catch (Exception e) {
            fail("Get CSS value failed: " + locator + ", css: " + cssName, e);
            return null;
        }
    }

    public static Dimension getSizeElement(By locator) {
        try {
            return waitForElementPresent(locator).getSize();
        } catch (Exception e) {
            fail("Get size failed: " + locator, e);
            return null;
        }
    }

    public static Point getLocationElement(By locator) {
        try {
            return waitForElementPresent(locator).getLocation();
        } catch (Exception e) {
            fail("Get location failed: " + locator, e);
            return null;
        }
    }

    public static String getTagNameElement(By locator) {
        try {
            return waitForElementPresent(locator).getTagName();
        } catch (Exception e) {
            fail("Get tag name failed: " + locator, e);
            return null;
        }
    }

    public static List<String> getListElementsText(By locator) {
        List<String> texts = new ArrayList<>();
        for (WebElement element : findAll(locator)) {
            texts.add(element.getText().trim());
        }
        return texts;
    }

    public static void selectOptionByText(By locator, String text) {
        try {
            new Select(waitForElementVisible(locator)).selectByVisibleText(text);
            Log.info("Selected option by text from: " + locator + " => " + text);
        } catch (Exception e) {
            fail("Select by text failed: " + locator + ", value: " + text, e);
        }
    }

    public static void selectOptionByValue(By locator, String value) {
        try {
            new Select(waitForElementVisible(locator)).selectByValue(value);
            Log.info("Selected option by value from: " + locator + " => " + value);
        } catch (Exception e) {
            fail("Select by value failed: " + locator + ", value: " + value, e);
        }
    }

    public static void selectOptionByIndex(By locator, int index) {
        try {
            new Select(waitForElementVisible(locator)).selectByIndex(index);
            Log.info("Selected option by index from: " + locator + " => " + index);
        } catch (Exception e) {
            fail("Select by index failed: " + locator + ", index: " + index, e);
        }
    }

    public static boolean verifySelectedByText(By locator, String text) {
        try {
            String selectedText = new Select(waitForElementVisible(locator)).getFirstSelectedOption().getText().trim();
            Assert.assertEquals(selectedText, text, "Selected text mismatch");
            return true;
        } catch (Exception e) {
            fail("Selected text verification failed: " + locator + ", expected: " + text, e);
            return false;
        }
    }

    public static boolean verifySelectedByValue(By locator, String value) {
        String selectedValue = getAttribute(locator, "value");
        Assert.assertEquals(selectedValue, value, "Selected value mismatch");
        return true;
    }

    public static boolean verifySelectedByIndex(By locator, int index) {
        try {
            Select select = new Select(waitForElementVisible(locator));
            int selectedIndex = select.getOptions().indexOf(select.getFirstSelectedOption());
            Assert.assertEquals(selectedIndex, index, "Selected index mismatch");
            return true;
        } catch (Exception e) {
            fail("Selected index verification failed: " + locator + ", expected: " + index, e);
            return false;
        }
    }

    public static boolean checkElementExist(By locator) {
        return checkElementExist(locator, SHORT_TIMEOUT_SECONDS, 500);
    }

    public static boolean checkElementExist(By locator, int maxRetries, int waitTimeMillis) {
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            if (!DriverFactory.getDriver().findElements(locator).isEmpty()) {
                return true;
            }
            sleep(waitTimeMillis / 1000.0);
        }
        return false;
    }

    public static boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementDisplayed(By locator, int timeoutSeconds) {
        try {
            return waitForElementVisible(locator, timeoutSeconds).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementPresent(By locator, int timeoutSeconds) {
        try {
            waitForElementPresent(locator, timeoutSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementVisible(By locator, int timeoutSeconds) {
        try {
            waitForElementVisible(locator, timeoutSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean verifyElementPresent(By locator) {
        return verifyElementPresent(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean verifyElementPresent(By locator, int timeoutSeconds) {
        try {
            Assert.assertTrue(isElementPresent(locator, timeoutSeconds), "Element not present: " + locator);
            return true;
        } catch (AssertionError error) {
            fail("Element not present: " + locator, error);
            return false;
        }
    }

    public static boolean verifyElementNotPresent(By locator) {
        return verifyElementNotPresent(locator, SHORT_TIMEOUT_SECONDS);
    }

    public static boolean verifyElementNotPresent(By locator, int timeoutSeconds) {
        try {
            waitFor(timeoutSeconds).until(ExpectedConditions.numberOfElementsToBe(locator, 0));
            return true;
        } catch (Exception e) {
            fail("Element still present: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementVisible(By locator) {
        return verifyElementVisible(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean verifyElementVisible(By locator, int timeoutSeconds) {
        try {
            Assert.assertTrue(isElementVisible(locator, timeoutSeconds), "Element not visible: " + locator);
            return true;
        } catch (AssertionError error) {
            fail("Element not visible: " + locator, error);
            return false;
        }
    }

    public static boolean verifyElementNotVisible(By locator) {
        return verifyElementNotVisible(locator, SHORT_TIMEOUT_SECONDS);
    }

    public static boolean verifyElementNotVisible(By locator, int timeoutSeconds) {
        try {
            waitFor(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            fail("Element still visible: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementClickable(By locator) {
        return verifyElementClickable(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean verifyElementClickable(By locator, int timeoutSeconds) {
        try {
            waitForElementClickable(locator, timeoutSeconds);
            return true;
        } catch (Exception e) {
            fail("Element not clickable: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementChecked(By locator) {
        try {
            Assert.assertTrue(waitForElementPresent(locator).isSelected(), "Element is not selected: " + locator);
            return true;
        } catch (Exception e) {
            fail("Element checked verification failed: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementText(By locator, String text) {
        return verifyElementTextEquals(locator, text);
    }

    public static boolean verifyElementTextEquals(By locator, String text) {
        String actual = getText(locator);
        Assert.assertEquals(actual, text, "Element text mismatch for: " + locator);
        return true;
    }

    public static boolean verifyElementTextContains(By locator, String text) {
        String actual = getText(locator);
        Assert.assertTrue(actual.contains(text), "Element text does not contain expected value for: " + locator);
        return true;
    }

    public static boolean verifyEquals(Object actual, Object expected) {
        Assert.assertEquals(actual, expected);
        return true;
    }

    public static boolean verifyContains(String actual, String expected) {
        Assert.assertTrue(actual.contains(expected), "Expected text not found");
        return true;
    }

    public static boolean verifyTrue(boolean condition) {
        Assert.assertTrue(condition, "Condition expected to be true");
        return true;
    }

    public static void hoverOnElement(By locator) {
        try {
            actions().moveToElement(waitForElementVisible(locator)).perform();
            Log.info("Hovered on: " + locator);
        } catch (Exception e) {
            fail("Hover failed: " + locator, e);
        }
    }

    public static void mouseHover(By locator) {
        hoverOnElement(locator);
    }

    public static void rightClickElement(By locator) {
        try {
            actions().contextClick(waitForElementClickable(locator)).perform();
            Log.info("Right clicked: " + locator);
        } catch (Exception e) {
            fail("Right click failed: " + locator, e);
        }
    }

    public static void doubleClickElement(By locator) {
        try {
            actions().doubleClick(waitForElementClickable(locator)).perform();
            Log.info("Double clicked: " + locator);
        } catch (Exception e) {
            fail("Double click failed: " + locator, e);
        }
    }

    public static void dragAndDrop(By source, By target) {
        try {
            actions().dragAndDrop(waitForElementVisible(source), waitForElementVisible(target)).perform();
            Log.info("Dragged " + source + " to " + target);
        } catch (Exception e) {
            fail("Drag and drop failed. Source: " + source + ", target: " + target, e);
        }
    }

    public static void moveToElement(By locator) {
        hoverOnElement(locator);
    }

    public static void scrollToElementAtTop(By locator) {
        scrollIntoView(locator, "start");
    }

    public static void scrollToElementAtBottom(By locator) {
        scrollIntoView(locator, "end");
    }

    public static void scrollToElementAtCenter(By locator) {
        try {
            WebElement element = waitForElementPresent(locator);
            getJsExecutor().executeScript(
                    "arguments[0].scrollIntoView({behavior:'instant', block:'center', inline:'nearest'});",
                    element
            );
        } catch (Exception e) {
            fail("Scroll to center failed: " + locator, e);
        }
    }

    private static void scrollIntoView(By locator, String blockPosition) {
        try {
            WebElement element = waitForElementPresent(locator);
            getJsExecutor().executeScript(
                    "arguments[0].scrollIntoView({behavior:'instant', block:arguments[1], inline:'nearest'});",
                    element, blockPosition
            );
        } catch (Exception e) {
            fail("Scroll failed: " + locator, e);
        }
    }

    public static void scrollToPosition(int x, int y) {
        getJsExecutor().executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
    }

    public static void openWebsite(String url) {
        DriverFactory.getDriver().get(url);
        waitForPageLoaded();
        Log.info("Opened URL: " + url);
    }

    public static void navigateToUrl(String url) {
        openWebsite(url);
    }

    public static void reloadPage() {
        DriverFactory.getDriver().navigate().refresh();
        waitForPageLoaded();
        Log.info("Page refreshed");
    }

    public static String getCurrentUrl() {
        return DriverFactory.getDriver().getCurrentUrl();
    }

    public static String getPageTitle() {
        return DriverFactory.getDriver().getTitle();
    }

    public static boolean VerifyPageTitle(String pageTitle) {
        Assert.assertEquals(getPageTitle(), pageTitle, "Page title mismatch");
        return true;
    }

    public static boolean verifyPageContainsText(String text) {
        Assert.assertTrue(DriverFactory.getDriver().getPageSource().contains(text), "Page source does not contain text");
        return true;
    }

    public static void switchToFrameByIndex(int index) {
        DriverFactory.getDriver().switchTo().frame(index);
    }

    public static void switchToFrameByIdOrName(String idOrName) {
        DriverFactory.getDriver().switchTo().frame(idOrName);
    }

    public static void switchToFrameByElement(By locator) {
        DriverFactory.getDriver().switchTo().frame(waitForElementPresent(locator));
    }

    public static void switchToDefaultContent() {
        DriverFactory.getDriver().switchTo().defaultContent();
    }

    public static void switchToWindowOrTabByPosition(int position) {
        List<String> handles = new ArrayList<>(DriverFactory.getDriver().getWindowHandles());
        if (position < 0 || position >= handles.size()) {
            fail("Invalid window position: " + position);
            return;
        }
        DriverFactory.getDriver().switchTo().window(handles.get(position));
    }

    public static void switchToWindowOrTabByTitle(String title) {
        for (String handle : DriverFactory.getDriver().getWindowHandles()) {
            DriverFactory.getDriver().switchTo().window(handle);
            if (DriverFactory.getDriver().getTitle().equals(title)) {
                return;
            }
        }
        fail("No window found with title: " + title);
    }

    public static void switchToWindowOrTabByUrl(String url) {
        for (String handle : DriverFactory.getDriver().getWindowHandles()) {
            DriverFactory.getDriver().switchTo().window(handle);
            if (DriverFactory.getDriver().getCurrentUrl().contains(url)) {
                return;
            }
        }
        fail("No window found with URL containing: " + url);
    }

    public static void switchToMainWindow() {
        switchToWindowOrTabByPosition(0);
    }

    public static void switchToMainWindow(String originalWindow) {
        DriverFactory.getDriver().switchTo().window(originalWindow);
    }

    public static void switchToLastWindow() {
        Set<String> windowHandles = DriverFactory.getDriver().getWindowHandles();
        String lastHandle = new ArrayList<>(windowHandles).get(windowHandles.size() - 1);
        DriverFactory.getDriver().switchTo().window(lastHandle);
    }

    public static void closeCurrentWindow() {
        DriverFactory.getDriver().close();
    }

    public static boolean verifyTotalOfWindowsOrTab(int number) {
        Assert.assertEquals(DriverFactory.getDriver().getWindowHandles().size(), number, "Window count mismatch");
        return true;
    }

    public static void acceptAlert() {
        waitForAlertPresent();
        DriverFactory.getDriver().switchTo().alert().accept();
    }

    public static void dismissAlert() {
        waitForAlertPresent();
        DriverFactory.getDriver().switchTo().alert().dismiss();
    }

    public static String getTextAlert() {
        waitForAlertPresent();
        return DriverFactory.getDriver().switchTo().alert().getText();
    }

    public static void setTextAlert(String text) {
        waitForAlertPresent();
        DriverFactory.getDriver().switchTo().alert().sendKeys(text);
    }

    public static boolean verifyAlertPresent(int timeoutSeconds) {
        return waitForAlertPresent(timeoutSeconds);
    }

    public static boolean waitForAlertPresent() {
        return waitForAlertPresent(DEFAULT_TIMEOUT_SECONDS);
    }

    public static boolean waitForAlertPresent(int timeoutSeconds) {
        try {
            waitFor(timeoutSeconds).until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            fail("Alert not present within " + timeoutSeconds + " seconds", e);
            return false;
        }
    }

    public static Alert getAlert() {
        try {
            return DriverFactory.getDriver().switchTo().alert();
        } catch (NoAlertPresentException e) {
            fail("Alert not present", e);
            return null;
        }
    }

    public static WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        try {
            return waitFor(timeoutSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            fail("Timeout waiting for visible element: " + locator, e);
            return null;
        }
    }

    public static WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static WebElement waitForElementClickable(By locator, long timeoutSeconds) {
        try {
            return waitFor((int) timeoutSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            fail("Timeout waiting for clickable element: " + locator, e);
            return null;
        }
    }

    public static WebElement waitForElementPresent(By locator) {
        return waitForElementPresent(locator, DEFAULT_TIMEOUT_SECONDS);
    }

    public static WebElement waitForElementPresent(By locator, long timeoutSeconds) {
        try {
            return waitFor((int) timeoutSeconds)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            fail("Timeout waiting for present element: " + locator, e);
            return null;
        }
    }

    public static boolean waitForElementHasAttribute(By locator, String attributeName) {
        try {
            return waitFor(DEFAULT_TIMEOUT_SECONDS)
                    .until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(locator), attributeName));
        } catch (Exception e) {
            fail("Timeout waiting for attribute '" + attributeName + "' on: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementAttributeValue(By locator, String attributeName, String attributeValue) {
        try {
            boolean matched = waitFor(DEFAULT_TIMEOUT_SECONDS)
                    .until(ExpectedConditions.attributeToBe(locator, attributeName, attributeValue));
            Assert.assertTrue(matched, "Attribute value mismatch");
            return true;
        } catch (Exception e) {
            fail("Attribute verification failed for: " + locator, e);
            return false;
        }
    }

    public static boolean verifyElementHasAttribute(By locator, String attributeName, int timeoutSeconds) {
        try {
            boolean matched = waitFor(timeoutSeconds)
                    .until(ExpectedConditions.attributeToBeNotEmpty(waitForElementPresent(locator, timeoutSeconds), attributeName));
            Assert.assertTrue(matched, "Attribute not present");
            return true;
        } catch (Exception e) {
            fail("Attribute verification failed for: " + locator, e);
            return false;
        }
    }

    public static void waitForPageLoaded() {
        waitForPageLoaded(DEFAULT_TIMEOUT_SECONDS);
    }

    public static void waitForPageLoaded(int timeoutSeconds) {
        try {
            ExpectedCondition<Boolean> pageLoadCondition =
                    driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            waitFor(timeoutSeconds).until(pageLoadCondition);
        } catch (Exception e) {
            fail("Page did not finish loading within " + timeoutSeconds + " seconds", e);
        }
    }

    public static void sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Sleep interrupted", e);
        }
    }
}
