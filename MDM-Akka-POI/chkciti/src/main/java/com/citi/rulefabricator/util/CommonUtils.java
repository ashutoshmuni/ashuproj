/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.citi.rulefabricator.enums.ApplicationConstants;

/**
 * The Class CommonUtils.
 */
public class CommonUtils {

    /** The Constant DEFAULT_DATE_TIME_FORMAT. */
    public static final String DEFAULT_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    
    /** The Constant DEFAULT_DATE_FORMAT. */
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    
    /** The Constant HYPHEN_DATE_FORMAT. */
    public static final String HYPHEN_DATE_FORMAT = "yyyy-MM-dd";
    
    /** The Constant FILE_DATETIME_FORMAT_YYYY_MM_DD. */
    public static final String FILE_DATETIME_FORMAT_YYYY_MM_DD = "yyyy-MM-dd HH-mm-ss";
    
    /** The Constant MONTH_DATE_FORMAT. */
    public static final String MONTH_DATE_FORMAT = "YYYYMM";
    
    /** The Constant NO_DELIM_DATE_FORMAT. */
    public static final String NO_DELIM_DATE_FORMAT = "yyyyMMdd";

    /** The Constant DEFAULT_TIME_ZONE. */
    public static final String DEFAULT_TIME_ZONE = "EST";

    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 4096;

    // setting the default time zone
    /** The Constant DEFAULT_TIME_ZONE_CANONICAL_NAME. */
    public static final String DEFAULT_TIME_ZONE_CANONICAL_NAME = "America/New_York";
    
    /** The Constant DEFAULTTIMEZONE. */
    public static final DateTimeZone DEFAULTTIMEZONE = DateTimeZone.forID(DEFAULT_TIME_ZONE);

    static {

        TimeZone defTimeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE);
        TimeZone.setDefault(defTimeZone);

    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * The utility class will have static methods so its instance should not be
     * created.
     */
    private CommonUtils() {
    }

    /**
     * Gets the date from string of format.
     *
     * @param inputDate the input date
     * @param format the format
     * @return the date from string of format
     */
    public static Date getDateFromStringOfFormat(String inputDate, String format) {

        Date date = null;
        DateFormat defDateTimeFormatter = new SimpleDateFormat(format, Locale.US);
        TimeZone defTimeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE);
        defDateTimeFormatter.setTimeZone(defTimeZone);
        defDateTimeFormatter.setLenient(Boolean.FALSE);
        TimeZone.setDefault(defTimeZone);

