/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.akka.exporters;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import akka.actor.UntypedActor;

import com.citi.rulefabricator.dao.mongo.impl.RuleDefChangeDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.RuleDefDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.RuleInstDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.SearchDAOMongoImpl;
import com.citi.rulefabricator.dto.ExportDTO;
import com.citi.rulefabricator.dto.RequestAuthorityDTO;
import com.citi.rulefabricator.dto.RequestDTO;
import com.citi.rulefabricator.dto.RuleAttributeDTO;
import com.citi.rulefabricator.dto.RuleDefDTO;
import com.citi.rulefabricator.dto.RuleInstDTO;
import com.citi.rulefabricator.dto.RuleInstInputAttributeDTO;
import com.citi.rulefabricator.dto.RuleInstOutputAttributeDTO;
import com.citi.rulefabricator.dto.SearchDTO;
import com.citi.rulefabricator.dto.TaskDTO.TaskStatus;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.exceptions.SystemRuntimeException;
import com.citi.rulefabricator.export.handler.excel.ExcelWriteContext;
import com.citi.rulefabricator.export.handler.excel.ExcelWriteStrategy;
import com.citi.rulefabricator.mongo.MongoUtil;
import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.services.helper.ApplicationRoleHelper;
import com.citi.rulefabricator.services.impl.RuleDefServiceImpl;
import com.citi.rulefabricator.services.impl.RuleInstServiceImpl;
import com.citi.rulefabricator.services.impl.TaskServiceImpl;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class RuleInstanceChangeReaderActor.
 */
public class RuleInstExportActor extends UntypedActor {

    /** The rule inst change mongo dao impl. */
    private RuleInstDAOMongoImpl ruleInstDAOMongoImpl;

    /** The rule def dao mongo impl. */
    private RuleDefDAOMongoImpl ruleDefDAOMongoImpl;

    /** The rule def change dao mongo impl. */
    private RuleDefChangeDAOMongoImpl ruleDefChangeDAOMongoImpl;

    /** The task service. */
    private TaskService taskService;

    /** The application context. */
    private ApplicationContext applicationContext;

    /** The mongo util. */
    private MongoUtil mongoUtil;

    /** The rownum. */
    private int rownum = 2;

    /** The download file path. */
    private String downloadFilePath;

    /** The records processed. */
    private long recordsProcessed;

    /** The rule inst service impl. */
    private RuleInstServiceImpl ruleInstServiceImpl;

    /** The search dao mongo impl. */
    private SearchDAOMongoImpl searchDAOMongoImpl;

    /** The rule def service impl. */
    private RuleDefServiceImpl ruleDefServiceImpl;

    /** The rule inst docs. */
    private List<Document> ruleInstDocs = new ArrayList<Document>();

