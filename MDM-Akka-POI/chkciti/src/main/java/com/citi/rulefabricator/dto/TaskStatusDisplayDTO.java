/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class TaskStatusDisplayDTO.
 */
public class TaskStatusDisplayDTO extends TaskDTO {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the start dt str.
     *
     * @return the start dt str
     */
    public String getStartDtStr() {
        return getStartDt() == null ? "" : CommonUtils.getDefDateTimeStrFromDate(getStartDt());
    }

    /**
     * Gets the end dt str.
     *
     * @return the end dt str
     */
    public String getEndDtStr() {
        return getEndDt() == null ? "" : CommonUtils.getDefDateTimeStrFromDate(getEndDt());
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        String fileSeparator = java.io.File.separator;
        return getFilePath() == null ? "" : getFilePath().substring(getFilePath().lastIndexOf(fileSeparator) + 1, getFilePath().length());
    }
}
