/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.citi.rulefabricator.dto.ExportResponseDTO;
import com.citi.rulefabricator.dto.ImportExportResponseDTO;
import com.citi.rulefabricator.dto.MapDefDTO;
import com.citi.rulefabricator.dto.PageDTO;
import com.citi.rulefabricator.dto.RequestAuthorityDTO;
import com.citi.rulefabricator.dto.RequestDTO;
import com.citi.rulefabricator.dto.ResponseDTO;
import com.citi.rulefabricator.dto.RuleDefDTO;
import com.citi.rulefabricator.dto.SearchDTO;
import com.citi.rulefabricator.dto.TaskDTO;
import com.citi.rulefabricator.dto.TaskStatusDisplayDTO;
import com.citi.rulefabricator.dto.UserDTO;
import com.citi.rulefabricator.dto.ValidationFailsDTO;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.services.MapDefService;
import com.citi.rulefabricator.services.MyRequestsService;
import com.citi.rulefabricator.services.RuleInstImportExportService;
import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.ValidationFailsService;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class RuleInstImportExportController.
 */
@Controller
@RequestMapping(value = "/ruleInstImportExport")
public class RuleInstImportExportController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleInstImportExportController.class);

    /** Size of a byte buffer to read/write file. */
    private static final int BUFFER_SIZE = 4096;

    /** The rule inst import export service impl. */
    @Autowired
    private RuleInstImportExportService ruleInstImportExportServiceImpl;

    /** The map def service. */
    @Autowired
    private MapDefService mapDefService;

    /** The task service. */
    @Autowired
    private TaskService taskService;

    /** The validation fails service. */
    @Autowired
    private ValidationFailsService validationFailsService;

    /** The my requests service. */
    @Autowired
    private MyRequestsService myRequestsService;

    /**
     * Import map data.
     *
     * @param mapId            the map id
     * @param ruleDefId the rule def id
     * @param masterChangeRequestId            the master change request id
     * @param taskId the task id
     * @param session            the session
     * @return the string
     */
    @RequestMapping(value = "/startImport/{mapId}", method = RequestMethod.GET)
    @ResponseBody
    public ImportExportResponseDTO startImport(@PathVariable Long mapId, @RequestParam("ruleDefId") Long ruleDefId,
            @RequestParam("masterChangeRequestId") String masterChangeRequestId, @RequestParam("taskId") Long taskId, HttpSession session) {

        ImportExportResponseDTO response = new ImportExportResponseDTO();
        response.setMessage(ApplicationConstants.MSG_FILE_IMPORT_SUCCESSFUL);
        response.setSuccess(true);

        if (taskId == null || taskId.longValue() <= 0) {
            response.setMessage(ApplicationConstants.MSG_NO_FILE_RECEIVED);
            response.setSuccess(false);
            return response;
        }

        MapDefDTO mapDefDTO = null;
        Long masterId = CommonUtils.convertStringToLong(masterChangeRequestId);

        try {

            final UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);
            RequestAuthorityDTO authDetails = myRequestsService.authorizeRequest(masterId, user);

            if (!authDetails.isError()) {
                // Search Map based on MapId
                mapDefDTO = mapDefService.findExistingOrNewlyCreatedMap(masterId, mapId);
                if (mapDefDTO == null) {
                    response.setMessage(ApplicationConstants.MSG_IMPORT_FAILURE_INVALID_MAPID);
                    response.setSuccess(false);
                    return response;
                } else {
                    ruleInstImportExportServiceImpl.importMapData(mapId, masterId, ruleDefId, user, authDetails, taskId);
                }
                response.setMessage(ApplicationConstants.MSG_FILE_IMPORT_SUCCESSFUL);
                response.setSuccess(true);

            } else {
                response.setMessage(authDetails.getMessage());
                response.setSuccess(false);

            }
            return response;
        } catch (Exception e) {
            LOGGER.error("RuleInstImportExportController::startImport - Exception ", e);
            return response;
        }
    }

    /**
     * View task status.
     *
     * @param taskType            the task type
     * @param taskCategory            the task category
     * @param taskCategoryId            the task category id
     * @param pageSize the page size
     * @param pageNo the page no
     * @param session the session
     * @return the response dto
     */
    @RequestMapping(value = "/viewTaskStatus/{taskType}/{taskCategory}/{taskCategoryId}/{pageSize}/{pageNo}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO<TaskStatusDisplayDTO> viewTaskStatus(@PathVariable String taskType, @PathVariable String taskCategory,
            @PathVariable Long taskCategoryId, @PathVariable("pageSize") String pageSize, @PathVariable("pageNo") String pageNo, HttpSession session) {

        List<TaskStatusDisplayDTO> listOfTaskStatus = null;
        ResponseDTO<TaskStatusDisplayDTO> response = new ResponseDTO<TaskStatusDisplayDTO>();
        PageDTO page = new PageDTO();
        final UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);
        if (!CommonUtils.isBlank(pageNo)) {
            page.setNumber(Integer.parseInt(pageNo));
        }
        if (!CommonUtils.isBlank(pageSize)) {
            page.setSize(Integer.parseInt(pageSize));
        }

        SearchDTO search = new SearchDTO();

        try {
            listOfTaskStatus = taskService.findAllTasks(taskType, taskCategory, taskCategoryId, page, user);
            search.setPage(page);
            response.setSearch(search);
            response.setData(listOfTaskStatus);

        } catch (Exception e) {
            LOGGER.error("RuleInstImportExportController::viewTaskStatus - Error IOException ", e);
        }
        return response;
    }

    /**
     * View failed imports.
     * 
     * @param taskId
     *            the task id
     * @param pageSize
     *            the page size
     * @param pageNo
     *            the page no
     * @return the response dto
     */
    @RequestMapping(value = "/viewFailedImports/{taskId}/{pageSize}/{pageNo}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDTO<ValidationFailsDTO> viewFailedImports(@PathVariable("taskId") String taskId, @PathVariable("pageSize") String pageSize,
            @PathVariable("pageNo") String pageNo) {

        List<ValidationFailsDTO> validationFailsList = null;
        ResponseDTO<ValidationFailsDTO> response = new ResponseDTO<ValidationFailsDTO>();
        if (!CommonUtils.isBlank(taskId)) {

            PageDTO pageDTO = new PageDTO();

            if (!CommonUtils.isBlank(pageNo)) {
                pageDTO.setNumber(Integer.valueOf(pageNo));
            }

            if (!CommonUtils.isBlank(pageSize)) {
                pageDTO.setSize(Integer.valueOf(pageSize));
            }
            validationFailsList = validationFailsService.getValidationFails(Long.valueOf(taskId), pageDTO);

            pageDTO.setTotalRecords(validationFailsService.getTotalValidationFails(Long.valueOf(taskId)));

            response.setSearch(new SearchDTO());
            response.getSearch().setPage(pageDTO);
            response.setData(validationFailsList);

        } else {
            response.setMessages(Arrays.asList(ApplicationConstants.MSG_NO_TASK_ID));
        }

        return response;
    }

    /**
     * Export rule inst data.
     *
     * @param request the request
     * @param session the session
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param newData the new data
     * @return the import export response dto
     */
    @RequestMapping(value = "/exportRuleInst/{mapDefId}/{ruleDefId}", method = RequestMethod.POST)
    @ResponseBody
    public ImportExportResponseDTO exportRuleInstData(@RequestBody RequestDTO<RuleDefDTO> request, HttpSession session,
            @PathVariable String mapDefId, @PathVariable String ruleDefId, boolean newData) {

        UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);
        ImportExportResponseDTO response = new ImportExportResponseDTO();

        try {
            RequestAuthorityDTO authDetails = myRequestsService.authorizeRequest(CommonUtils.convertStringToLong(request.getRequestId()), user);
            if (!authDetails.isError()) {

                Long mapId = CommonUtils.convertStringToLong(mapDefId);
                Long ruleId = CommonUtils.convertStringToLong(ruleDefId);
                MapDefDTO mapDefDTO = mapDefService.findOne(mapId, user);
                if (mapDefDTO == null) {
                    response.setMessage(ApplicationConstants.MSG_EXPORT_FAILURE_INVALID_MAPID);
                    response.setSuccess(false);
                } else {
                    response = ruleInstImportExportServiceImpl.exportRuleInstData(request, mapId, ruleId, newData, user, authDetails);
                }
            } else {
                response.setSuccess(false);
                response.setMessage(authDetails.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("RuleInstImportExportController::exportRuleInstData - Exception ", e);
            response.setMessage(ApplicationConstants.MSG_EXPORT_FAILURE);
            response.setSuccess(false);
            return response;
        }
        return response;
    }

    /**
     * Export template.
     *
     * @param mapDefId the map def id
     * @param ruleDefId the rule def id
     * @param masterChangeRequestId the master change request id
     * @param session the session
     * @param request the request
     * @param response the response
     * @return the export response dto
     */
    @RequestMapping(value = "/export/template/{mapDefId}/{ruleDefId}", method = RequestMethod.GET)
    @ResponseBody
    public ExportResponseDTO exportTemplate(@PathVariable Long mapDefId, @PathVariable Long ruleDefId,
            @RequestParam(value = "masterChangeRequestId", defaultValue = "0") String masterChangeRequestId, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) {

        UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);
        ExportResponseDTO result = new ExportResponseDTO();
        OutputStream outStream = null;
        FileInputStream inputStream = null;

        try {
            Long masterId = CommonUtils.convertStringToLong(masterChangeRequestId);
            RequestAuthorityDTO authDetails = myRequestsService.authorizeRequest(masterId, user);
            if (!authDetails.isError()) {

                MapDefDTO mapDefDTO = mapDefService.findOne(mapDefId, user);
                if (mapDefDTO == null) {
                    result.setMessage(ApplicationConstants.MSG_EXPORT_FAILURE_INVALID_MAPID);
                    result.setSuccess(false);
                } else {
                    File templateFile = ruleInstImportExportServiceImpl.exportRuleInstTemplate(mapDefId, ruleDefId, masterId, user, authDetails);
                    inputStream = new FileInputStream(templateFile);

                    // get MIME type of the file
                    String mimeType = request.getSession().getServletContext().getMimeType(templateFile.getAbsolutePath());
                    if (mimeType == null) {
                        // set to binary type if MIME mapping not found
                        mimeType = ApplicationConstants.MIMETYPE;
                    }

                    // set content attributes for the response
                    response.setContentType(mimeType);
                    response.setContentLength((int) templateFile.length());

                    // set headers for the response
                    String headerKey = ApplicationConstants.HEADERKEY;
                    String headerValue = String.format(ApplicationConstants.HEADERVALUE, templateFile.getName());
                    response.setHeader(headerKey, headerValue);

                    // get output stream of the response
                    outStream = response.getOutputStream();

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead = -1;

                    // write bytes read from the input stream into the output
                    // stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                result.setSuccess(false);
                result.setMessage(authDetails.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("RuleInstImportExportController::exportRuleInstData - Exception ", e);
            result.setMessage(ApplicationConstants.MSG_EXPORT_FAILURE);
            result.setSuccess(false);
            return result;
        } finally {
            CommonUtils.closeResource(inputStream);
            CommonUtils.closeResource(outStream);
        }
        return result;
    }

    /**
     * Download file.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param taskId
     *            the task id
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("taskId") Long taskId) {
        TaskDTO taskDTO = taskService.findOne(taskId);
        String filePath = taskDTO.getFilePath();
        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            // get absolute path of the application
            // construct the complete absolute path of the file
            // appPath + filePath
            String fullPath = filePath;
            File downloadFile = new File(fullPath);

            inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = request.getSession().getServletContext().getMimeType(fullPath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("RuleInstImportExportController::downloadFile - Error FileNotFoundException ", e);
        } catch (IOException e) {
            LOGGER.error("RuleInstImportExportController::downloadFile - Error IOException ", e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outStream) {
                    outStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("RuleInstImportExportController::downloadFile - Error IOException ", e);
            }
        }

    }

    /**
     * Upload import file.
     *
     * @param mapId the map id
     * @param ruleDefId the rule def id
     * @param multiPartFile the multi part file
     * @param masterChangeRequestId the master change request id
     * @param session the session
     * @param response the response
     */
    @RequestMapping(value = "/uploadImportFile/{mapId}", method = RequestMethod.POST)
    @ResponseBody
    public void uploadImportFile(@PathVariable Long mapId, @RequestParam("ruleDefId") Long ruleDefId,
            @RequestParam("file") MultipartFile multiPartFile, @RequestParam("masterChangeRequestId") String masterChangeRequestId,
            HttpSession session, HttpServletResponse response) {

        ObjectMapper mapper = new ObjectMapper();
        ImportExportResponseDTO responseDTO = new ImportExportResponseDTO();
        response.setContentType("text/html;charset=UTF-8");
        String respJson = null;
        responseDTO.setMessage(ApplicationConstants.MSG_FILE_UPLOAD_STARTED);
        responseDTO.setSuccess(true);
        final UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);

        MapDefDTO mapDefDTO = null;
        Long masterId = CommonUtils.convertStringToLong(masterChangeRequestId);

        try {
            if (multiPartFile == null || multiPartFile.isEmpty()) {
                responseDTO.setMessage(ApplicationConstants.MSG_NO_FILE_RECEIVED);
                responseDTO.setSuccess(false);
                respJson = mapper.writeValueAsString(responseDTO);
                response.getWriter().write(respJson);
                response.flushBuffer();
                return;
            }

            RequestAuthorityDTO authDetails = myRequestsService.authorizeRequest(masterId, user);

            if (!authDetails.isError()) {
                // Search Map based on MapId
                mapDefDTO = mapDefService.findExistingOrNewlyCreatedMap(masterId, mapId);
                if (mapDefDTO == null) {
                    responseDTO.setMessage(ApplicationConstants.MSG_UPLOAD_FAILURE_INVALID_MAPID);
                    responseDTO.setSuccess(false);

                } else {
                    ruleInstImportExportServiceImpl.uploadImportFile(multiPartFile, masterId, mapId, mapDefDTO.getName(), ruleDefId, user,
                            authDetails);
                }

            } else {
                responseDTO.setMessage(authDetails.getMessage());
                responseDTO.setSuccess(false);
            }
            respJson = mapper.writeValueAsString(responseDTO);
            response.getWriter().write(respJson);
            response.flushBuffer();
        } catch (Exception e) {
            LOGGER.error("RuleInstImportExportController::uploadImportFile - Error happend during file upload ", e);
            responseDTO.setMessage(ApplicationConstants.MSG_NO_FILE_RECEIVED);
            responseDTO.setSuccess(false);
            try {
                respJson = mapper.writeValueAsString(responseDTO);
                response.getWriter().write(respJson);
                response.flushBuffer();
            } catch (Exception exc) {
                LOGGER.error(exc.getMessage(), exc);
            }
        }
    }

    /**
     * Cancel task.
     *
     * @param masterChangeRequestId the master change request id
     * @param taskId the task id
     * @param session the session
     * @return the import export response dto
     * @throws Exception the exception
     */
    @RequestMapping(value = "/cancelTask/{taskId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ImportExportResponseDTO cancelTask(@RequestParam final Long masterChangeRequestId, @PathVariable("taskId") Long taskId, HttpSession session)
            throws Exception {
        ImportExportResponseDTO response = new ImportExportResponseDTO();

        if (taskId != null && taskId.longValue() > -1) {
            final UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);
            RequestAuthorityDTO authDetails = myRequestsService.authorizeRequest(masterChangeRequestId, user);

            if (!authDetails.isError()) {
                boolean result = ruleInstImportExportServiceImpl.cancelTask(taskId);
                if (result) {
                    response.setMessage(ApplicationConstants.CANCEL_SUCCESSFUL);
                    response.setSuccess(true);
                } else {
                    response.setMessage(ApplicationConstants.CANCEL_FAILURE);
                    response.setSuccess(false);
                }
            } else {
                response.setMessage(authDetails.getMessage());
                response.setSuccess(false);

            }
        } else {
            response.setMessage(ApplicationConstants.MSG_NO_TASK_ID);
            response.setSuccess(false);

        }
        return response;
    }

    /**
     * Export validation failure.
     *
     * @param id the id
     * @param isTaskId the is task id
     * @param session the session
     * @param request the request
     * @param response the response
     * @return the export response dto
     */
    @RequestMapping(value = "/exportValidationFailure/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ExportResponseDTO exportValidationFailure(@PathVariable String id, @RequestParam Boolean isTaskId, HttpSession session,
            HttpServletRequest request, HttpServletResponse response) {

        ExportResponseDTO exportResponse = new ExportResponseDTO();

        UserDTO user = (UserDTO) session.getAttribute(ApplicationConstants.USERBEAN);

        exportResponse = validationFailsService.exportValidationFailures(CommonUtils.convertStringToLong(id), user, isTaskId);

        CommonUtils.downloadExportFile(response, exportResponse, request);

        return exportResponse;
    }

    /**
     * Gets the import map data service impl.
     * 
     * @return the import map data service impl
     */
    public RuleInstImportExportService getImportMapDataServiceImpl() {
        return ruleInstImportExportServiceImpl;
    }

    /**
     * Sets the import map data service impl.
     * 
     * @param importMapDataServiceImpl
     *            the new import map data service impl
     */
    public void setImportMapDataServiceImpl(RuleInstImportExportService importMapDataServiceImpl) {
        this.ruleInstImportExportServiceImpl = importMapDataServiceImpl;
    }

    /**
     * Gets the map def service.
     * 
     * @return the map def service
     */
    public MapDefService getMapDefService() {
        return mapDefService;
    }

    /**
     * Sets the map def service.
     * 
     * @param mapDefService
     *            the new map def service
     */
    public void setMapDefService(MapDefService mapDefService) {
        this.mapDefService = mapDefService;
    }

    /**
     * Gets the task service.
     * 
     * @return the task service
     */
    public TaskService getTaskService() {
        return taskService;
    }

    /**
     * Sets the task service.
     * 
     * @param taskService
     *            the new task service
     */
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Gets the validation fails service.
     * 
     * @return the validation fails service
     */
    public ValidationFailsService getValidationFailsService() {
        return validationFailsService;
    }

    /**
     * Sets the validation fails service.
     * 
     * @param validationFailsService
     *            the new validation fails service
     */
    public void setValidationFailsService(ValidationFailsService validationFailsService) {
        this.validationFailsService = validationFailsService;
    }

    /**
     * Gets the my requests service.
     *
     * @return the my requests service
     */
    public MyRequestsService getMyRequestsService() {
        return myRequestsService;
    }

    /**
     * Sets the my requests service.
     *
     * @param myRequestsService the new my requests service
     */
    public void setMyRequestsService(MyRequestsService myRequestsService) {
        this.myRequestsService = myRequestsService;
    }

}
