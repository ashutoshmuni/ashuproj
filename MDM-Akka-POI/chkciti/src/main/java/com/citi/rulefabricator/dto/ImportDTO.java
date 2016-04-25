/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.citi.rulefabricator.dto.TaskDTO.TaskSource;

/**
 * The Class ImportDTO.
 *
 * @param <T>
 *            the generic type
 */
public class ImportDTO<T> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7074092894655825681L;

    /** The user. */
    private UserDTO user;

    /** The task id. */
    private Long taskId;

    /** The entity. */
    private T entity;

    /** The list of excel data maps. */
    private List<Map<String, String>> listOfExcelDataMaps;

    /** The master change request id. */
    private long masterChangeRequestId;

    /** The task source. */
    private TaskSource taskSource;

    /** The upload import file. */
    private boolean uploadImportFile;

    /** The multipart file. */
    private MultipartFile multipartFile;

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
     *            the new user
     */
    public final void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Gets the task id.
     *
     * @return the task id
     */
    public final Long getTaskId() {
        return taskId;
    }

    /**
     * Sets the task id.
     *
     * @param taskId
     *            the new task id
     */
    public final void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * Gets the entity.
     *
     * @return the entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Sets the entity.
     *
     * @param entity
     *            the new entity
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * Gets the list of excel data maps.
     *
     * @return the list of excel data maps
     */
    public List<Map<String, String>> getListOfExcelDataMaps() {
        return listOfExcelDataMaps;
    }

    /**
     * Sets the list of excel data maps.
     *
     * @param listOfExcelDataMaps
     *            the list of excel data maps
     */
    public void setListOfExcelDataMaps(List<Map<String, String>> listOfExcelDataMaps) {
        this.listOfExcelDataMaps = listOfExcelDataMaps;
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

    /**
     * Gets the task source.
     *
     * @return the task source
     */
    public TaskSource getTaskSource() {
        return taskSource;
    }

    /**
     * Sets the task source.
     *
     * @param taskSource
     *            the new task source
     */
    public void setTaskSource(TaskSource taskSource) {
        this.taskSource = taskSource;
    }

    /**
     * Checks if is upload import file.
     *
     * @return true, if is upload import file
     */
    public boolean isUploadImportFile() {
        return uploadImportFile;
    }

    /**
     * Sets the upload import file.
     *
     * @param uploadImportFile the new upload import file
     */
    public void setUploadImportFile(boolean uploadImportFile) {
        this.uploadImportFile = uploadImportFile;
    }

    /**
     * Gets the multipart file.
     *
     * @return the multipart file
     */
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    /**
     * Sets the multipart file.
     *
     * @param multipartFile the new multipart file
     */
    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

}
