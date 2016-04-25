/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.exceptions;

import java.text.MessageFormat;

import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.util.ApplicationProperties;

/**
 * The Class ApplicationException.
 */
public class ApplicationException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The message. */
    private final String errorCode, message;
    
    /** The args. */
    private final Object[] args;
    
    /** The msgarray. */
    private final String[] msgarray;

    /**
     * Instantiates a new application exception.
     *
     * @param key the key
     * @param args the args
     */
    public ApplicationException(String key, Object... args) {
        this.message = ApplicationProperties.getProperty(key);
        this.args = args;
        if (message.contains("|")) {
            this.msgarray = message.trim().split("\\|");
            this.errorCode = msgarray[0];
        } else {
            this.errorCode = ApplicationConstants.UNKNOWN_ERRORCODE;
            this.msgarray = new String[] { ApplicationConstants.UNDEFINED_ERROR };
        }
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        String msg = "";
        if (null != msgarray && msgarray.length > 1) {
            msg = msgarray[1];
        }
        if (args != null && args.length > 0) {
            msg = MessageFormat.format(msg, args);
        }
        return msg;
    }

    /**
     * Gets the UI message.
     *
     * @return Message to be displayed on the client.
     */
    public String getUIMessage() {
        if (null != msgarray && msgarray.length > 2) {
            return msgarray[2];
        }
        return message;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