    /** The excel write context. */
    private ExcelWriteContext excelWriteContext;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleInstExportActor.class);

    /**
     * Instantiates a new rule inst export actor.
     *
     * @param applicationContext the application context
     */
    public RuleInstExportActor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.ruleInstDAOMongoImpl = applicationContext.getBean(RuleInstDAOMongoImpl.class);
        this.taskService = applicationContext.getBean(TaskServiceImpl.class);
        this.ruleDefDAOMongoImpl = applicationContext.getBean(RuleDefDAOMongoImpl.class);
        this.mongoUtil = applicationContext.getBean(MongoUtil.class);
        this.downloadFilePath = applicationContext.getBean(ApplicationRoleHelper.class).getDownloadFilePath();
        this.ruleInstServiceImpl = applicationContext.getBean(RuleInstServiceImpl.class);
        this.ruleDefChangeDAOMongoImpl = applicationContext.getBean(RuleDefChangeDAOMongoImpl.class);
        this.searchDAOMongoImpl = applicationContext.getBean(SearchDAOMongoImpl.class);
        this.ruleDefServiceImpl = applicationContext.getBean(RuleDefServiceImpl.class);
        this.excelWriteContext = applicationContext.getBean(ExcelWriteContext.class);
    }

    /**
     * On receive.
     *
     * @param message the message
     * @throws Exception the exception
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ExportDTO) {

            ExportDTO exportDTO = (ExportDTO) message;
            try {
                Map<String, Object> params = exportDTO.getParams();
                Long mapId = exportDTO.getEntityId();
                String mapName = exportDTO.getEntityName();
                Long ruleDefId = (Long) params.get(ApplicationConstants.RULE_DEF_ID);
                Boolean newData = (Boolean) params.get(ApplicationConstants.NEW_DATA);
                exportDTO.setEntityName(mapName);

                RuleDef currentEffectiveRuleDef = findEffectiveRuleDef(exportDTO, ruleDefId);

                SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
                Sheet sheet = workbook.createSheet(mapName);
                sheet.autoSizeColumn(5);

                ExcelWriteStrategy<RuleDef> excelWriteStrategy = (ExcelWriteStrategy<RuleDef>) excelWriteContext
                        .getStrategy(ApplicationConstants.RULE_INST);
                excelWriteStrategy.prepareSheetHeader(workbook, sheet, currentEffectiveRuleDef);

                writeDataToFile(mapId, ruleDefId, newData, exportDTO, workbook, currentEffectiveRuleDef);

            } catch (SystemRuntimeException e) {
                taskService.updateTask(exportDTO.getTaskId(), TaskStatus.FAILED, "Exception: " + e.getMessage(), null, exportDTO.getUser());
                LOGGER.error("Exception writing data to file for map: " + exportDTO.getEntityId() + " with masterId: " + exportDTO.getMasterId()
                        + "and taskId:" + exportDTO.getTaskId(), e);
            } catch (Exception e) {
                taskService.updateTask(exportDTO.getTaskId(), TaskStatus.FAILED, "Exception: " + e.getMessage(), null, exportDTO.getUser());
                LOGGER.error("Exception writing data to file for map: " + exportDTO.getEntityId() + " with masterId: " + exportDTO.getMasterId()
                        + "and taskId:" + exportDTO.getTaskId(), e);
            } finally {
                getContext().stop(getSelf());
            }
        }
    }

    /**
     * Find effective rule def.
     *
     * @param exportDTO the export dto
     * @param ruleDefId the rule def id
     * @return the rule def
     */
    private RuleDef findEffectiveRuleDef(ExportDTO exportDTO, Long ruleDefId) {
        RuleDef currentEffectiveRuleDef = null;
        Document currentRuleDefDoc = null;
        if (null != ruleDefId) {

            RuleDefDTO ruleDefDTO = ruleDefServiceImpl.findOne(ruleDefId, exportDTO.getUser());
            currentEffectiveRuleDef = new RuleDef(ruleDefDTO);

        } else {
            currentRuleDefDoc = ruleDefDAOMongoImpl.getCurrentlyEffectiveRuleForMapId(exportDTO.getEntityId(), exportDTO.getUser()
                    .getUserSelectedWEFDate());
            currentEffectiveRuleDef = mongoUtil.documentToPojo(currentRuleDefDoc, RuleDef.class);
        }

        return currentEffectiveRuleDef;
    }

    /**
     * Write data to file.
     *
     * @param mapId the map id
     * @param ruleDefId the rule def id
     * @param newData the new data
     * @param exportDTO the export dto
     * @param workbook the workbook
     * @param currentEffectiveRuleDef the current effective rule def
     */
    private void writeDataToFile(Long mapId, Long ruleDefId, Boolean newData, ExportDTO exportDTO, SXSSFWorkbook workbook,
            RuleDef currentEffectiveRuleDef) {

        final int batchLimit = 1000;
        SearchDTO searchFilter = exportDTO.getSearchFilter();
        RequestDTO<RuleInstDTO> request = new RequestDTO<RuleInstDTO>();
        request.setSearch(searchFilter);
        request.setRequestId(exportDTO.getMasterId().toString());
        RequestAuthorityDTO authDetails = exportDTO.getAuthDetails();
        SearchDTO processedCriteria = ruleInstServiceImpl.getProcessedSearchCriteria(request, mapId, newData, exportDTO.getUser(), authDetails);

        Long totalRecords = searchDAOMongoImpl.getTotalRecords(processedCriteria);
        exportDTO.setTotalRecords(totalRecords);

        long numberPages = totalRecords / batchLimit;
        long remainder = totalRecords % batchLimit;
        int startIndex = 1;

        if (remainder > 0) {
            numberPages++;
        }
        if (totalRecords > batchLimit) {
            for (int index = startIndex; index <= numberPages; index++) {
                processedCriteria.getPage().setNumber(index);
                processedCriteria.getPage().setSize(batchLimit);
                processChunk(exportDTO, workbook, currentEffectiveRuleDef, authDetails, processedCriteria, newData);
            }
        } else {
            processedCriteria.getPage().setNumber(startIndex);
            processedCriteria.getPage().setSize(batchLimit);
            processChunk(exportDTO, workbook, currentEffectiveRuleDef, authDetails, processedCriteria, newData);
        }
    }

    /**
     * Process chunk.
     *
     * @param exportDTO the export dto
     * @param workbook the workbook
     * @param currentEffectiveRuleDef the current effective rule def
     * @param authDetails the auth details
     * @param prefixedCriteria the prefixed criteria
     * @param newData the new data
     * @throws SystemRuntimeException the system runtime exception
     */
    private void processChunk(ExportDTO exportDTO, SXSSFWorkbook workbook, RuleDef currentEffectiveRuleDef, RequestAuthorityDTO authDetails,
            SearchDTO prefixedCriteria, Boolean newData) throws SystemRuntimeException {
        List<RuleInstDTO> ruleInsts = null;
        if (newData) {
            ruleInsts = ruleInstServiceImpl.searchForRuleInstInChangeCollection(prefixedCriteria);
        } else {
            ruleInsts = ruleInstServiceImpl.searchForRuleInstInPrimaryCollection(prefixedCriteria, exportDTO.getMasterId(), exportDTO.getUser(),
                    authDetails.getPageMode());
        }

        if (ruleInsts != null) {
            taskService.incrementRecordsProcessedBy(exportDTO.getTaskId(), (long) ruleInsts.size());
            forwardWrites(ruleInsts, exportDTO, workbook, currentEffectiveRuleDef);
        }
    }

    /**
     * Forward writes.
     *
     * @param ruleInstList the rule inst list
     * @param exportDTO the export dto
     * @param workbook the workbook
     * @param currentEffectiveRuleDef the current effective rule def
     * @throws SystemRuntimeException the system runtime exception
     */
    private void forwardWrites(List<RuleInstDTO> ruleInstList, ExportDTO exportDTO, SXSSFWorkbook workbook, RuleDef currentEffectiveRuleDef)
            throws SystemRuntimeException {
        recordsProcessed += ruleInstList.size();
        FileOutputStream out = null;

        try {
            prepareSheetData(workbook, workbook.getSheet(exportDTO.getEntityName()), ruleInstList, currentEffectiveRuleDef);

            if (recordsProcessed == exportDTO.getTotalRecords().longValue()) {
                DateFormat dateFormat = new SimpleDateFormat(CommonUtils.FILE_DATETIME_FORMAT_YYYY_MM_DD);
                Date date = CommonUtils.getDefaultNewDate();
                String timeStr = dateFormat.format(date);

                File file = new File(downloadFilePath);

                if (!file.exists()) {
                    file.mkdir();
                }

                file = new File(downloadFilePath + ApplicationConstants.EXPORT_FILE_PREFIX + exportDTO.getEntityName()
                        + ApplicationConstants.UNDERSCORECHAR + exportDTO.getUser().getSoeId() + ApplicationConstants.UNDERSCORECHAR + timeStr
                        + ApplicationConstants.EXCELEXTN);

                out = new FileOutputStream(file.getAbsolutePath());
                workbook.write(out);
                taskService.updateTask(exportDTO.getTaskId(), TaskStatus.COMPLETED, "Export Completed ", file.getAbsolutePath(), exportDTO.getUser());
            }

        } catch (Exception e) {
            LOGGER.error("RuleInstExportActor::forwardWrites - Error Exception ", e);
            throw new SystemRuntimeException("ruleinst.export.write.error", e.getMessage(), e.getStackTrace(), exportDTO.getEntityId(),
                    exportDTO.getMasterId());
        } finally {
            CommonUtils.closeResource(out);
        }
    }

    /**
     * Prepare sheet data.
     *
     * @param workbook the workbook
     * @param sheet the sheet
     * @param ruleInstList the rule inst list
     * @param ruleDef the rule def
     * @throws Exception the exception
     */
    private void prepareSheetData(SXSSFWorkbook workbook, Sheet sheet, List<RuleInstDTO> ruleInstList, RuleDef ruleDef) throws Exception {
        int cellnum = 0;
        CellStyle cellStyle = workbook.createCellStyle();
        String attrName;
        RuleInstInputAttributeDTO ruleInstInputAttributeDTO = null;
        RuleInstOutputAttributeDTO ruleInstOutputAttributeDTO = null;
        Cell cell = null;
        Row row = null;
        if (ruleInstList != null) {
            for (RuleInstDTO ruleInst : ruleInstList) {
                row = sheet.createRow(rownum++);

                cell = row.createCell(cellnum++);
                cell.setCellValue(ruleInst.getSeq().toString());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(cellnum++);
                SimpleDateFormat format = new SimpleDateFormat(CommonUtils.DEFAULT_DATE_FORMAT);
                cell.setCellValue(format.format(ruleInst.getEffStartDate()));
                cell.setCellStyle(cellStyle);

                cell = row.createCell(cellnum++);
                cell.setCellValue(ruleInst.getPriority());
                cell.setCellStyle(cellStyle);

                List<RuleAttributeDTO> sortedInputAttrr = ruleDef.getInput();
                Collections.sort(sortedInputAttrr, new RuleAttributeDTO());

                for (RuleAttributeDTO ruleDefAttr : sortedInputAttrr) {
                    attrName = ruleDefAttr.getName();

                    ruleInstInputAttributeDTO = ruleInstContainsInputAttribute(attrName, ruleInst);

                    if (ruleInstInputAttributeDTO != null) {
                        cell = row.createCell(cellnum++);
                        cell.setCellValue(ruleInstInputAttributeDTO.getValue());
                        cell.setCellStyle(cellStyle);

                        if (!ruleDefAttr.getAnchorFlag()) {
                            cell = row.createCell(cellnum++);
                            cell.setCellValue(ruleInstInputAttributeDTO.getDimensionFlag());
                            cell.setCellStyle(cellStyle);

                            cell = row.createCell(cellnum++);
                            cell.setCellValue(ruleInstInputAttributeDTO.getTreeId() != null ? ruleInstInputAttributeDTO.getTreeId() : "");
                            cell.setCellStyle(cellStyle);
                        }
                    } else {
                        cell = row.createCell(cellnum++);
                        cell.setCellValue("");
                        cell.setCellStyle(cellStyle);

                        if (!ruleDefAttr.getAnchorFlag()) {
                            cell = row.createCell(cellnum++);
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle);

                            cell = row.createCell(cellnum++);
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle);
                        }
                    }

                }

                List<RuleAttributeDTO> sortedOutputAttrr = ruleDef.getOutput();
                Collections.sort(sortedOutputAttrr, new RuleAttributeDTO());

                for (RuleAttributeDTO ruleDefAttr : sortedOutputAttrr) {
                    attrName = ruleDefAttr.getName();

                    ruleInstOutputAttributeDTO = ruleInstContainsOutputAttribute(attrName, ruleInst);
                    if (ruleInstOutputAttributeDTO != null) {
                        cell = row.createCell(cellnum++);
                        cell.setCellValue(ruleInstOutputAttributeDTO.getValue());
                        cell.setCellStyle(cellStyle);
                    }
                }
                cellnum = 0;
            }

        }
    }

    /**
     * Rule inst contains input attribute.
     *
     * @param attrName the attr name
     * @param ruleInst the rule inst
     * @return the rule inst input attribute dto
     */
    private RuleInstInputAttributeDTO ruleInstContainsInputAttribute(String attrName, RuleInstDTO ruleInst) {
        RuleInstInputAttributeDTO returnValue = null;

        if (!CommonUtils.isBlank(attrName)) {
            for (RuleInstInputAttributeDTO ruleInstInputAttributeDTO : ruleInst.getInput()) {
                if (ruleInstInputAttributeDTO.getName().equals(attrName)) {
                    returnValue = ruleInstInputAttributeDTO;
                    break;
                }
            }
        }

        return returnValue;
    }

    /**
     * Rule inst contains output attribute.
     *
     * @param attrName the attr name
     * @param ruleInst the rule inst
     * @return the rule inst output attribute dto
     */
    private RuleInstOutputAttributeDTO ruleInstContainsOutputAttribute(String attrName, RuleInstDTO ruleInst) {
        RuleInstOutputAttributeDTO returnValue = null;

        if (!CommonUtils.isBlank(attrName)) {
            for (RuleInstOutputAttributeDTO ruleInstOutputAttributeDTO : ruleInst.getOutput()) {
                if (ruleInstOutputAttributeDTO.getName().equals(attrName)) {
                    returnValue = ruleInstOutputAttributeDTO;
                    break;
                }
            }
        }
        return returnValue;
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
     * @param taskService the new task service
     */
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
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
     * Sets the application context.
     *
     * @param applicationContext the new application context
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
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
     * Gets the records processed.
     *
     * @return the records processed
     */
    public long getRecordsProcessed() {
        return recordsProcessed;
    }

    /**
     * Sets the records processed.
     *
     * @param recordsProcessed the new records processed
     */
    public void setRecordsProcessed(long recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    /**
     * Gets the rule inst docs.
     *
     * @return the rule inst docs
     */
    public List<Document> getRuleInstDocs() {
        return ruleInstDocs;
    }

    /**
     * Sets the rule inst docs.
     *
     * @param ruleInstDocs the new rule inst docs
     */
    public void setRuleInstDocs(List<Document> ruleInstDocs) {
        this.ruleInstDocs = ruleInstDocs;
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
     * Gets the rownum.
     *
     * @return the rownum
     */
    public int getRownum() {
        return rownum;
    }

    /**
     * Sets the rownum.
     *
     * @param rownum the new rownum
     */
    public void setRownum(int rownum) {
        this.rownum = rownum;
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

}
