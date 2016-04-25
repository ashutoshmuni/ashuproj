/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

/**
 * The DTO for capturing the response of Import/Export
 *
 * @author das_s
 */
public class ImportExportResponseDTO implements ImportExportResponseBaseDTO {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The message. */
    private String message;

    /** The success flag. */
    private boolean success;

    /**
     * Instantiates a new import export response dto.
     */
    public ImportExportResponseDTO() {
    }

    /**
     * Instantiates a new ImportExportResponseDTO
     * 
     * @param message
     * @param success
     */
    public ImportExportResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
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
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Checks if is success.
     *
     * @return true, if is success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success.
     *
     * @param success the new success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
