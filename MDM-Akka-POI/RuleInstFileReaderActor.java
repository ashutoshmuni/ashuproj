/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.akka.importers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.citi.rulefabricator.akka.merge.RuleInstanceChangeReaderActor;
import com.citi.rulefabricator.akka.messages.EndOfFileMsg;
import com.citi.rulefabricator.akka.messages.ImportTaskCompleteMsg;
import com.citi.rulefabricator.dao.dos.AttributeValueDO;
import com.citi.rulefabricator.dto.FileImportChunkDTO;
import com.citi.rulefabricator.dto.ImportDTO;
import com.citi.rulefabricator.dto.RuleAttributeDTO;
import com.citi.rulefabricator.dto.TaskDTO;
import com.citi.rulefabricator.dto.TaskDTO.TaskStatus;
import com.citi.rulefabricator.dto.UserDTO;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.exceptions.ApplicationException;
import com.citi.rulefabricator.poi.ExcelReader;
import com.citi.rulefabricator.poi.ExcelRowContentCallBack;
import com.citi.rulefabricator.poi.ExcelWorkSheetContentCallbackHandler;
import com.citi.rulefabricator.processors.RuleInstChangeProcessor;
import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.services.impl.RuleInstImportExportServiceImpl;
import com.citi.rulefabricator.services.impl.TaskServiceImpl;
import com.citi.rulefabricator.util.AttributeDisplayOrderComparator;
import com.citi.rulefabricator.util.AttributeValidator;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class RuleInstFileReaderActor.
 */
public class RuleInstFileReaderActor extends UntypedActor {

    /** The mongo insertion actor. */
    private ActorRef mongoInsertionActor;

    /** The task service. */
    private TaskService taskService;

    /** The mandatory columns. */
    private static final String[] MANDATORYCOLUMNS = { "Sequence Number", "Start Date", "Priority" };

    /** The input attrs fileds. */
    private static final String[] INPUTATTRFIELDS = { "Value", "Is Dimension", "Tree ID" };

    /** The task id. */
    private Long taskId;

    /** The Constant BATCH_SIZE. */
    private static final int BATCH_SIZE = 1000;

    /** The row data list. */
    private List<Map<String, List<String>>> rowDataList;

    /** The validation value source. */
    private Map<String, Map<String, List<AttributeValueDO>>> validationValueSource;

    /** The no of write requests. */
    private long noOfWriteRequests = 0;

    /** The no of write responses. */
    private long noOfWriteResponses = 0;

    /** The final row no. */
    private int finalRowNo;

    /** The is valid sheet. */
    private boolean isValidSheet = true;

    /** The import file path. */
    private String importFilePath;

    /** The rule inst change processor. */
    private RuleInstChangeProcessor ruleInstChangeProcessor;

