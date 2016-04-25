/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.util.Date;

import com.citi.rulefabricator.util.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class MapDefDTO.
 */
public class MapDefDTO extends BaseDTO {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3222021616850051993L;

    /** The name. */
    private String name;

    /** The description. */
    private String description;

    /** The effective date. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date effectiveDate;

    /** The status. */
    private String status;

    /**
     * Instantiates a new map def dto.
     *
     * @param mapDefDTO
     *            the map def dto
     */
    public MapDefDTO(MapDefDTO mapDefDTO) {
        if (mapDefDTO != null) {
            this.setName(mapDefDTO.getName());
            this.setDescription(mapDefDTO.getDescription());
            this.setEffectiveDate(mapDefDTO.getEffectiveDate());
            this.set_id(mapDefDTO.get_id());
            this.setCreatedBy(mapDefDTO.getCreatedBy());
            this.setCreatedDt(mapDefDTO.getCreatedDt());
            this.setUpdatedBy(mapDefDTO.getUpdatedBy());
            this.setUpdatedDt(mapDefDTO.getUpdatedDt());
        }
    }

    /**
     * Instantiates a new map def dto.
     */
    public MapDefDTO() {
        super();
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the effective date.
     *
     * @return the effectiveDate
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the effective date.
     *
     * @param effectiveDate
     *            the effectiveDate to set
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
