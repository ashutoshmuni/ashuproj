/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.poi;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class handles the processing of a sheet#.xml sheet part of a XSSF .xlsx
 * file, and generates row and cell events for it.
 */
public class XSSFSheetXMLHandler extends DefaultHandler {
    /**
     * These are the different kinds of cells we support. We keep track of the
     * current one between the start and end.
     */

    enum xssfDataType {
        
        /** The bool. */
        BOOL, 
        /** The error. */
        ERROR, 
        /** The formula. */
        FORMULA, 
        /** The inlinestr. */
        INLINESTR, 
        /** The sstindex. */
        SSTINDEX, 
        /** The number. */
        NUMBER, 
        /** The blank. */
        BLANK
    }

    /** Table with the styles used for formatting. */
    private StylesTable stylesTable;

    /** The shared strings table. */
    private ReadOnlySharedStringsTable sharedStringsTable;

    /** Where our text is going. */
    private final SheetContentsHandler output;

    // Set when V start element is seen
    /** The v is open. */
    private boolean vIsOpen;

    // Set when cell start element is seen.
    // used when cell close element is seen.
    /** The next data type. */
    private xssfDataType nextDataType;

    // Used to format numeric cell values.
    /** The format index. */
    private short formatIndex;
    
    /** The format string. */
    private String formatString;
    
    /** The formatter. */
    private final DataFormatter formatter;
    
    /** The cell ref. */
    private String cellRef;
    
    /** The formulas not results. */
    private boolean formulasNotResults;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XSSFSheetXMLHandler.class);

    // Gathers characters as they are seen.
    /** The value. */
    private StringBuilder value = new StringBuilder();

    /** The last cell ref invoked. */
    private String lastCellRefInvoked = null;

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles            Table of styles
     * @param strings            Table of shared strings
     * @param sheetContentsHandler the sheet contents handler
     * @param dataFormatter the data formatter
     * @param formulasNotResults the formulas not results
     */
    public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler,
            DataFormatter dataFormatter, boolean formulasNotResults) {
        this.stylesTable = styles;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.formulasNotResults = formulasNotResults;
        this.nextDataType = xssfDataType.NUMBER;
        this.formatter = dataFormatter;
    }

    /**
     * Accepts objects needed while parsing.
     *
     * @param styles            Table of styles
     * @param strings            Table of shared strings
     * @param sheetContentsHandler the sheet contents handler
     * @param formulasNotResults the formulas not results
     */
    public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetContentsHandler,
            boolean formulasNotResults) {
        this(styles, strings, sheetContentsHandler, new DataFormatter(), formulasNotResults);
    }

    /**
     * Start element.
     *
     * @param uri the uri
     * @param localName the local name
     * @param name the name
     * @param attributes the attributes
     * @throws SAXException the SAX exception
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

        if ("inlineStr".equals(name) || "v".equals(name)) {
            vIsOpen = true;
            // Clear contents cache
            value.setLength(0);
        } else if ("c".equals(name)) {
            setDefaults(attributes);
        } else if ("row".equals(name)) {
            int rowNum = Integer.parseInt(attributes.getValue("r")) - 1;
            output.startRow(rowNum);
        }

    }

    /**
     * Sets the defaults.
     *
     * @param attributes the new defaults
     */
    private void setDefaults(Attributes attributes) {
        // Get the cell reference
        cellRef = attributes.getValue("r");

        // Set up defaults.
        this.nextDataType = xssfDataType.NUMBER;
        this.formatIndex = -1;
        this.formatString = null;
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        if ("b".equals(cellType)) {
            this.nextDataType = xssfDataType.BOOL;
        } else if ("e".equals(cellType)) {
            this.nextDataType = xssfDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            this.nextDataType = xssfDataType.INLINESTR;
        } else if ("s".equals(cellType)) {
            this.nextDataType = xssfDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            this.nextDataType = xssfDataType.FORMULA;
        } else if (cellStyleStr != null) {
            // It's a number, but almost certainly one with a special style
            // or format
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            this.formatIndex = style.getDataFormat();
            this.formatString = style.getDataFormatString();
            if (this.formatString == null) {
                this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
            }
        }
    }

    /**
     * End element.
     *
     * @param uri the uri
     * @param localName the local name
     * @param name the name
     * @throws SAXException the SAX exception
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String name) throws SAXException {
        boolean emptyCell = true;

        String thisStr = null;

        if (lastCellRefInvoked == cellRef && !"row".equals(name)) {
            return;
        }

        // v => contents of a cell
        if ("v".equals(name)) {
            thisStr = getValue(nextDataType);
            output.cell(cellRef, thisStr);
            emptyCell = false;

        } else if ("row".equals(name)) {
            output.endRow();
            emptyCell = false;
        }

        if (emptyCell && "c".equals(name)) {
            output.cell(null, "");
        }
        lastCellRefInvoked = cellRef;
    }

    /**
     * Characters.
     *
     * @param ch the ch
     * @param start the start
     * @param length the length
     * @throws SAXException the SAX exception
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (vIsOpen) {
            value.append(ch, start, length);
        }
    }

    /**
     * Gets the value.
     *
     * @param nextDataType the next data type
     * @return the value
     */
    public String getValue(xssfDataType nextDataType) {
        String thisStr = null;
        switch (nextDataType) {

        case BOOL:
            char first = value.charAt(0);
            thisStr = first == '0' ? "FALSE" : "TRUE";
            break;

        case ERROR:
            thisStr = "\"ERROR:" + value.toString() + '"';
            break;

        case FORMULA:
            thisStr = '"' + value.toString() + '"';
            break;

        case INLINESTR:
            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
            thisStr = rtsi.toString();
            break;

        case SSTINDEX:
            String sstIndex = value.toString();
            thisStr = sstIndex(sstIndex);
            break;

        case NUMBER:
            String n = value.toString();
            thisStr = number(n);
            break;

        default:
            thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
            break;
        }
        return thisStr;
    }

    /**
     * Number.
     *
     * @param n the n
     * @return the string
     */
    private String number(String n) {
        String thisStr = null;
        if (this.formatString != null) {
            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
        } else {
            thisStr = n;
        }
        return thisStr;
    }

    /**
     * Sst index.
     *
     * @param sstIndex the sst index
     * @return the string
     */
    private String sstIndex(String sstIndex) {
        String thisStr = null;
        try {
            int idx = Integer.parseInt(sstIndex);
            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
            thisStr = rtss.toString();
        } catch (NumberFormatException ex) {
            LOGGER.error("Number format exception", ex);
        }

        return thisStr;
    }

    /**
     * You need to implement this to handle the results of the sheet parsing.
     */
    public interface SheetContentsHandler {
        
        /**
         *  A row with the (zero based) row number has started.
         *
         * @param rowNum the row num
         */
        public void startRow(int rowNum);

        /**
         *  A row with the (zero based) row number has ended.
         */
        public void endRow();

        /**
         *  A cell, with the given formatted value, was encountered.
         *
         * @param cellReference the cell reference
         * @param formattedValue the formatted value
         */
        public void cell(String cellReference, String formattedValue);

        /**
         *  A header or footer has been encountered.
         *
         * @param text the text
         * @param isHeader the is header
         * @param tagName the tag name
         */
        public void headerFooter(String text, boolean isHeader, String tagName);
    }
}