    /** The attribute validator. */
    private AttributeValidator attributeValidator;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleInstanceChangeReaderActor.class);

    /**
     * Instantiates a new rule inst file reader actor.
     *
     * @param mongoInsertionActor
     *            the mongo insertion actor
     * @param applicationContext
     *            the application context
     */
    public RuleInstFileReaderActor(ActorRef mongoInsertionActor, ApplicationContext applicationContext) {
        this.mongoInsertionActor = mongoInsertionActor;
        this.taskService = applicationContext.getBean(TaskServiceImpl.class);
        this.importFilePath = applicationContext.getBean(RuleInstImportExportServiceImpl.class).getImportFilePath();
        this.ruleInstChangeProcessor = applicationContext.getBean(RuleInstChangeProcessor.class);
        this.attributeValidator = applicationContext.getBean(AttributeValidator.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    /**
     * On receive.
     *
     * @param message
     *            the message
     * @throws Exception
     *             the exception
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ImportDTO) {
            ImportDTO<RuleDef> mapDataImportDTO = (ImportDTO<RuleDef>) message;
            processImport(mapDataImportDTO);
        } else if (message instanceof ImportTaskCompleteMsg) {
            ImportTaskCompleteMsg importCompleteMsg = (ImportTaskCompleteMsg) message;
            TaskDTO taskDTO = taskService.findOne(taskId);
            noOfWriteResponses++;
            if (noOfWriteRequests == noOfWriteResponses) {
                taskService.updateTask(taskId, TaskStatus.COMPLETED, "Import Completed", taskDTO.getFilePath(), importCompleteMsg.getUser());
                getContext().stop(getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * Process.
     *
     * @param mapDataImportDTO
     *            the map data import dto
     * @return true, if successful the MDM base exception
     */
    public void processImport(final ImportDTO<RuleDef> mapDataImportDTO) {

        taskId = mapDataImportDTO.getTaskId();

        LOGGER.info("Import Data File read started for masterChangeRequestId : {} and taskId: {}", mapDataImportDTO.getMasterChangeRequestId(),
                taskId);

        // Fetch the existing task
        TaskDTO taskDTO = taskService.findOne(taskId);

        importFilePath = taskDTO.getFilePath();

        // Update the status to IN_PROGRESS
        taskService.updateTask(taskId, TaskStatus.IN_PROGRESS, ApplicationConstants.IN_PROGRESS, importFilePath, mapDataImportDTO.getUser());

        rowDataList = new ArrayList<Map<String, List<String>>>();

        ExcelWorkSheetContentCallbackHandler sheetRowCallbackHandler = new ExcelWorkSheetContentCallbackHandler(new ExcelRowContentCallBack() {
            private long noOfRowsProcessed = 0;

            @Override
            public void processRow(int rowNum, Map<String, List<String>> columnValueMap) throws Exception {
                finalRowNo = rowNum;
                try {
                    if (rowNum == 1) {
                        validationValueSource = validateSheetHeader(mapDataImportDTO, columnValueMap);
                    } else {
                        if (isValidSheet && !isEmptyRow(columnValueMap)) {
                            columnValueMap.put(ApplicationConstants.ROWNUM, Arrays.asList(Integer.toString(rowNum + 1)));

                            rowDataList.add(columnValueMap);
                            noOfRowsProcessed++;
                            if (noOfRowsProcessed % BATCH_SIZE == 0) {
                                LOGGER.debug("Sending batch " + BATCH_SIZE + " : Till now: " + (rowNum));
                                FileImportChunkDTO chunkDTO = new FileImportChunkDTO();
                                chunkDTO.setMasterChangeRequestId(mapDataImportDTO.getMasterChangeRequestId());
                                chunkDTO.setRuleDef(mapDataImportDTO.getEntity());
                                chunkDTO.setUser(mapDataImportDTO.getUser());
                                chunkDTO.setTaskId(mapDataImportDTO.getTaskId());
                                chunkDTO.setListOfExcelDataMaps(new ArrayList<Map<String, List<String>>>(rowDataList));
                                chunkDTO.setValidationValueSource(validationValueSource);
                                rowDataList.clear();
                                mongoInsertionActor.tell(chunkDTO, getSelf());
                                noOfWriteRequests++;
                                rowDataList = new ArrayList<Map<String, List<String>>>();
                            }
                        }
                    }

                } catch (ApplicationException mdmEx) {
                    isValidSheet = false;
                    taskService.updateTask(taskId, TaskStatus.FAILED, mdmEx.getMessage(), importFilePath, mapDataImportDTO.getUser());
                    LOGGER.error("RuleInstFileReaderActor::process - Error Exception ", mdmEx);

                } catch (Exception e) {
                    isValidSheet = false;
                    taskService.updateTask(taskId, TaskStatus.FAILED, "Error Occurred!" + e.getMessage(), importFilePath, mapDataImportDTO.getUser());
                    LOGGER.error("RuleInstFileReaderActor::process - Error Exception ", e);
                }
            }
        });

        ExcelReader excelReader;
        try {
            excelReader = new ExcelReader(taskDTO.getFilePath(), sheetRowCallbackHandler, null);
            excelReader.process();
            if (isValidSheet) {
                if (!rowDataList.isEmpty()) {
                    LOGGER.debug("Sending remaining batch " + BATCH_SIZE + " : Till now: " + (rowDataList.get(0).get(ApplicationConstants.ROWNUM)));
                    FileImportChunkDTO chunkDTO = new FileImportChunkDTO();
                    chunkDTO.setMasterChangeRequestId(mapDataImportDTO.getMasterChangeRequestId());
                    chunkDTO.setRuleDef(mapDataImportDTO.getEntity());
                    chunkDTO.setUser(mapDataImportDTO.getUser());
                    chunkDTO.setListOfExcelDataMaps(rowDataList);
                    chunkDTO.setValidationValueSource(validationValueSource);
                    chunkDTO.setTaskId(taskId);
                    mongoInsertionActor.tell(chunkDTO, getSelf());
                    noOfWriteRequests++;
                }
                LOGGER.debug("Total number of rows read: " + finalRowNo);
                LOGGER.info("Import Data File read completed for masterChangeRequestId : {} and taskId: {}, Data sent in chunks for insertion.",
                        mapDataImportDTO.getMasterChangeRequestId(), taskId);
            }
        } catch (ApplicationException e) {
            isValidSheet = false;
            taskService.updateTask(taskId, TaskStatus.FAILED, e.getMessage(), importFilePath, mapDataImportDTO.getUser());
            LOGGER.error("RuleInstFileReaderActor::process - Error Exception ", e);

        } catch (Exception e) {
            isValidSheet = false;
            taskService.updateTask(taskId, TaskStatus.FAILED, e.getMessage(), importFilePath, mapDataImportDTO.getUser());
            LOGGER.error("RuleInstFileReaderActor::process - Error Exception ", e);

        }
        mongoInsertionActor.tell(new EndOfFileMsg(), getSelf());

    }

    /**
     * Validate sheet header.
     *
     * <<<<<<< .mine <<<<<<< .mine
     * 
     * @param mapDataImportDTO
     *            the map data import dto
     * @param map
     *            the map
     * @return the map
     * @throws ApplicationException
     *             =======
     * @param mapDataImportDTO
     *            the map data import dto
     * @param map
     *            the map
     * @return the map
     * @throws ApplicationException
     *             the application exception >>>>>>> .r5796 the application
     *             exception =======
     * @param mapDataImportDTO
     *            the map data import dto
     * @param map
     *            the map
     * @throws ApplicationException
     *             >>>>>>> .r5598
     */

    private Map<String, Map<String, List<AttributeValueDO>>> validateSheetHeader(ImportDTO<RuleDef> mapDataImportDTO, Map<String, List<String>> map)
            throws ApplicationException {

        Map<String, Map<String, List<AttributeValueDO>>> returnSource = new HashMap<String, Map<String, List<AttributeValueDO>>>();
        List<String> columnList = new ArrayList<String>();
        Set<String> headerColumns = map.keySet();
        columnList.addAll(headerColumns);
        RuleDef ruleDef = mapDataImportDTO.getEntity();
        UserDTO user = mapDataImportDTO.getUser();

        // Validate the mandatory columns first
        validateMandatoryColumns(headerColumns);

        // Validate other column headers as per Rule information
        validateAttributeColumns(columnList, ruleDef);

        validateInputAttributes(map, returnSource, headerColumns, ruleDef);

        validateOutputAttributes(returnSource, headerColumns, ruleDef);

        if (isValidSheet && user.isAdmin()) {
            mapDataImportDTO.setMasterChangeRequestId(ruleInstChangeProcessor.saveMasterChangeRequestWithApproveStatus(user));
        }
        return returnSource;
    }

    /**
     * Validate output attributes.
     *
     * @param returnSource
     *            the return source
     * @param headerColumns
     *            the header columns
     * @param ruleDef
     *            the rule def
     * @throws ApplicationException
     *             the application exception
     */
    private void validateOutputAttributes(Map<String, Map<String, List<AttributeValueDO>>> returnSource, Set<String> headerColumns, RuleDef ruleDef)
            throws ApplicationException {
        // Sort output columns according to display order before matching
        // headers
        Collections.sort(ruleDef.getOutput(), new AttributeDisplayOrderComparator());
        for (RuleAttributeDTO outputRuleAttribute : ruleDef.getOutput()) {
            if (headerColumns == null || headerColumns.size() <= (MANDATORYCOLUMNS.length + ruleDef.getInput().size())) {
                throw new ApplicationException("ruleinst.import.outputAttrs.headers.missing", ruleDef.getMapDefId());
            } else {
                if (!headerColumns.contains(outputRuleAttribute.getName())) {
                    throw new ApplicationException("ruleinst.import.attribute.column.missing", outputRuleAttribute.getName());
                }
            }
        }

        for (RuleAttributeDTO outputRuleAttribute : ruleDef.getOutput()) {
            String attrName = outputRuleAttribute.getName();
            Map<String, List<AttributeValueDO>> list = attributeValidator.getAtrributeValuesFromCache(attrName);
            if (list != null) {
                returnSource.put(attrName, list);
            }
        }
    }

    /**
     * Validate input attributes.
     *
     * @param map
     *            the map
     * @param returnSource
     *            the return source
     * @param headerColumns
     *            the header columns
     * @param ruleDef
     *            the rule def
     * @throws ApplicationException
     *             the application exception
     */
    private void validateInputAttributes(Map<String, List<String>> map, Map<String, Map<String, List<AttributeValueDO>>> returnSource,
            Set<String> headerColumns, RuleDef ruleDef) throws ApplicationException {
        // Sort input columns according to display order before matching headers
        Collections.sort(ruleDef.getInput(), new AttributeDisplayOrderComparator());
        StringBuilder subAttrMissing = null;
        for (RuleAttributeDTO inputRuleAttribute : ruleDef.getInput()) {
            if (headerColumns == null || headerColumns.size() <= MANDATORYCOLUMNS.length) {
                throw new ApplicationException("ruleinst.import.inputAttrs.headers.missing", ruleDef.getMapDefId());
            } else {
                if (!headerColumns.contains(inputRuleAttribute.getName())) {
                    throw new ApplicationException("ruleinst.import.attribute.column.missing", inputRuleAttribute.getName());
                } else {
                    subAttrMissing = validateSubAttributes(map, subAttrMissing, inputRuleAttribute);
                }
            }
        }

        if (subAttrMissing != null) {
            throw new ApplicationException("ruleinst.import.attribute.column.subattribute.missing", subAttrMissing.toString());
        } else {
            getAttrValuesFromCache(returnSource, ruleDef);
        }
    }

    /**
     * Gets the attr values from cache.
     *
     * @param returnSource
     *            the return source
     * @param ruleDef
     *            the rule def
     * @return the attr values from cache
     */
    private void getAttrValuesFromCache(Map<String, Map<String, List<AttributeValueDO>>> returnSource, RuleDef ruleDef) {
        for (RuleAttributeDTO inputRuleAttribute : ruleDef.getInput()) {
            String attrName = inputRuleAttribute.getName();
            final Map<String, List<AttributeValueDO>> mapslist = attributeValidator.getAtrributeValuesFromCache(attrName);
            if (mapslist != null) {
                returnSource.put(attrName, mapslist);
            }
        }
    }

    // This method validates if the subattributes(Value, IsDimension, TreeId)
    // are correctly available in the sheet
    /**
     * Validate sub attributes.
     *
     * @param map
     *            the map
     * @param subAttrMissing
     *            the sub attr missing
     * @param inputRuleAttribute
     *            the input rule attribute
     * @return the string builder
     */
    private StringBuilder validateSubAttributes(Map<String, List<String>> map, StringBuilder subAttrMissing, RuleAttributeDTO inputRuleAttribute) {
        List<String> subAttrs = map.get(inputRuleAttribute.getName());
        StringBuilder builder = null;

        if (inputRuleAttribute.getAnchorFlag()) {
            if (!subAttrs.contains(INPUTATTRFIELDS[0])) {
                builder = new StringBuilder();
                builder.append(INPUTATTRFIELDS[0]);
                builder.append(" column having anchor Tag is missing for input attribute");
                builder.append(inputRuleAttribute.getName());
                builder.append(". ");
                if (subAttrMissing == null) {
                    subAttrMissing = new StringBuilder();
                }
                subAttrMissing.append(builder.toString());
            }
        } else {
            for (int i = 0; i < INPUTATTRFIELDS.length; i++) {
                if (!subAttrs.contains(INPUTATTRFIELDS[i])) {
                    if (subAttrMissing == null) {
                        subAttrMissing = new StringBuilder();
                    }
                    builder = new StringBuilder();
                    builder.append(INPUTATTRFIELDS[i]);
                    builder.append(" column is missing for input attribute ");
                    builder.append(inputRuleAttribute.getName());
                    builder.append(". ");
                    subAttrMissing.append(builder.toString());
                }
            }
        }
        return subAttrMissing;
    }

    /**
     * Validate attribute columns.
     *
     * @param columnList
     *            the column list
     * @param ruleDef
     *            the rule def
     * @throws ApplicationException
     *             the application exception
     */
    private void validateAttributeColumns(List<String> columnList, RuleDef ruleDef) throws ApplicationException {
        List<RuleAttributeDTO> inputAttrs = ruleDef.getInput();
        List<RuleAttributeDTO> outputAttrs = ruleDef.getOutput();
        if ((inputAttrs.size() + outputAttrs.size()) != (columnList.size() - MANDATORYCOLUMNS.length)) {
            throw new ApplicationException("ruleinst.import.validation.attributes.column.mismatch", ruleDef.getMapDefId());
        }
    }

    /**
     * Validate mandatory columns.
     *
     * @param headerColumns
     *            the header columns
     * @throws ApplicationException
     *             the application exception
     */
    private void validateMandatoryColumns(Set<String> headerColumns) throws ApplicationException {
        for (int i = 0; i < MANDATORYCOLUMNS.length; i++) {
            String mandatoryColumnName = MANDATORYCOLUMNS[i];
            if (!headerColumns.contains(mandatoryColumnName)) {
                throw new ApplicationException("ruleinst.import.mandatory.column.missing", mandatoryColumnName.toUpperCase());
            }
        }
    }

    /**
     * Checks if is empty row.
     *
     * @param columnValueMap
     *            the columnValueMap
     * @return true, if is empty row
     */
    private boolean isEmptyRow(Map<String, List<String>> columnValueMap) {
        boolean returnVal = true;

        for (Map.Entry<String,List<String>> header : columnValueMap.entrySet()) {
        	List<String> values = header.getValue();
            for (String value : values) {
                if (!CommonUtils.isBlank(value)) {
                    returnVal = false;
                    break;
                }
            }

            if (!returnVal) {
                break;
            }
        }
        return returnVal;
    }
}
