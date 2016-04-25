/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.exceptions.ApplicationException;
import com.citi.rulefabricator.poi.XSSFSheetXMLHandler.SheetContentsHandler;
import com.myjeeva.poi.ExcelSheetCallback;

/**
 * The Class ExcelReader.
 */
public class ExcelReader {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    /** The Constant READ_ALL. */
    private static final int READ_ALL = -1;

    /** The xlsx package. */
    private OPCPackage xlsxPackage;
    
    /** The sheet contents handler. */
    private SheetContentsHandler sheetContentsHandler;
    
    /** The sheet callback. */
    private ExcelSheetCallback sheetCallback;

    /**
     * Constructor: Microsoft Excel File (XSLX) Reader.
     *
     * @param pkg            a {@link OPCPackage} object - The package to process XLSX
     * @param sheetContentsHandler            a {@link SheetContentsHandler} object - WorkSheet contents
     *            handler
     * @param sheetCallback            a {@link ExcelSheetCallback} object - WorkSheet callback for
     *            sheet processing begin and end (can be null)
     */
    public ExcelReader(OPCPackage pkg, SheetContentsHandler sheetContentsHandler, ExcelSheetCallback sheetCallback) {
        this.xlsxPackage = pkg;
        this.sheetContentsHandler = sheetContentsHandler;
        this.sheetCallback = sheetCallback;
    }

    /**
     * Constructor: Microsoft Excel File (XSLX) Reader.
     *
     * @param filePath            a {@link String} object - The path of XLSX file
     * @param sheetContentsHandler            a {@link SheetContentsHandler} object - WorkSheet contents
     *            handler
     * @param sheetCallback            a {@link ExcelSheetCallback} object - WorkSheet callback for
     *            sheet processing begin and end (can be null)
     * @throws Exception the exception
     */
    public ExcelReader(String filePath, SheetContentsHandler sheetContentsHandler, ExcelSheetCallback sheetCallback) throws Exception {
        this(getOPCPackage(getFile(filePath)), sheetContentsHandler, sheetCallback);
    }

    /**
     * Constructor: Microsoft Excel File (XSLX) Reader.
     *
     * @param file            a {@link File} object - The File object of XLSX file
     * @param sheetContentsHandler            a {@link SheetContentsHandler} object - WorkSheet contents
     *            handler
     * @param sheetCallback            a {@link ExcelSheetCallback} object - WorkSheet callback for
     *            sheet processing begin and end (can be null)
     * @throws Exception the exception
     */
    public ExcelReader(File file, SheetContentsHandler sheetContentsHandler, ExcelSheetCallback sheetCallback) throws Exception {
        this(getOPCPackage(file), sheetContentsHandler, sheetCallback);
    }

    /**
     * Instantiates a new excel reader.
     *
     * @param file the file
     * @param sheetContentsHandler the sheet contents handler
     * @throws Exception the exception
     */
    public ExcelReader(File file, SheetContentsHandler sheetContentsHandler) throws Exception {
        this(getOPCPackage(file), sheetContentsHandler, null);
    }

    /**
     * Processing all the WorkSheet from XLSX Workbook.
     * 
     * <br>
     * <br>
     * <strong>Example 1:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader("src/main/resources/Sample-Person-Data.xlsx", workSheetHandler, sheetCallback);
     * <br>excelReader.process();</code> <br>
     * <br>
     * <strong>Example 2:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader(file, workSheetHandler, sheetCallback);
     * <br>excelReader.process();</code> <br>
     * <br>
     * <strong>Example 3:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader(pkg, workSheetHandler, sheetCallback);
     * <br>excelReader.process();</code>
     *
     * @throws ApplicationException the application exception
     */
    public void process() throws ApplicationException {
        read(READ_ALL);
    }

