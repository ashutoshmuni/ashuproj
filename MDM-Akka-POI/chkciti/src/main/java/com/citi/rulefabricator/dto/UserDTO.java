/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.citi.rulefabricator.util.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class UserDTO.
 */
public class UserDTO implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8885912924294310407L;

    /** The soe id. */
    private String soeId;

    /** The is admin. */
    private boolean isAdmin;

    /** The is maker. */
    private boolean isMaker;

    /** The is checker. */
    private boolean isChecker;

    /** The application roles. */
    private List<String> applicationRoles;

    /** The mapping roles maker. */
    private List<String> mappingRolesMaker;

    /** The mapping roles checker. */
    private List<String> mappingRolesChecker;

    /** The system wef date. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date systemWEFDate;

    /** The user selected wef date. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date userSelectedWEFDate;

    /**
     * Gets the soe id.
     *
     * @return the soe id
     */
    public String getSoeId() {
        return soeId;
    }

    /**
     * Sets the soe id.
     *
     * @param soeId
     *            the new soe id
     */
    public void setSoeId(String soeId) {
        this.soeId = soeId;
    }

    /**
     * Checks if is admin.
     *
     * @return true, if is admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the admin.
     *
     * @param isAdmin
     *            the new admin
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Checks if is maker.
     *
     * @return true, if is maker
     */
    public boolean isMaker() {
        return isMaker;
    }

    /**
     * Sets the maker.
     *
     * @param isMaker
     *            the new maker
     */
    public void setMaker(boolean isMaker) {
        this.isMaker = isMaker;
    }

    /**
     * Checks if is checker.
     *
     * @return true, if is checker
     */
    public boolean isChecker() {
        return isChecker;
    }

    /**
     * Sets the checker.
     *
     * @param isChecker
     *            the new checker
     */
    public void setChecker(boolean isChecker) {
        this.isChecker = isChecker;
    }

    /**
     * Gets the application roles.
     *
     * @return the application roles
     */
    public List<String> getApplicationRoles() {
        return applicationRoles;
    }

    /**
     * Sets the application roles.
     *
     * @param applicationRoles
     *            the new application roles
     */
    public void setApplicationRoles(List<String> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    /**
     * Gets the mapping roles maker.
     *
     * @return the mapping roles maker
     */
    public List<String> getMappingRolesMaker() {
        return mappingRolesMaker;
    }

    /**
     * Sets the mapping roles maker.
     *
     * @param mappingRolesMaker
     *            the new mapping roles maker
     */
    public void setMappingRolesMaker(List<String> mappingRolesMaker) {
        this.mappingRolesMaker = mappingRolesMaker;
    }

    /**
     * Gets the mapping roles checker.
     *
     * @return the mapping roles checker
     */
    public List<String> getMappingRolesChecker() {
        return mappingRolesChecker;
    }

    /**
     * Sets the mapping roles checker.
     *
     * @param mappingRolesChecker
     *            the new mapping roles checker
     */
    public void setMappingRolesChecker(List<String> mappingRolesChecker) {
        this.mappingRolesChecker = mappingRolesChecker;
    }

    /**
     * Gets the system wef date.
     *
     * @return the system wef date
     */
    public Date getSystemWEFDate() {
        return systemWEFDate;
    }

    /**
     * Sets the system wef date.
     *
     * @param systemWEFDate
     *            the new system wef date
     */
    public void setSystemWEFDate(Date systemWEFDate) {
        this.systemWEFDate = systemWEFDate;
    }

    /**
     * Gets the user selected wef date.
     *
     * @return the user selected wef date
     */
    public Date getUserSelectedWEFDate() {
        return userSelectedWEFDate;
    }

    /**
     * Sets the user selected wef date.
     *
     * @param userSelectedWEFDate
     *            the new user selected wef date
     */
    public void setUserSelectedWEFDate(Date userSelectedWEFDate) {
        this.userSelectedWEFDate = userSelectedWEFDate;
    }

}
