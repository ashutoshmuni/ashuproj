/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.akka.importers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

import com.citi.rulefabricator.dto.ImportDTO;
import com.citi.rulefabricator.dto.TaskDTO.TaskStatus;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.exceptions.SystemRuntimeException;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class MultiActorFileImportManager.
 */
@Component
@Scope(value = "prototype")
public class MultiActorFileImportManager implements ApplicationContextAware {

    /** The Constant INSERTION_ACTOR_COUNT. */
    private static final int INSERTION_ACTOR_COUNT = 5;

    /** The application context. */
    private ApplicationContext applicationContext;

    /** The mongo insertion actor. */
    private ActorRef mongoInsertionActor;

    /** The file reader actor. */
    private ActorRef fileReaderActor;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiActorFileImportManager.class);

    private ActorSystem actorSystem;

    //private TaskService taskService;

    private String importFilePath;

    /**
     * Process.
     *
     * @param mapDataImportDTO
     *            the map data import dto
     */
    public void processImport(ImportDTO<RuleDef> mapDataImportDTO) {
        LOGGER.info("MultiActorFileImportManager:Import start with masterRequestId : {} and Task Id : {}",
                mapDataImportDTO.getMasterChangeRequestId(), mapDataImportDTO.getTaskId());

        mongoInsertionActor = actorSystem.actorOf(Props.create(RuleInstMongoInsertionActor.class, applicationContext).withRouter(
                new RoundRobinPool(INSERTION_ACTOR_COUNT)));

        fileReaderActor = actorSystem.actorOf(Props.create(RuleInstFileReaderActor.class, mongoInsertionActor, applicationContext));
        fileReaderActor.tell(mapDataImportDTO, ActorRef.noSender());
        LOGGER.info("Import End with User : {} ", mapDataImportDTO.getUser().getSoeId());
    }

    /**
     * Process import file upload.
     *
     * @param mapDataImportDTO
     *            the map data import dto
     */
    /*public void processImportFileUpload(ImportDTO<RuleDef> mapDataImportDTO) {
        LOGGER.info("MultiActorFileImportManager:Import File Upload start with masterRequestId : {} and Task Id : {}",
                mapDataImportDTO.getMasterChangeRequestId(), mapDataImportDTO.getTaskId());
        processImportUpload(mapDataImportDTO);
        LOGGER.info("MultiActorFileImportManager:Import File Upload end with Rule Def Id : {}", mapDataImportDTO.getEntity().get_id());
    }*/

    /*private void processImportUpload(final ImportDTO<RuleDef> mapDataImportDTO) {
        try {
            // 1. Save the file at "${import.file.path}"/TaskId/
            File file = uploadImportFile(mapDataImportDTO.getMultipartFile(), importFilePath, String.valueOf(mapDataImportDTO.getTaskId()));

            // 2. Update the task with the filePath
            taskService.updateTask(mapDataImportDTO.getTaskId(), TaskStatus.NEW, ApplicationConstants.MSG_FILE_UPLOAD_SUCCESSFUL,
                    file.getAbsolutePath(), mapDataImportDTO.getUser());
        } catch (IOException exe) {
            taskService.updateTask(mapDataImportDTO.getTaskId(), TaskStatus.FAILED, ApplicationConstants.UPLOAD_FAILED + exe.getMessage(), null,
                    mapDataImportDTO.getUser());
            LOGGER.error("MultiActorFileImportManager::processImportUpload - Error IOException ", exe);
        }
    }*/

    private File uploadImportFile(final MultipartFile multiPartFile, String importBasePath, String taskId) throws IOException {
        File importFile = null;
        String fileSeparator = java.io.File.separator;
        DateFormat dateFormat = new SimpleDateFormat(CommonUtils.FILE_DATETIME_FORMAT_YYYY_MM_DD);
        Date date = CommonUtils.getDefaultNewDate();
        String timeStr = dateFormat.format(date);
        boolean dirExists = true;
        String dirPath = importBasePath + fileSeparator + taskId;

        File dir = new File(dirPath);

        if (multiPartFile != null) {
            FileOutputStream out = null;
            try {
                if (!dir.exists()) {
                    dirExists = dir.mkdirs();
                }
                if (dirExists) {
                    importFile = new File(dirPath + fileSeparator
                            + multiPartFile.getOriginalFilename().substring(0, multiPartFile.getOriginalFilename().indexOf('.'))
                            + ApplicationConstants.UNDERSCORECHAR + timeStr + ApplicationConstants.EXCELEXTN);
                    out = new FileOutputStream(importFile);
                    out.write(multiPartFile.getBytes());
                }

            } catch (IOException e) {
                LOGGER.error("MultiActorFileImportManager::uploadImportFile - Error IOException ", e);
                //throw new SystemRuntimeException("ERROR", "MultiActorFileImportManager::uploadImportFile", e.getStackTrace());
            } finally {
                CommonUtils.closeResource(out);
            }
        }

        return importFile;
    }

    /**
     * Sets the application context.
     *
     * @param context the new application context
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;

    }

    /**
     * Gets the mongo insertion actor.
     *
     * @return the mongo insertion actor
     */
    public ActorRef getMongoInsertionActor() {
        return mongoInsertionActor;
    }

    /**
     * Sets the mongo insertion actor.
     *
     * @param mongoInsertionActor
     *            the new mongo insertion actor
     */
    public void setMongoInsertionActor(ActorRef mongoInsertionActor) {
        this.mongoInsertionActor = mongoInsertionActor;
    }

    /**
     * Gets the file reader actor.
     *
     * @return the file reader actor
     */
    public ActorRef getFileReaderActor() {
        return fileReaderActor;
    }

    /**
     * Sets the file reader actor.
     *
     * @param fileReaderActor
     *            the new file reader actor
     */
    public void setFileReaderActor(ActorRef fileReaderActor) {
        this.fileReaderActor = fileReaderActor;
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param actorSystem
     *            the actorSystem to set
     */
    public final void setActorSystem(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    /**
     * @param taskService
     *            the taskService to set
     */
//    public void setTaskService(TaskService taskService) {
//        this.taskService = taskService;
//    }

    /**
     * @param importFilePath
     *            the importFilePath to set
     */
    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
    }
}
