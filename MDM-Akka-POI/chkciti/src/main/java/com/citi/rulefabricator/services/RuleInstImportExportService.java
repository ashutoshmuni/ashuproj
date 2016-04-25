/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.services;

import java.util.concurrent.Future;

import com.citi.rulefabricator.dto.RequestAuthorityDTO;
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

}
