/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.services.impl;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;

import com.citi.rulefabricator.akka.importers.MultiActorFileImportManager;
import com.citi.rulefabricator.dto.ImportDTO;
import com.citi.rulefabricator.dto.RequestAuthorityDTO;
import com.citi.rulefabricator.dto.TaskDTO;
import com.citi.rulefabricator.dto.TaskDTO.TaskSource;
import com.citi.rulefabricator.dto.UserDTO;
import com.citi.rulefabricator.services.RuleInstImportExportService;
//import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.util.MongoUtil;

/**
 * The Class RuleInstImportExportServiceImpl.
 */
public class RuleInstImportExportServiceImpl implements RuleInstImportExportService {

    /** The task service impl. */
    //private TaskService taskServiceImpl;
    
    /** The multi actor file import manager. */
    private MultiActorFileImportManager multiActorFileImportManager;
    
    /** The mongo util. */
    private MongoUtil mongoUtil;
    
    /** The download file path. */
    private String downloadFilePath;
    
    /** The import file path. */
    private String importFilePath;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleInstImportExportServiceImpl.class);

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
     * @see com.citi.rulefabricator.services.RuleInstImportExportService#importMapData(java.lang.Long, java.lang.Long, java.lang.Long, com.citi.rulefabricator.dto.UserDTO, com.citi.rulefabricator.dto.RequestAuthorityDTO, java.lang.Long)
     */
    @Override
    public Future<TaskDTO> importMapData(Long mapId, Long masterChangeRequestId, Long ruleDefId, UserDTO user, RequestAuthorityDTO authDetails,
            Long taskId) {

        LOGGER.info("RuleInstImportExportService.importMapData start with masterChangeRequestId : {} , User : {} ", masterChangeRequestId,
                user.getSoeId());

        ImportDTO<RuleDef> importDTO = new ImportDTO<RuleDef>();
        //importDTO.setEntity(currentEffectiveRuleDef);
        importDTO.setUser(user);
        importDTO.setMasterChangeRequestId(masterChangeRequestId);
        importDTO.setTaskSource(TaskSource.ONDEMAND);
        importDTO.setTaskId(taskId);

        multiActorFileImportManager.processImport(importDTO);

        TaskDTO task = new TaskDTO();//taskServiceImpl.findOne(importDTO.getTaskId());

        LOGGER.info("RuleInstImportExportService.importMapData submitted to akka with masterChangeRequestId : {}  User : {} ",
                masterChangeRequestId.toString(), user.getSoeId());

        return new AsyncResult<TaskDTO>(task);
    }

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
     * @see com.citi.rulefabricator.services.RuleInstImportExportService#exportRuleInstData(com.citi.rulefabricator.dto.RequestDTO, java.lang.Long, java.lang.Long, java.lang.Boolean, com.citi.rulefabricator.dto.UserDTO, com.citi.rulefabricator.dto.RequestAuthorityDTO)
     */
   /* @Override
    public AsyncImportExportResponseDTO exportRuleInstData(RequestDTO<RuleDefDTO> request, Long mapDefId, Long ruleDefId, Boolean newDataFlag,
            UserDTO user, RequestAuthorityDTO authDetails) {
        LOGGER.info("RuleInstImportExportService.exportRuleInstData start with Map Def Id : {} , Rule Def Id : {} ", mapDefId, ruleDefId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ApplicationConstants.MAP_DEF_ID, mapDefId);
        params.put(ApplicationConstants.RULE_DEF_ID, ruleDefId);
        LOGGER.info("RuleInstImportExportService.exportRuleInstData end with User : {} ", user.getSoeId());
        return ruleInstExportHandler.handleExportToExcel(request, params, user, authDetails, newDataFlag);

    }*/

    /**
     * Export rule inst template.
     *
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param masterId the master id
     * @param user the user
     * @param authDetails the auth details
     * @return the file
     * @see com.citi.rulefabricator.services.RuleInstImportExportService#exportRuleInstTemplate(java.lang.Long, java.lang.Long, java.lang.Long, com.citi.rulefabricator.dto.UserDTO, com.citi.rulefabricator.dto.RequestAuthorityDTO)
     */
    /*@Override
    public File exportRuleInstTemplate(Long mapDefId, Long ruleDefId, Long masterId, UserDTO user, RequestAuthorityDTO authDetails) {
        LOGGER.info("RuleInstImportExportService.exportRuleInstTemplate start with Map Def Id : {} , Rule Def Id : {} ", mapDefId, ruleDefId);
        RuleDef ruleDef = fetchEffRuleDef(masterId, mapDefId, ruleDefId, user, authDetails);
        LOGGER.info("RuleInstImportExportService.exportRuleInstTemplate end with Master Id : {} , user : {} ", masterId, user.getSoeId());
        return ruleInstExportHandler.exportTemplate(new RuleDefDTO(ruleDef), masterId, user);

    }*/

    /**
     * Fetch eff rule def.
     *
     * @param masterChangeRequestId the master change request id
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param user the user
     * @param authDetails the auth details
     * @return the rule def
     */
    /*private RuleDef fetchEffRuleDef(final Long masterChangeRequestId, final Long mapDefId, final Long ruleDefId, final UserDTO user,
            final RequestAuthorityDTO authDetails) {
        RuleDef ruleDefTemplate = null;
        if (null != ruleDefId) {
            Document ruleDefDoc = ruleDefDAOMongoImpl.findOne(ruleDefId);
            if (null != ruleDefDoc) {
                ruleDefTemplate = mongoUtil.documentToPojo(ruleDefDoc, RuleDef.class, Boolean.TRUE);
            } else {
                ruleDefDoc = ruleDefChangeDAOMongoImpl.findOneByMasterIdRuleDefId(masterChangeRequestId, ruleDefId);
                if (null != ruleDefDoc) {
                    RuleDefChange ruleDefChange = mongoUtil.documentToPojo(ruleDefDoc, RuleDefChange.class, Boolean.TRUE);
                    ruleDefTemplate = ruleDefChange.getDelta();
                }
            }
        } else {
            Date workingEffDate = user.getSystemWEFDate();
            if (authDetails.getPageMode() == ApplicationConstants.PAGE_MODE.EDIT.getValue()) {
                workingEffDate = user.getUserSelectedWEFDate();
            }
            Document effRuleDefDoc = ruleDefDAOMongoImpl.getCurrentlyEffectiveRuleForMapId(mapDefId, workingEffDate);
            ruleDefTemplate = mongoUtil.documentToPojo(effRuleDefDoc, RuleDef.class, Boolean.TRUE);
        }

        if (null != ruleDefTemplate) {
            Collections.sort(ruleDefTemplate.getInput(), new AttributeDisplayOrderComparator());
        }

        return ruleDefTemplate;
    }*/

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
     * @see com.citi.rulefabricator.services.RuleInstImportExportService#uploadImportFile(org.springframework.web.multipart.MultipartFile, long, java.lang.Long, java.lang.String, java.lang.Long, com.citi.rulefabricator.dto.UserDTO, com.citi.rulefabricator.dto.RequestAuthorityDTO)
     */
    /*public Future<TaskDTO> uploadImportFile(MultipartFile inFile, long masterChangeRequestId, final Long mapDefId, String mapName, Long ruleDefId,
            UserDTO user, RequestAuthorityDTO authDetails) {
        LOGGER.info("RuleInstImportExportService.uploadImportFile start with Map Def Id : {} , Rule Def Id : {} ", mapDefId, ruleDefId);
        LOGGER.info("RuleInstImportExportService.uploadImportFile start with MapName : {}  ",mapName);
        RuleDef currentEffectiveRuleDef = fetchEffRuleDef(masterChangeRequestId, mapDefId, ruleDefId, user, authDetails);

        // Create a task with Status NEW
        TaskDTO taskDTO = taskServiceImpl.createNewTask(TaskType.IMPORT, TaskCategory.RULE_INST, mapDefId, currentEffectiveRuleDef.getMapName(),
                TaskSource.ONDEMAND, user, TaskStatus.NEW, masterChangeRequestId);

        ImportDTO<RuleDef> importDTO = new ImportDTO<RuleDef>();
        importDTO.setMultipartFile(inFile);
        importDTO.setEntity(currentEffectiveRuleDef);
        importDTO.setUser(user);
        importDTO.setMasterChangeRequestId(masterChangeRequestId);
        importDTO.setTaskSource(TaskSource.ONDEMAND);
        importDTO.setUploadImportFile(true);
        importDTO.setTaskId(taskDTO.get_id());

        multiActorFileImportManager.processImportFileUpload(importDTO);

        TaskDTO task = taskServiceImpl.findOne(importDTO.getTaskId());
        LOGGER.info("RuleInstImportExportService.uploadImportFile end with MasterChangeRequestId : {} , user : {} ", masterChangeRequestId,
                user.getSoeId());
        return new AsyncResult<TaskDTO>(task);

    }*/

    /**
     * Gets the multi actor file import manager.
     *
     * @return the multi actor file import manager
     */
    public MultiActorFileImportManager getMultiActorFileImportManager() {
        return multiActorFileImportManager;
    }

    /**
     * Sets the multi actor file import manager.
     *
     * @param multiActorFileImportManager the new multi actor file import manager
     */
    public void setMultiActorFileImportManager(MultiActorFileImportManager multiActorFileImportManager) {
        this.multiActorFileImportManager = multiActorFileImportManager;
    }

    /**
     * Gets the mongo util.
     *
     * @return the mongo util
     */
    public MongoUtil getMongoUtil() {
        return mongoUtil;
    }

    /**
     * Sets the mongo util.
     *
     * @param mongoUtil the new mongo util
     */
    public void setMongoUtil(MongoUtil mongoUtil) {
        this.mongoUtil = mongoUtil;
    }

    /**
     * Gets the download file path.
     *
     * @return the download file path
     */
    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    /**
     * Sets the download file path.
     *
     * @param downloadFilePath the new download file path
     */
    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    /**
     * Gets the import file path.
     *
     * @return the import file path
     */
    public String getImportFilePath() {
        return importFilePath;
    }

    /**
     * Sets the import file path.
     *
     * @param importFilePath the new import file path
     */
    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
    }
}