    /**
     * Processing of particular WorkSheet (zero based) from XLSX Workbook.
     * 
     * <br>
     * <br>
     * <strong>Example 1:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader("src/main/resources/Sample-Person-Data.xlsx", workSheetHandler, sheetCallback);
     * <br>excelReader.process(2);</code> <br>
     * <br>
     * <strong>Example 2:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader(file, workSheetHandler, sheetCallback);
     * <br>excelReader.process(2);</code> <br>
     * <br>
     * <strong>Example 3:</strong><br>
     * <code>ExcelReader excelReader = new ExcelReader(pkg, workSheetHandler, sheetCallback);
     * <br>excelReader.process(2);</code>
     *
     * @param sheetNumber            a int object
     * @throws ApplicationException the application exception
     */
    public void process(int sheetNumber) throws ApplicationException {
        read(sheetNumber);
    }

    /**
     * Read.
     *
     * @param sheetNumber the sheet number
     * @throws ApplicationException the application exception
     */
    private void read(int sheetNumber) throws ApplicationException {
        ReadOnlySharedStringsTable strings;
        try {
            strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
            XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
            StylesTable styles = xssfReader.getStylesTable();
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            for (int sheet = 0; sheets.hasNext(); sheet++) {
                processSingleSheetAndInvokeCallback(sheetNumber, strings, styles, sheets, sheet);
            }
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
            throw new ApplicationException(ApplicationConstants.ERRORCODE_5005);
        } catch (SAXException se) {
            LOGGER.error(se.getMessage(), se);
            throw new ApplicationException(ApplicationConstants.ERRORCODE_5005);
        } catch (OpenXML4JException oxe) {
            LOGGER.error(oxe.getMessage(), oxe);
            throw new ApplicationException(ApplicationConstants.ERRORCODE_5005);
        } catch (ParserConfigurationException pce) {
            LOGGER.error(pce.getMessage(), pce);
            throw new ApplicationException(ApplicationConstants.ERRORCODE_5005);
        } catch (IndexOutOfBoundsException iobe) {
            LOGGER.error(iobe.getMessage(), iobe);
            throw new ApplicationException(ApplicationConstants.ERRORCODE_5005);
        }
    }

    /**
     * Process single sheet and invoke callback.
     *
     * @param sheetNumber the sheet number
     * @param strings the strings
     * @param styles the styles
     * @param sheets the sheets
     * @param sheet the sheet
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     */
    private void processSingleSheetAndInvokeCallback(int sheetNumber, ReadOnlySharedStringsTable strings, StylesTable styles, Iterator<InputStream> sheets, int sheet)
            throws IOException, ParserConfigurationException, SAXException {
        if (null != sheetCallback) {
            this.sheetCallback.startSheet(sheet);
        }

        InputStream stream = sheets.next();
        if ((READ_ALL == sheetNumber) || (sheet == sheetNumber)) {
            readSheet(styles, strings, stream);
        }
        IOUtils.closeQuietly(stream);

        if (null != sheetCallback) {
            this.sheetCallback.endSheet();
        }
    }

    /**
     * Parses the content of one sheet using the specified styles and
     * shared-strings tables.
     *
     * @param styles            a {@link StylesTable} object
     * @param sharedStringsTable            a {@link ReadOnlySharedStringsTable} object
     * @param sheetInputStream            a {@link InputStream} object
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     */
    private void readSheet(StylesTable styles, ReadOnlySharedStringsTable sharedStringsTable, InputStream sheetInputStream) throws IOException,
            ParserConfigurationException, SAXException {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        XMLReader sheetParser = saxFactory.newSAXParser().getXMLReader();

        ContentHandler handler = new XSSFSheetXMLHandler(styles, sharedStringsTable, sheetContentsHandler, true);

        sheetParser.setContentHandler(handler);
        sheetParser.parse(new InputSource(sheetInputStream));
    }

    /**
     * Gets the file.
     *
     * @param filePath the file path
     * @return the file
     * @throws Exception the exception
     */
    private static File getFile(String filePath) throws Exception {
        if (null == filePath || filePath.isEmpty()) {
            throw new Exception("File path cannot be null");
        }

        return new File(filePath);
    }

    /**
     * Gets the OPC package.
     *
     * @param file the file
     * @return the OPC package
     * @throws Exception the exception
     */
    private static OPCPackage getOPCPackage(File file) throws Exception {
        if (null == file || !file.canRead()) {
            throw new Exception("File object is null or cannot have read permission");
        }

        return OPCPackage.open(new FileInputStream(file));
    }
}
