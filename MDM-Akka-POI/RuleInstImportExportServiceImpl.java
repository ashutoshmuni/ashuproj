/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.services.impl;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.multipart.MultipartFile;

import com.citi.rulefabricator.akka.importers.MultiActorFileImportManager;
import com.citi.rulefabricator.dao.mongo.impl.RuleDefChangeDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.RuleDefDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.RuleInstDAOMongoImpl;
import com.citi.rulefabricator.dto.AsyncImportExportResponseDTO;
import com.citi.rulefabricator.dto.ImportDTO;
import com.citi.rulefabricator.dto.RequestAuthorityDTO;
import com.citi.rulefabricator.dto.RequestDTO;
import com.citi.rulefabricator.dto.RuleDefDTO;
import com.citi.rulefabricator.dto.TaskDTO;
import com.citi.rulefabricator.dto.TaskDTO.TaskCategory;
import com.citi.rulefabricator.dto.TaskDTO.TaskSource;
import com.citi.rulefabricator.dto.TaskDTO.TaskStatus;
import com.citi.rulefabricator.dto.TaskDTO.TaskType;
import com.citi.rulefabricator.dto.UserDTO;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.export.handler.IExportHandler;
import com.citi.rulefabricator.mongo.MongoUtil;
import com.citi.rulefabricator.services.RuleInstImportExportService;
import com.citi.rulefabricator.services.RuleInstService;
import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.services.entities.RuleDefChange;
import com.citi.rulefabricator.util.AttributeDisplayOrderComparator;

/**
 * The Class RuleInstImportExportServiceImpl.
 */
public class RuleInstImportExportServiceImpl implements RuleInstImportExportService {

    /** The task service impl. */
    private TaskService taskServiceImpl;
    
    /** The multi actor file import manager. */
    private MultiActorFileImportManager multiActorFileImportManager;
    
    /** The rule def dao mongo impl. */
    private RuleDefDAOMongoImpl ruleDefDAOMongoImpl;
    
    /** The rule inst dao mongo impl. */
    private RuleInstDAOMongoImpl ruleInstDAOMongoImpl;
    
    /** The rule def change dao mongo impl. */
    private RuleDefChangeDAOMongoImpl ruleDefChangeDAOMongoImpl;
    
    /** The mongo util. */
    private MongoUtil mongoUtil;
    
    /** The download file path. */
    private String downloadFilePath;
    
    /** The import file path. */
    private String importFilePath;

    /** The rule inst service. */
    private RuleInstService ruleInstService;
    
    /** The rule inst export handler. */
    private IExportHandler<AsyncImportExportResponseDTO, RuleDefDTO> ruleInstExportHandler;

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
        RuleDef currentEffectiveRuleDef = fetchEffRuleDef(masterChangeRequestId, mapId, ruleDefId, user, authDetails);

        ImportDTO<RuleDef> importDTO = new ImportDTO<RuleDef>();
        importDTO.setEntity(currentEffectiveRuleDef);
        importDTO.setUser(user);
        importDTO.setMasterChangeRequestId(masterChangeRequestId);
        importDTO.setTaskSource(TaskSource.ONDEMAND);
        importDTO.setTaskId(taskId);

        multiActorFileImportManager.processImport(importDTO);

        TaskDTO task = taskServiceImpl.findOne(importDTO.getTaskId());

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
    @Override
    public AsyncImportExportResponseDTO exportRuleInstData(RequestDTO<RuleDefDTO> request, Long mapDefId, Long ruleDefId, Boolean newDataFlag,
            UserDTO user, RequestAuthorityDTO authDetails) {
        LOGGER.info("RuleInstImportExportService.exportRuleInstData start with Map Def Id : {} , Rule Def Id : {} ", mapDefId, ruleDefId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ApplicationConstants.MAP_DEF_ID, mapDefId);
        params.put(ApplicationConstants.RULE_DEF_ID, ruleDefId);
        LOGGER.info("RuleInstImportExportService.exportRuleInstData end with User : {} ", user.getSoeId());
        return ruleInstExportHandler.handleExportToExcel(request, params, user, authDetails, newDataFlag);

    }

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
    @Override
    public File exportRuleInstTemplate(Long mapDefId, Long ruleDefId, Long masterId, UserDTO user, RequestAuthorityDTO authDetails) {
        LOGGER.info("RuleInstImportExportService.exportRuleInstTemplate start with Map Def Id : {} , Rule Def Id : {} ", mapDefId, ruleDefId);
        RuleDef ruleDef = fetchEffRuleDef(masterId, mapDefId, ruleDefId, user, authDetails);
        LOGGER.info("RuleInstImportExportService.exportRuleInstTemplate end with Master Id : {} , user : {} ", masterId, user.getSoeId());
        return ruleInstExportHandler.exportTemplate(new RuleDefDTO(ruleDef), masterId, user);

    }

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
    private RuleDef fetchEffRuleDef(final Long masterChangeRequestId, final Long mapDefId, final Long ruleDefId, final UserDTO user,
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
    }

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
    public Future<TaskDTO> uploadImportFile(MultipartFile inFile, long masterChangeRequestId, final Long mapDefId, String mapName, Long ruleDefId,
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

    }

