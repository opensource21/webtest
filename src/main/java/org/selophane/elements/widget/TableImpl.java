package org.selophane.elements.widget;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.selophane.elements.base.ElementImpl;
import org.selophane.elements.base.UniqueElementLocator;

/**
 * Table wrapper.
 */
public class TableImpl extends ElementImpl implements Table {
    /**
     * Creates a Table for a given WebElement.
     *
     * @param elementLocator the locator of the webelement.
     */
    public TableImpl(final UniqueElementLocator elementLocator) {
        super(elementLocator);
    }

    @Override
    public int getRowCount() {
        return getRows().size();
    }

    @Override
    public int getColumnCount() {

        return findElement(By.cssSelector("tr")).findElements(
                By.cssSelector("*")).size();
        // Would ideally do:
        // return findElements(By.cssSelector("tr:first-of-type > *"));
        // however HTMLUnitDriver does not support CSS3
    }

    @Override
    public WebElement getCellAtIndex(int rowIdx, int colIdx) {
        // Get the row at the specified index
        WebElement row = getRows().get(rowIdx);

        List<WebElement> cells;

        // CSOFF: InnerAssignment For Performance-Reason this seems to be right.
        if ((cells = row.findElements(By.tagName("td"))).size() > 0) {
            // Cells are most likely to be td tags
            return cells.get(colIdx);
        } else if ((cells = row.findElements(By.tagName("th"))).size() > 0) {
            // Failing that try th tags
            return cells.get(colIdx);
        } else {
            final String error =
                    String.format("Could not find cell at row: %s column: %s",
                            Integer.valueOf(rowIdx), Integer.valueOf(colIdx));
            throw new RuntimeException(error);
        }
        // CSON: InnerAssignment
    }

    /**
     * Gets all rows in the table in order header > body > footer
     *
     * @return list of row WebElements
     */
    private List<WebElement> getRows() {
        List<WebElement> rows = new ArrayList<WebElement>();

        // Header rows
        rows.addAll(findElements(By.cssSelector("thead tr")));

        // Body rows
        rows.addAll(findElements(By.cssSelector("tbody tr")));

        // Footer rows
        rows.addAll(findElements(By.cssSelector("tfoot tr")));

        return rows;
    }

}
