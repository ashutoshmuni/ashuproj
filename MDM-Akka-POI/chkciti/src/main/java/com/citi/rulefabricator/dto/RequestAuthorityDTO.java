/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.io.Serializable;

import com.citi.rulefabricator.enums.ApplicationConstants;

/**
 * The Class RequestAuthorityDTO.
 */
public class RequestAuthorityDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -11100150484563368L;

    /** The page mode. */
    private int pageMode = ApplicationConstants.PAGE_MODE.READ.getValue();

    /** The request id. */
    private long requestId;

    /** The request label. */
    private String requestLabel;

    /** The requested by. */
    private String requestedBy;

    /** The wf status. */
    private String wfStatus;

    /** The error. */
    private boolean error;

    /** The message. */
    private String message;

    /**
     * Checks if is error.
     *
     * @return true, if is error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error
     *            the new error
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the wf status.
     *
     * @return the wf status
     */
    public String getWfStatus() {
        return wfStatus;
    }

    /**
     * Sets the wf status.
     *
     * @param wfStatus
     *            the new wf status
     */
    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    /**
     * Gets the page mode.
     *
     * @return the page mode
     */
    public int getPageMode() {
        return pageMode;
    }

    /**
     * Sets the page mode.
     *
     * @param pageMode
     *            the new page mode
     */
    public void setPageMode(int pageMode) {
        this.pageMode = pageMode;
    }

    /**
     * Gets the request id.
     *
     * @return the request id
     */
    public long getRequestId() {
        return requestId;
    }

    /**
     * Sets the request id.
     *
     * @param requestId
     *            the new request id
     */
    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the request label.
     *
     * @return the request label
     */
    public String getRequestLabel() {
        return requestLabel;
    }

    /**
     * Sets the request label.
     *
     * @param requestLabel
     *            the new request label
     */
    public void setRequestLabel(String requestLabel) {
        this.requestLabel = requestLabel;
    }

    /**
     * Gets the requested by.
     *
     * @return the requested by
     */
    public String getRequestedBy() {
        return requestedBy;
    }

    /**
     * Sets the requested by.
     *
     * @param requestedBy the new requested by
     */
    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
