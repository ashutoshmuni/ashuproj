/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.io.Serializable;
import java.util.Date;

import com.citi.rulefabricator.util.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class BaseDTO.
 */
public abstract class BaseDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7074092894655825681L;

    /** The _id. */
    private Long _id;

    /** The created by. */
    private String createdBy;

    /** The created dt. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_TIME_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date createdDt;

    /** The updated by. */
    private String updatedBy;

    /** The updated dt. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_TIME_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date updatedDt;

    /**
     * Gets the _id.
     *
     * @return the _id
     */
    public Long get_id() {
        return _id;
    }

    /**
     * Sets the _id.
     *
     * @param _id
     *            the new _id
     */
    public void set_id(Long _id) {
        this._id = _id;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *            the new created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the created dt.
     *
     * @return the created dt
     */
    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Sets the created dt.
     *
     * @param createdDt
     *            the new created dt
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Gets the updated by.
     *
     * @return the updated by
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *            the new updated by
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Gets the updated dt.
     *
     * @return the updated dt
     */
    public Date getUpdatedDt() {
        return updatedDt;
    }

    /**
     * Sets the updated dt.
     *
     * @param updatedDt
     *            the new updated dt
     */
    public void setUpdatedDt(Date updatedDt) {
        this.updatedDt = updatedDt;
    }
}