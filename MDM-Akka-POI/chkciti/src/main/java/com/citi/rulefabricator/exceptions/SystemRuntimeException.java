/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.exceptions;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.util.ApplicationProperties;

/**
 * The Class SystemRuntimeException.
 */
public class SystemRuntimeException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The message. */
    private final String errorCode, message;
    
    /** The args. */
    private final Object[] args;
    
    /** The stack trace. */
    private final StackTraceElement[] stackTrace;
    
    /** The msgarray. */
    private final String[] msgarray;
    
    /** The exception msg. */
    private final String exceptionMsg;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRuntimeException.class);

    /**
     * Instantiates a new system runtime exception.
     *
     * @param errorKey the error key
     * @param exceptionMessage the exception message
     * @param stackTrace the stack trace
     * @param args the args
     */
    public SystemRuntimeException(final String errorKey, final String exceptionMessage, final StackTraceElement[] stackTrace, final Object... args) {
        this.exceptionMsg = exceptionMessage;
        this.stackTrace = stackTrace;
        this.args = args;
        this.message = ApplicationProperties.getProperty(errorKey);
        if (message.contains("|")) {
            this.msgarray = message.trim().split("\\|");
            this.errorCode = msgarray[0];
        } else {
            this.errorCode = ApplicationConstants.UNKNOWN_ERRORCODE;
            this.msgarray = new String[] { ApplicationConstants.UNDEFINED_ERROR };
        }
        LOGGER.error(getMessage());
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        String msg = this.message;
        if (null != msgarray && msgarray.length > 1) {
            msg = msgarray[1];
        }

        if (args != null && args.length > 0) {
            msg = MessageFormat.format(msg, args);
        }

        if (!StringUtils.isEmpty(exceptionMsg)) {
            msg = msg + ", parentExceptionMessage: " + exceptionMsg;
        }
        return msg;
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the exception msg.
     *
     * @return the exception msg
     */
    public String getExceptionMsg() {
        return exceptionMsg;
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
     * @see java.lang.Throwable#getStackTrace()
     */
    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

}
