/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.services;

import java.io.File;
import java.util.concurrent.Future;

import org.springframework.web.multipart.MultipartFile;

import com.citi.rulefabricator.dto.AsyncImportExportResponseDTO;
import com.citi.rulefabricator.dto.RequestAuthorityDTO;
import com.citi.rulefabricator.dto.RequestDTO;
import com.citi.rulefabricator.dto.RuleDefDTO;
import com.citi.rulefabricator.dto.TaskDTO;
import com.citi.rulefabricator.dto.UserDTO;

/**
 * The Interface RuleInstImportExportService.
 */
public interface RuleInstImportExportService {
    
    /**
     * Import map data.
     *
     * @param mapId the map id
     * @param masterChangeRequestId the master change request id
     * @param ruleDefId the rule def id
     * @param user the user
     * @param authDetails the auth details
     * @param taskId the task id
     * @return the future
     */
    public Future<TaskDTO> importMapData(Long mapId, Long masterChangeRequestId, Long ruleDefId, UserDTO user, RequestAuthorityDTO authDetails,
            Long taskId);

    /**
     * Upload import file.
     *
     * @param inFile the in file
     * @param masterChangeRequestId the master change request id
     * @param mapDefId the map def id
     * @param mapName the map name
     * @param ruleDefId the rule def id
     * @param user the user
     * @param authDetails the auth details
     * @return the future
     */
    public Future<TaskDTO> uploadImportFile(MultipartFile inFile, long masterChangeRequestId, final Long mapDefId, String mapName, Long ruleDefId,
            UserDTO user, RequestAuthorityDTO authDetails);

    /**
     * Export rule inst data.
     *
     * @param request the request
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param newDataFlag the new data flag
     * @param user the user
     * @param authDetails the auth details
     * @return the async import export response dto
     */
    public AsyncImportExportResponseDTO exportRuleInstData(RequestDTO<RuleDefDTO> request, Long mapDefId, Long ruleDefId, Boolean newDataFlag,
            UserDTO user, RequestAuthorityDTO authDetails);

    /**
     * Export rule inst template.
     *
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param masterId the master id
     * @param user the user
     * @param authDetails the auth details
     * @return the file
     */
    public File exportRuleInstTemplate(Long mapDefId, Long ruleDefId, Long masterId, UserDTO user, RequestAuthorityDTO authDetails);

    /**
     * Cancel task.
     *
     * @param taskId the task id
     * @return true, if successful
     */
    public boolean cancelTask(Long taskId);
}
