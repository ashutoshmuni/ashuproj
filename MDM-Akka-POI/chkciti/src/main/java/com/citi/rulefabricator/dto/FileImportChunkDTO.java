/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.util.List;
import java.util.Map;

import com.citi.rulefabricator.services.entities.RuleDef;

/**
 * The Class FileImportChunkDTO.
 */
public class FileImportChunkDTO {

    /** The user. */
    private UserDTO user;

    /** The task id. */
    private Long taskId;

    /** The rule def. */
    private RuleDef ruleDef;

    /** The list of excel data maps. */
    private List<Map<String, List<String>>> listOfExcelDataMaps;

    /** The master change request id. */
    private long masterChangeRequestId;

    /**
     * Gets the rule def.
     *
     * @return the mapRule
     */
    public final RuleDef getRuleDef() {
        return ruleDef;
    }

    /**
     * Sets the rule def.
     *
     * @param ruleDef
     *            the new rule def
     */
    public final void setRuleDef(RuleDef ruleDef) {
        this.ruleDef = ruleDef;
    }

    /**
     * Gets the list of excel data maps.
     *
     * @return the listOfExcelDataMaps
     */
    public final List<Map<String, List<String>>> getListOfExcelDataMaps() {
        return listOfExcelDataMaps;
    }

    /**
     * Sets the list of excel data maps.
     *
     * @param listOfExcelDataMaps
     *            the listOfExcelDataMaps to set
     */
    public final void setListOfExcelDataMaps(List<Map<String, List<String>>> listOfExcelDataMaps) {
        this.listOfExcelDataMaps = listOfExcelDataMaps;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public final UserDTO getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user
     *            the user to set
     */
    public final void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Gets the task id.
     *
     * @return the taskId
     */
    public final Long getTaskId() {
        return taskId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId
     *            the taskId to set
     */
    public final void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * Gets the master change request id.
     *
     * @return the master change request id
     */
    public long getMasterChangeRequestId() {
        return masterChangeRequestId;
    }

    /**
     * Sets the master change request id.
     *
     * @param masterChangeRequestId
     *            the new master change request id
     */
    public void setMasterChangeRequestId(long masterChangeRequestId) {
        this.masterChangeRequestId = masterChangeRequestId;
    }

}
