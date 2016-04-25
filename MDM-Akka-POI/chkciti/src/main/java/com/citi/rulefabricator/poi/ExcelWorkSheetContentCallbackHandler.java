/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.poi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.CellReference;
import org.springframework.util.CollectionUtils;

import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.poi.XSSFSheetXMLHandler.SheetContentsHandler;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * 
 * 
 * This class is the callback handler for every row read in the excel file, used
 * the code from below link
 * 
 * <a href=
 * "http://myjeeva.com/read-excel-through-java-using-xssf-and-sax-apache-poi.html"
 * >http://myjeeva.com/read-excel-through-java-using-xssf-and-sax-apache-
 * poi.html</a.
 * 
 *
 */
public class ExcelWorkSheetContentCallbackHandler implements SheetContentsHandler {

    /** The Constant LOG. */
    private static final Log LOG = LogFactory.getLog(ExcelWorkSheetContentCallbackHandler.class);

    /** The Constant HEADER_ROW1. */
    private static final int HEADER_ROW1 = 0;
    
    /** The Constant HEADER_ROW2. */
    private static final int HEADER_ROW2 = 1;

    // once an entire row of data has been read, pass map to this callback for
    // processing
    /** The row callback. */
    private ExcelRowContentCallBack rowCallback;

    // LinkedHashMaps are used so iteration order is predictable over insertion
    // order
    /** The current row map. */
    private Map<String, List<String>> currentRowMap;
    
    /** The column headers. */
    private Map<String, List<String>> columnHeaders;
    
    /** The current row. */
    private int currentRow;
    
    /** The current column. */
    private int currentColumn;
    
    /** The prev column. */
    private int prevColumn;
    
    /** The values. */
    private List<String> values;
    
    /** The is dimensions. */
    private List<String> isDimensions;
    
    /** The tree ids. */
    private List<String> treeIds;
    
    /** The column1 value. */
    private String column1Value;
    
    /** The column2 value. */
    private String column2Value;
    
    /** The column3 value. */
    private String column3Value;
    
    /** The column1 header. */
    private String column1Header = null;
    
    /** The column2 header. */
    private String column2Header = null;
    
    /** The column3 header. */
    private String column3Header = null;
    
    /** The anchor column header. */
    private String anchorColumnHeader = null;
    
    /** The anchor column value. */
    private String anchorColumnValue = null;

	/** The attribute params. */
    private static List<String> attributeParams = new ArrayList<String>(Arrays.asList(ApplicationConstants.ATTRVALUECOLUMN,
            ApplicationConstants.DIMENSION_COLUMN, ApplicationConstants.TREEID_COLUMN));


    /**
     * Instantiates a new excel work sheet content callback handler.
     *
     * @param rowCallbackHandler the row callback handler
     */
    public ExcelWorkSheetContentCallbackHandler(ExcelRowContentCallBack rowCallbackHandler) {
        this.rowCallback = rowCallbackHandler;
    }

    /**
     * Start row.
     *
     * @param rowNum the row num
     * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#startRow(int)
     */
    @Override
    public void startRow(int rowNum) {

        this.currentRow = rowNum;
        this.currentColumn = 0;
        this.prevColumn = 0;
        this.values = new ArrayList<String>();
        this.isDimensions = new ArrayList<String>();
        this.treeIds = new ArrayList<String>();
        this.anchorColumnValue = null;

        if (this.currentRow == HEADER_ROW1) {
            this.columnHeaders = new LinkedHashMap<String, List<String>>();
        } else if (this.currentRow > HEADER_ROW2) {
            this.currentRowMap = new LinkedHashMap<String, List<String>>();

            // Add column header as key into current row map so that each entry
            // will exist.
            // This ensures each column header will be in the "currentRowMap"
            // when passed to the user callback.
            // Remember, the 'column headers map key' is the actual cell column
            // reference, it's value is the file column header value.
            // In the 'cell' method below, this empty string will be overwritten
            // with the file row value (if has one, else remains empty).
            for (String columnHeader : this.columnHeaders.keySet()) {
                this.currentRowMap.put(columnHeader, new ArrayList<String>());
            }

        }

    }