    /**
     * Gets the task service impl.
     *
     * @return the task service impl
     */
    public TaskService getTaskServiceImpl() {
        return taskServiceImpl;
    }

    /**
     * Sets the task service impl.
     *
     * @param taskServiceImpl the new task service impl
     */
    public void setTaskServiceImpl(TaskService taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

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
     * Gets the rule def dao mongo impl.
     *
     * @return the rule def dao mongo impl
     */
    public RuleDefDAOMongoImpl getRuleDefDAOMongoImpl() {
        return ruleDefDAOMongoImpl;
    }

    /**
     * Sets the rule def dao mongo impl.
     *
     * @param ruleDefDAOMongoImpl the new rule def dao mongo impl
     */
    public void setRuleDefDAOMongoImpl(RuleDefDAOMongoImpl ruleDefDAOMongoImpl) {
        this.ruleDefDAOMongoImpl = ruleDefDAOMongoImpl;
    }

    /**
     * Gets the rule inst dao mongo impl.
     *
     * @return the rule inst dao mongo impl
     */
    public RuleInstDAOMongoImpl getRuleInstDAOMongoImpl() {
        return ruleInstDAOMongoImpl;
    }

    /**
     * Sets the rule inst dao mongo impl.
     *
     * @param ruleInstDAOMongoImpl the new rule inst dao mongo impl
     */
    public void setRuleInstDAOMongoImpl(RuleInstDAOMongoImpl ruleInstDAOMongoImpl) {
        this.ruleInstDAOMongoImpl = ruleInstDAOMongoImpl;
    }

    /**
     * Gets the rule def change dao mongo impl.
     *
     * @return the rule def change dao mongo impl
     */
    public RuleDefChangeDAOMongoImpl getRuleDefChangeDAOMongoImpl() {
        return ruleDefChangeDAOMongoImpl;
    }

    /**
     * Sets the rule def change dao mongo impl.
     *
     * @param ruleDefChangeDAOMongoImpl the new rule def change dao mongo impl
     */
    public void setRuleDefChangeDAOMongoImpl(RuleDefChangeDAOMongoImpl ruleDefChangeDAOMongoImpl) {
        this.ruleDefChangeDAOMongoImpl = ruleDefChangeDAOMongoImpl;
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

    /**
     * Cancel task.
     *
     * @param taskId the task id
     * @return true, if successful
     * @see com.citi.rulefabricator.services.RuleInstImportExportService#cancelTask(java.lang.Long)
     */
    @Override
    public boolean cancelTask(Long taskId) {

        return taskServiceImpl.deleteTask(taskId);
    }

    /**
     * Gets the rule inst service.
     *
     * @return the rule inst service
     */
    public RuleInstService getRuleInstService() {
        return ruleInstService;
    }

    /**
     * Sets the rule inst service.
     *
     * @param ruleInstService the new rule inst service
     */
    public void setRuleInstService(RuleInstService ruleInstService) {
        this.ruleInstService = ruleInstService;
    }

    /**
     * Sets the rule inst service.
     *
     * @param ruleInstService the new rule inst service
     */
    public void setRuleInstService(RuleInstServiceImpl ruleInstService) {
        this.ruleInstService = ruleInstService;
    }

    /**
     * Gets the rule inst export handler.
     *
     * @return the rule inst export handler
     */
    public IExportHandler<AsyncImportExportResponseDTO, RuleDefDTO> getRuleInstExportHandler() {
        return ruleInstExportHandler;
    }

    /**
     * Sets the rule inst export handler.
     *
     * @param ruleInstExportHandler the rule inst export handler
     */
    public void setRuleInstExportHandler(IExportHandler<AsyncImportExportResponseDTO, RuleDefDTO> ruleInstExportHandler) {
        this.ruleInstExportHandler = ruleInstExportHandler;
    }
}