        try {
            date = defDateTimeFormatter.parse(inputDate);
        } catch (ParseException e) {
            LOGGER.error("CommonUtils::getDefaultDateFromString - Error while parsing  ", e);
            //throw new SystemRuntimeException("error.5001", "CommonUtils::getDefaultDateFromString", e.getStackTrace());
        }
        return date;
    }

    /**
     * Gets the def date time str from date.
     *
     * @param inputDate the input date
     * @param format the format
     * @return the def date time str from date
     */
    public static String getDefDateTimeStrFromDate(Date inputDate, String format) {

        String date = null;

        if (inputDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            date = formatter.format(inputDate);
        }
        return date;
    }

    /**
     * Gets the default date from string.
     *
     * @param inputDate the input date
     * @return the default date from string
     */
    public static Date getDefaultDateFromString(String inputDate) {
        return getDateFromStringOfFormat(inputDate, DEFAULT_DATE_FORMAT);
    }

    /**
     * Gets the hyphen format date from string.
     *
     * @param inputDate the input date
     * @return the hyphen format date from string
     */
    public static Date getHyphenFormatDateFromString(String inputDate) {
        return getDateFromStringOfFormat(inputDate, HYPHEN_DATE_FORMAT);
    }

    /**
     * Gets the hyphen date time str from date.
     *
     * @param inputDate the input date
     * @return the hyphen date time str from date
     */
    public static String getHyphenDateTimeStrFromDate(Date inputDate) {
        return getDefDateTimeStrFromDate(inputDate, HYPHEN_DATE_FORMAT);
    }

    /**
     * Gets the def date time str from date.
     *
     * @param inputDate the input date
     * @return the def date time str from date
     */
    public static String getDefDateTimeStrFromDate(Date inputDate) {
        return getDefDateTimeStrFromDate(inputDate, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * Gets the month date str from date.
     *
     * @param inputDate the input date
     * @return the month date str from date
     */
    public static String getMonthDateStrFromDate(Date inputDate) {
        return getDefDateTimeStrFromDate(inputDate, MONTH_DATE_FORMAT);
    }

    /**
     * Gets the no delim str from date.
     *
     * @param inputDate the input date
     * @return the no delim str from date
     */
    public static String getNoDelimStrFromDate(Date inputDate) {
        return getDefDateTimeStrFromDate(inputDate, NO_DELIM_DATE_FORMAT);
    }

    /**
     * Gets the file date time str from date.
     *
     * @param inputDate the input date
     * @return the file date time str from date
     */
    public static String getFileDateTimeStrFromDate(Date inputDate) {

        String date = null;

        if (inputDate != null) {
            Format formatter = new SimpleDateFormat(FILE_DATETIME_FORMAT_YYYY_MM_DD);
            date = formatter.format(inputDate);
        }
        return date;
    }

    /**
     * Gets the def date str from date.
     *
     * @param inputDate the input date
     * @return the def date str from date
     */
    public static String getDefDateStrFromDate(Date inputDate) {

        String date = null;

        if (inputDate != null) {
            Format formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            date = formatter.format(inputDate);
        }
        return date;
    }

    /**
     * Gets the default new date.
     *
     * @return the default new date
     */
    public static Date getDefaultNewDate() {
        Date date = new Date();
        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(date.getTime());
        return date;
    }

    /**
     * Gets the joda date time zero.
     *
     * @param inputDate the input date
     * @return the joda date time zero
     */
    public static Date getJodaDateTimeZero(Date inputDate) {

        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(inputDate.getTime());
        setTimeToZero(jodaDateTime);
        jodaDateTime.setMillisOfDay(0);
        return jodaDateTime.toDate();
    }

    /**
     * Gets the joda date time zero string.
     *
     * @param inputDate the input date
     * @return the joda date time zero string
     */
    public static String getJodaDateTimeZeroString(Date inputDate) {

        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(inputDate.getTime());
        setTimeToZero(jodaDateTime);
        jodaDateTime.setMillisOfDay(0);
        return getDefDateStrFromDate(jodaDateTime.toDate());
    }

    /**
     * Convert string to long.
     *
     * @param param the param
     * @return the long
     */
    public static long convertStringToLong(String param) {
        return param != null && !"".equals(param) ? Long.parseLong(param) : 0;
    }

    /**
     * Gets the def format utc date from string without time.
     *
     * @param inputDate the input date
     * @return the def format utc date from string without time
     */
    public static Date getDefFormatUTCDateFromStringWithoutTime(String inputDate) {
        DateTime datetime = null;
        DateTimeFormatter frmtr = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);

        datetime = frmtr.parseDateTime(inputDate);

        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth());
        setTimeToZero(jodaDateTime);
        return jodaDateTime.toDate();
    }

    /**
     * Gets the def format utc date from string with eod time.
     *
     * @param inputDate the input date
     * @return the def format utc date from string with eod time
     */
    public static Date getDefFormatUTCDateFromStringWithEODTime(String inputDate) {
        DateTime datetime = null;
        DateTimeFormatter frmtr = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);

        datetime = frmtr.parseDateTime(inputDate);

        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDateTime(datetime.getYear(), datetime.getMonthOfYear(), datetime.getDayOfMonth(), 23, 59, 59, 0);
        return jodaDateTime.toDate();
    }

    /**
     * Sets the time to zero.
     *
     * @param jodaDateTime the new time to zero
     */
    public static void setTimeToZero(MutableDateTime jodaDateTime) {
        jodaDateTime.setHourOfDay(0);
        jodaDateTime.setMinuteOfDay(0);
        jodaDateTime.setSecondOfDay(0);
        jodaDateTime.setMillisOfDay(0);
    }

    /**
     * Gets the end of time utc date.
     *
     * @return the end of time utc date
     */
    public static Date getEndOfTimeUTCDate() {
        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(9999, 12, 31);
        setTimeToZero(jodaDateTime);
        return jodaDateTime.toDate();
    }

    /**
     * Gets the previous month end date.
     *
     * @param date the date
     * @return the previous month end date
     */
    public static Date getPreviousMonthEndDate(Date date) {

        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        jodaDateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        jodaDateTime.addDays(-1);
        return jodaDateTime.toDate();
    }

    /**
     * Are date equal.
     *
     * @param left the left
     * @param right the right
     * @return true, if successful
     */
    public static boolean areDateEqual(Date left, Date right) {

        MutableDateTime jodaDateTimeLeft = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTimeLeft.setDate(left.getTime());

        MutableDateTime jodaDateTimeRight = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTimeRight.setDate(right.getTime());

        if (jodaDateTimeLeft.getYear() == jodaDateTimeRight.getYear() && jodaDateTimeLeft.getMonthOfYear() == jodaDateTimeRight.getMonthOfYear()
                && jodaDateTimeLeft.getDayOfMonth() == jodaDateTimeRight.getDayOfMonth()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the first day month date.
     *
     * @param date the date
     * @return the first day month date
     */
    public static Date getFirstDayMonthDate(Date date) {
        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        jodaDateTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        jodaDateTime.setDayOfMonth(1);
        setTimeToZero(jodaDateTime);
        return jodaDateTime.toDate();
    }

    /**
     * Gets the current month first day date.
     *
     * @return the current month first day date
     */
    public static Date getCurrentMonthFirstDayDate() {
        MutableDateTime jodaDateTime = new MutableDateTime(DEFAULTTIMEZONE);
        jodaDateTime.setDate(new Date().getTime());
        jodaDateTime.setHourOfDay(0);
        jodaDateTime.setMinuteOfDay(0);
        jodaDateTime.setSecondOfDay(0);
        jodaDateTime.setMillisOfDay(0);
        jodaDateTime.setDayOfMonth(1);
        return jodaDateTime.toDate();
    }

    /**
     * Method to convert MultipartFile to Normal file Object.
     *
     * @param multiPartFile the multi part file
     * @return the file
     */
   
    public static File toFile(final MultipartFile multiPartFile) {
        File importFile = null;
        OutputStream out = null;
        if (multiPartFile != null) {
            importFile = new File(multiPartFile.getOriginalFilename());
            try {
                out=new FileOutputStream(importFile);
                out.write(multiPartFile.getBytes());
            } catch (IOException e) {
                LOGGER.error("CommonUtils::toFile - Error IOException ", e);
                //throw new SystemRuntimeException("error.5004", "CommonUtils::toFile", e.getStackTrace());
            } finally {
                try {
                    
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("CommonUtils - Error  IOException", e);
             }
          }
       }
       return importFile;
    }
    
    
    /**
     * <p>
     * Checks whether the <code>String</code> contains only digit characters.
     * </p>
     *
     * <p>
     * <code>Null</code> and empty String will return <code>false</code>.
     * </p>
     *
     * @param str
     *            the <code>String</code> to check
     * @return <code>true</code> if str contains only unicode numeric
     */
    public static boolean isDigits(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    

    

    /**
     * Gets the sort order.
     *
     * @param param the param
     * @return the sort order
     */
    public static int getSortOrder(String param) {

        return param.equals(ApplicationConstants.PAGE_SORT.DESC.getValue()) ? ApplicationConstants.PAGE_SORT.DESC.getIntValue()
                : ApplicationConstants.PAGE_SORT.ASC.getIntValue();
    }

    /**
     * Collection entities name.
     *
     * @return the map
     */
    public static Map<String, String> collectionEntitiesName() {
        Map<String, String> data = new HashMap<String, String>();

        data.put("map_def", "0");
        data.put("rule_def", "0");
        data.put("rule_inst", "0");
        data.put("map_group_def", "0");

        return data;
    }

    /**
     * Gets the list size.
     *
     * @param param the param
     * @return the list size
     */
    public static int getListSize(List<Document> param) {

        return (param != null) ? param.size() : 0;
    }

    /**
     * Checks if is blank.
     *
     * @param str the str
     * @return true, if is blank
     */
    public static boolean isBlank(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Gets the default time zone.
     *
     * @return the default time zone
     */
    public static DateTimeZone getDefaultTimeZone() {
        return DEFAULTTIMEZONE;
    }

    /**
     * Close resource.
     *
     * @param resource the resource
     */
    public static void closeResource(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                LOGGER.error("CommonUtils::closerResource - Error while closing resource: ", e);
            }
        }
    }

    /**
     * Gets the sql to util date.
     *
     * @param inputDate the input date
     * @return the sql to util date
     */
    public static Timestamp getSqlToUtilDate(Date inputDate) {
        return new Timestamp(inputDate.getTime());
    }

}