    // Note, POI will not invoke this method if the cell
    // is blank or if it detects there's no more data in the row.
    /**
     * @see com.citi.rulefabricator.poi.XSSFSheetXMLHandler.SheetContentsHandler#cell(java.lang.String, java.lang.String)
     */
    @Override
    public void cell(String cellReference, String formattedValue) {

        if (!StringUtils.isEmpty(cellReference)) {
            this.prevColumn = this.currentColumn > this.prevColumn ? this.currentColumn : this.prevColumn;
            this.currentColumn = getCellIndex(cellReference);
        }
        String columnRef = getColumnReference(cellReference);
        if (this.currentRow == HEADER_ROW1 && !CommonUtils.isBlank(columnRef)) {
            this.columnHeaders.put(formattedValue, new ArrayList<String>());
            populateHeader(formattedValue);
        } else if (this.currentRow == HEADER_ROW2 && !CommonUtils.isBlank(columnRef)) {
            if (!attributeParams.contains(formattedValue)) {
                processDataForNonAttribColumns(formattedValue);
            } else {
                populateRowValues(formattedValue);
            }
        } else {
            populateRowValues(formattedValue);
        }
        this.currentColumn++;

    }

    /**
     * Process data for non attrib columns.
     *
     * @param formattedValue the formatted value
     */
    private void processDataForNonAttribColumns(String formattedValue) {
        if (currentColumn == 0) {
            this.columnHeaders.remove(column1Header);
            String col1Header = column1Header + ApplicationConstants.SPACE + formattedValue;
            column1Header = col1Header.trim();
            this.columnHeaders.put(column1Header, new ArrayList<String>());
        } else if (currentColumn == 1) {
            this.columnHeaders.remove(column2Header);
            String col2Header = column2Header + ApplicationConstants.SPACE + formattedValue;
            column2Header = col2Header.trim();
            this.columnHeaders.put(column2Header, new ArrayList<String>());
        } else if (currentColumn == 2) {
            this.columnHeaders.remove(column3Header);
            String col3Header = column3Header + ApplicationConstants.SPACE + formattedValue;
            column3Header = col3Header.trim();
            this.columnHeaders.put(column3Header, new ArrayList<String>());
        }
    }

    /**
     * End row.
     *
     * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#endRow()
     */
    @Override
    public void endRow() {
        if (this.columnHeaders != null && !columnHeaders.isEmpty()) {
            List<String> keyList = new ArrayList<String>();
            keyList.addAll(columnHeaders.keySet());
            try {
                if (this.currentRow == HEADER_ROW2) {
                    extractFirstRowInfo(keyList);
                    this.rowCallback.processRow(this.currentRow, this.columnHeaders);
                }
                if (this.currentRow > HEADER_ROW2) {
                    // If the last column(s) are empty then we explicitly add
                    // blank data for them in the values array.
                    // Total columns = 3 columns per non anchor input attributes
                    // + 1 anchor + 1 output + sequence+start date + priority
                    int totalColumns = (keyList.size() - 5) * 3 + 5;
                    if (areTrailingColumnsEmpty(totalColumns)) {
                        for (int i = prevColumn + 1; i <= totalColumns; i++) {
                            this.setColumnValue(i, "");
                            prevColumn++;
                        }
                    }
                    extractSecondRowInfo(keyList);
                    this.rowCallback.processRow(this.currentRow, currentRowMap);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error invoking callback", e);
            }
            this.currentRow++;
        }
    }

    /**
     * Are trailing columns empty.
     *
     * @param totalColumns the total columns
     * @return true, if successful
     */
    private boolean areTrailingColumnsEmpty(int totalColumns) {
        // prevColumn and currentColumn are zero index based.
        // prevColumn has to be one less than the total columns starting from
        // zero.
        int zeroIndexBasedTotalColumns = totalColumns - 1;
        return (zeroIndexBasedTotalColumns - prevColumn) > 1;
    }

    /**
     * Extract second row info.
     *
     * @param keyList the key list
     */
    private void extractSecondRowInfo(List<String> keyList) {
        String columnHeader = null;
        LOG.debug("rowNum=" + currentRow + ", map=" + currentRowMap);
        this.currentRowMap.get(keyList.get(keyList.size() - 3)).add(column1Value);
        this.currentRowMap.get(keyList.get(keyList.size() - 2)).add(column2Value);
        this.currentRowMap.get(keyList.get(keyList.size() - 1)).add(column3Value);

        // this is for the anchor column
        this.currentRowMap.get(keyList.get(0)).add(anchorColumnValue);

        // will be looping only for the variable attributes and not for the
        // static params (like priority, etc)
        // 5 factor comes by including the 3 static params(sequence,
        // date, priority), 1 output value and 1 anchor tag
        int loop = keyList.size() - 5;

        for (int i = 0; i < loop; i++) {
            columnHeader = keyList.get(i + 1);
            List<String> currentValues = this.currentRowMap.get(columnHeader);
            if (values.size() > i) {
                String value = values.get(i);
                currentValues.add(value);
            } else {
                currentValues.add("");
            }
            if (isDimensions.size() > i) {
                String dimension = isDimensions.get(i);
                currentValues.add(dimension);
            } else {
                currentValues.add("");
            }
            if (treeIds.size() > i) {
                String treeId = treeIds.get(i);
                currentValues.add(treeId);
            } else {
                currentValues.add("");
            }
        }
        // this is for the last column
        if (!CollectionUtils.isEmpty(values)) {
            this.currentRowMap.get(keyList.get(keyList.size() - 4)).add(values.get(values.size() - 1));
        }
    }

    /**
     * Extract first row info.
     *
     * @param keyList the key list
     */
    private void extractFirstRowInfo(List<String> keyList) {
        String columnHeader;
        for (int i = 0; i < isDimensions.size(); i++) {
            // since the sequence of static column headers in the
            // linkedHashMap(priority, etc) are modified, the map
            // will have the variable attribute headers at the
            // start(i.e. attrNames), 1st column will be the anchor
            // column
            columnHeader = keyList.get(i + 1);
            this.columnHeaders.get(columnHeader).add(values.get(i));
            this.columnHeaders.get(columnHeader).add(isDimensions.get(i));
            this.columnHeaders.get(columnHeader).add(treeIds.get(i));

        }
        // Adding the anchorColumn, assuming it will be the 1st
        // column always, since the columnHeader map sequence gets
        // modified
        this.columnHeaders.get(keyList.get(0)).add(anchorColumnValue);
        // this is the output attribute header
        String outputAttrHeader = keyList.get(keyList.size() - 4);
        this.columnHeaders.get(outputAttrHeader).add(values.get(values.size() - 1));
    }

    /**
     * Header footer.
     *
     * @param text the text
     * @param isHeader the is header
     * @param tagName the tag name
     * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#headerFooter(java.lang.String,
     *      boolean, java.lang.String)
     */
    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        // Not Used
    }

