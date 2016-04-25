/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.io.Serializable;
import java.util.List;

import com.citi.rulefabricator.enums.ApplicationConstants;

/**
 * The Class ResponseDTO.
 *
 * @param <T>
 *            the generic type
 */
public class ResponseDTO<T extends BaseDTO> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2331104065578487708L;

    /** The status. */
    private int status = ApplicationConstants.RESPONSE_STATUS.SUCCESS.getValue();

    /** The messages. */
    private List<String> messages;

    /** The data. */
    private List<T> data;

    /** The ref data. */
    private List<Object> refData;

    /** The user. */
    private UserDTO user;

    /** The authorization mode. */
    private int authorizationMode;

    /** The request id. */
    private long requestId;

    /** The request label. */
    private String requestLabel;

    /** The request status. */
    private String requestStatus;

    /** The requested by. */
    private String requestedBy;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *            the new data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the messages.
     *
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sets the messages.
     *
     * @param messages
     *            the new messages
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user
     *            the new user
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Gets the ref data.
     *
     * @return the ref data
     */
    public List<Object> getRefData() {
        return refData;
    }

    /**
     * Sets the ref data.
     *
     * @param refData
     *            the new ref data
     */
    public void setRefData(List<Object> refData) {
        this.refData = refData;
    }

    /**
     * Gets the authorization mode.
     *
     * @return the authorization mode
     */
    public int getAuthorizationMode() {
        return authorizationMode;
    }

    /**
     * Sets the authorization mode.
     *
     * @param authorizationMode
     *            the new authorization mode
     */
    public void setAuthorizationMode(int authorizationMode) {
        this.authorizationMode = authorizationMode;
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
     * Gets the request status.
     *
     * @return the request status
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets the request status.
     *
     * @param requestStatus
     *            the new request status
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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