    /**
     * Returns the alphabetic column reference from this cell reference.
     * Example: Given 'A12' returns 'A' or given 'BA205' returns 'BA'
     *
     * @param cellReference the cell reference
     * @return the column reference
     */
    private String getColumnReference(String cellReference) {

        if (StringUtils.isBlank(cellReference)) {
            return "";
        }

        return cellReference.split("[0-9]*$")[0];
    }

    /**
     * Gets the cell index.
     *
     * @param cellReference the cell reference
     * @return the cell index
     */
    public int getCellIndex(String cellReference) {
        CellReference ref = new CellReference(cellReference);
        return ref.getCol();
    }

    /**
     * Populate header.
     *
     * @param formattedValue the formatted value
     */
    private void populateHeader(String formattedValue) {
        if (this.currentColumn == 0) {
            column1Header = formattedValue;
        } else if (this.currentColumn == 1) {
            column2Header = formattedValue;
        } else if (this.currentColumn == 2) {
            column3Header = formattedValue;
        } else if (this.currentColumn == 3) {
            anchorColumnHeader = formattedValue;
        } else if (this.currentColumn % 3 == 1) {
            this.values.add(formattedValue);
        } else if (this.currentColumn % 3 == 2) {
            this.isDimensions.add(formattedValue);
        } else if (this.currentColumn % 3 == 0) {
            this.treeIds.add(formattedValue);
        }
    }

    /**
     * Populate row values.
     *
     * @param formattedValue the formatted value
     */
    private void populateRowValues(String formattedValue) {
        // POI jumps over empty cells in the sheet. So, there could be a gap of
        // data for column numbers between prevColumn and currentColumn. This
        // gap is filled with empty/blank data.
        if (currentColumn - prevColumn > 1) {
            for (int i = prevColumn; i < currentColumn; i++) {
                setColumnValue(i, "");
                prevColumn++;
            }
        }

        setColumnValue(this.currentColumn, formattedValue);
    }

    /**
     * Sets the column value.
     *
     * @param currentColumn the current column
     * @param formattedValue the formatted value
     */
    private void setColumnValue(int currentColumn, String formattedValue) {
        if (currentColumn == 0) {
            column1Value = formattedValue;
        } else if (currentColumn == 1) {
            column2Value = formattedValue;
        } else if (currentColumn == 2) {
            column3Value = formattedValue;
        } else if (currentColumn == 3) {
            anchorColumnValue = formattedValue;
        } else if (currentColumn % 3 == 1) {
            this.values.add(formattedValue);
        } else if (currentColumn % 3 == 2) {
            this.isDimensions.add(formattedValue);
        } else if (currentColumn % 3 == 0) {
            this.treeIds.add(formattedValue);
        }
    }
}