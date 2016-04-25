/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.akka.importers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import akka.actor.UntypedActor;

import com.citi.rulefabricator.akka.messages.ImportTaskCompleteMsg;
import com.citi.rulefabricator.dao.mongo.impl.RuleInstChangeDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.RuleInstDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.SequenceDAOMongoImpl;
import com.citi.rulefabricator.dto.AttributeFailureDTO;
import com.citi.rulefabricator.dto.FileImportChunkDTO;
import com.citi.rulefabricator.dto.RuleAttributeDTO;
import com.citi.rulefabricator.dto.RuleInstDTO;
import com.citi.rulefabricator.dto.RuleInstInputAttributeDTO;
import com.citi.rulefabricator.dto.RuleInstOutputAttributeDTO;
import com.citi.rulefabricator.dto.ValidationFailsDTO;
import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.enums.Sequence;
import com.citi.rulefabricator.processors.RuleInstChangeProcessor;
import com.citi.rulefabricator.services.TaskService;
import com.citi.rulefabricator.services.entities.RuleDef;
import com.citi.rulefabricator.services.impl.TaskServiceImpl;
import com.citi.rulefabricator.util.AttributeValidator;
import com.citi.rulefabricator.util.CommonUtils;

/**
 * The Class RuleInstMongoInsertionActor.
 */
public class RuleInstMongoInsertionActor extends UntypedActor {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = LoggerFactory.getLogger(RuleInstMongoInsertionActor.class);

  /** The application context. */
  private ApplicationContext applicationContext;

  /** The rule inst change processor. */
//  private RuleInstChangeProcessor ruleInstChangeProcessor;
//
//  private RuleInstDAOMongoImpl ruleInstDAOMongoImpl;
//
//  private RuleInstChangeDAOMongoImpl ruleInstChangeDAOMongoImpl;

  /** The task service. */
//  private TaskService taskService;

  /** The attribute validator. */
//  private AttributeValidator attributeValidator;

  /** The sequence mongo dao impl. */
//  private SequenceDAOMongoImpl sequenceMongoDAOImpl;

  /**
   * Instantiates a new rule inst mongo insertion actor.
   *
   * @param contextParam
   *          the context param
   */
  public RuleInstMongoInsertionActor(ApplicationContext contextParam) {
    this.applicationContext = contextParam;
//    this.ruleInstChangeProcessor = applicationContext.getBean(RuleInstChangeProcessor.class);
//    this.taskService = applicationContext.getBean(TaskServiceImpl.class);
//    this.attributeValidator = applicationContext.getBean(AttributeValidator.class);
//    this.sequenceMongoDAOImpl = applicationContext.getBean(SequenceDAOMongoImpl.class);
//    this.ruleInstDAOMongoImpl = applicationContext.getBean(RuleInstDAOMongoImpl.class);
//    this.ruleInstChangeDAOMongoImpl = applicationContext.getBean(RuleInstChangeDAOMongoImpl.class);
  }

  /**
   * On receive.
   *
   * @param message
   *          the message
   * @throws Exception
   *           the exception
   * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
   */
  @Override
  public void onReceive(Object message) throws Exception {

    List<RuleInstDTO> ruleInstDTOs = null;
    if (message instanceof FileImportChunkDTO) {

      long startMilliSec = System.currentTimeMillis();
      FileImportChunkDTO fileImportChunkDTO = (FileImportChunkDTO) message;
      LOGGER.info("Rule Inst Data Insertion started for a chunk of maxsize 2000 masterChangeRequestId : {} and taskId: {}",
          fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getTaskId());

      ruleInstDTOs = new ArrayList<RuleInstDTO>();
      List<Map<String, List<String>>> listOfExcelMaps = fileImportChunkDTO.getListOfExcelDataMaps();
      if (null != listOfExcelMaps) {
        Long noOfRecordsProcessed = new Long(listOfExcelMaps.size());
        LOGGER.debug("Actor received " + listOfExcelMaps.size());
        LOGGER.info("Rule Inst Data validation started for a chunk of maxsize 2000 masterChangeRequestId : {} and taskId: {}",
            fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getTaskId());
        long validationStartMilliSec = System.currentTimeMillis();
        for (Map<String, List<String>> map : listOfExcelMaps) {
          processSingleExcelRow(fileImportChunkDTO, map, ruleInstDTOs);
        }
        long validationendMilliSec = System.currentTimeMillis();
        long validationDiff = validationendMilliSec - validationStartMilliSec;
        LOGGER.info("Rule Inst Data validation completed for a chunk of maxsize 2000 masterChangeRequestId : {} and taskId: {} in time :{} millisec",
            new Object[] { fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getTaskId(), validationDiff });

//        if (!ruleInstDTOs.isEmpty()) {
//          ruleInstChangeProcessor.handleBulk(fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getTaskId(), ruleInstDTOs,
//              fileImportChunkDTO.getUser());
//        }

        ImportTaskCompleteMsg taskCompleteMsg = new ImportTaskCompleteMsg();
        taskCompleteMsg.setTaskId(fileImportChunkDTO.getTaskId());
        taskCompleteMsg.setUser(fileImportChunkDTO.getUser());
        taskCompleteMsg.setNoOfRecordsProcessed(noOfRecordsProcessed);
//        taskService.incrementRecordsProcessedBy(taskCompleteMsg.getTaskId(), noOfRecordsProcessed);
        long endMilliSec = System.currentTimeMillis();
        long totalTime = endMilliSec - startMilliSec;
        LOGGER.info(
            "Rule Inst Data Insertion completed for a chunk of maxsize 2000 with masterChangeRequestId : {} and taskId: {} in time : {} millisec",
            new Object[] { fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getTaskId(), totalTime });

        getSender().tell(taskCompleteMsg, getSelf());
      }
    }

  }

  /**
   * Sets the application context.
   *
   * @param applicationContext
   *          the applicationContext to set
   */
  public final void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Process single excel row.
   *
   * @param fileImportChunkDTO
   *          the file import chunk dto
   * @param map
   *          the map
   * @param ruleInstList
   *          the rule inst list
   */
  private void processSingleExcelRow(FileImportChunkDTO fileImportChunkDTO, Map<String, List<String>> map, List<RuleInstDTO> ruleInstList) {
    int rowNum = Integer.valueOf(map.get("_rowNum").get(0));
    RuleDef ruleDef = fileImportChunkDTO.getRuleDef();
    RuleInstDTO ruleInstDTO = new RuleInstDTO();
    ruleInstDTO.setMapDefId(ruleDef.getMapDefId());
    ValidationFailsDTO validationFailsDTO = new ValidationFailsDTO();
    validationFailsDTO.setFailures(new ArrayList<AttributeFailureDTO>());

    processSequence(map, fileImportChunkDTO.getMasterChangeRequestId(), ruleInstDTO, validationFailsDTO);
    processStartDate(map, ruleInstDTO);
    processPriority(map, ruleInstDTO);

    // Now populate Input attributes
    processInputAttr(map, ruleDef, ruleInstDTO);

    // Now populate output attributes
    processOutputAttr(map, ruleDef, ruleInstDTO);

//    ruleInstDTO.setIdentifier(RuleInstChangeProcessor.generateChangeSummaryIdentifier(ruleDef.getMapDefId(), ruleDef.getMapName(),
//        ruleInstDTO.getEffStartDate(), ruleInstDTO.getSeq()));

    /*
     * attributeValidator.validateRuleInst(ruleInstDTO, fileImportChunkDTO.getTaskId(), fileImportChunkDTO.getUser(), rowNum,
     * fileImportChunkDTO.getMasterChangeRequestId(), fileImportChunkDTO.getValidationValueSource());
     */
//    attributeValidator.validateAllInpOptAttrs(ruleInstDTO, fileImportChunkDTO.getTaskId(), rowNum, fileImportChunkDTO.getValidationValueSource(),
//        validationFailsDTO);
//    attributeValidator.saveValidationFailures(ruleInstDTO, fileImportChunkDTO.getTaskId(), fileImportChunkDTO.getUser(), rowNum,
//        fileImportChunkDTO.getMasterChangeRequestId(), validationFailsDTO);
    ruleInstList.add(ruleInstDTO);
  }

  /**
   * Process output attr.
   *
   * @param map
   *          the map
   * @param ruleDef
   *          the rule def
   * @param ruleInstDTO
   *          the rule inst dto
   */
  private void processOutputAttr(Map<String, List<String>> map, RuleDef ruleDef, RuleInstDTO ruleInstDTO) {
    List<RuleInstOutputAttributeDTO> outputDataAttrs = new ArrayList<RuleInstOutputAttributeDTO>();
    RuleInstOutputAttributeDTO outputAttr = null;

    for (RuleAttributeDTO ruleRef : ruleDef.getOutput()) {

      outputAttr = new RuleInstOutputAttributeDTO();

      outputAttr.setName(ruleRef.getName());
      outputAttr.setValue(map.get(ruleRef.getName()).get(0));
      outputAttr.set_id(ruleRef.get_id());
      outputDataAttrs.add(outputAttr);

    }
    ruleInstDTO.setOutput(outputDataAttrs);
  }

  /**
   * Process input attr.
   *
   * @param map
   *          the map
   * @param ruleDef
   *          the rule def
   * @param ruleInstDTO
   *          the rule inst dto
   */
  private void processInputAttr(Map<String, List<String>> map, RuleDef ruleDef, RuleInstDTO ruleInstDTO) {
    List<RuleInstInputAttributeDTO> inputDataAttrs = new ArrayList<RuleInstInputAttributeDTO>();
    RuleInstInputAttributeDTO inputAttr = null;
    List<String> tempList = null;

    for (RuleAttributeDTO ruleRef : ruleDef.getInput()) {

      inputAttr = new RuleInstInputAttributeDTO();

      inputAttr.setName(ruleRef.getName());
      inputAttr.set_id(ruleRef.get_id());
      inputAttr.setDisplayOrder(Double.valueOf(ruleRef.getDisplayOrder()).intValue());
      inputAttr.setAnchor(ruleRef.getAnchorFlag());
      tempList = map.get(ruleRef.getName());

      if (tempList.get(0) != null && !CommonUtils.isBlank(tempList.get(0))) {
        inputAttr.setValue(tempList.get(0));
      }

      if (tempList.size() > 1 && tempList.get(1) != null && !CommonUtils.isBlank(tempList.get(1))) {
        inputAttr.setDimensionFlag(tempList.get(1));
      }

      if (tempList.size() >= 2 && tempList.get(2) != null && !CommonUtils.isBlank(tempList.get(2))) {
        inputAttr.setTreeId(tempList.get(2));
      }

      inputDataAttrs.add(inputAttr);
    }
    ruleInstDTO.setInput(inputDataAttrs);
  }

  /**
   * Process priority.
   *
   * @param map
   *          the map
   * @param ruleInstDTO
   *          the rule inst dto
   */
  private void processPriority(Map<String, List<String>> map, RuleInstDTO ruleInstDTO) {
    String temp;
    if (map.get(ApplicationConstants.IMPORT_HEADER_PRIORITY) != null && !map.get(ApplicationConstants.IMPORT_HEADER_PRIORITY).isEmpty()) {
      temp = map.get(ApplicationConstants.IMPORT_HEADER_PRIORITY).get(0);
      if (!CommonUtils.isBlank(temp)) {
        ruleInstDTO.setPriority(new Integer(temp));
      }

    }
  }

  /**
   * Process start date.
   *
   * @param map
   *          the map
   * @param ruleInstDTO
   *          the rule inst dto
   */
  private void processStartDate(Map<String, List<String>> map, RuleInstDTO ruleInstDTO) {
    String temp;
    if (map.get(ApplicationConstants.IMPORT_HEADER_START_DATE) != null && !(map.get(ApplicationConstants.IMPORT_HEADER_START_DATE).isEmpty())) {
      temp = map.get(ApplicationConstants.IMPORT_HEADER_START_DATE).get(0);
      if (!CommonUtils.isBlank(temp)) {
        ruleInstDTO.setEffStartDate(CommonUtils.getDefFormatUTCDateFromStringWithoutTime(temp));
      }

    }
  }

  /**
   * Process sequence.
   *
   * @param map
   *          the map
   * @param ruleInstDTO
   *          the rule inst dto
   */
  private void processSequence(Map<String, List<String>> map, long masterChangeRequestId, RuleInstDTO ruleInstDTO,
      ValidationFailsDTO validationFailsDTO) {
    String temp;
    if (map.get(ApplicationConstants.IMPORT_HEADER_SEQUENCE) != null && !map.get(ApplicationConstants.IMPORT_HEADER_SEQUENCE).isEmpty()) {

      temp = map.get(ApplicationConstants.IMPORT_HEADER_SEQUENCE).get(0);
      if (!CommonUtils.isBlank(temp)) {
        ruleInstDTO.setSeq(new Long(temp));
        ruleInstDTO.setNewSequence(false);
        if (null != ruleInstDAOMongoImpl.findOneBySeqNo(ruleInstDTO.getMapDefId(), ruleInstDTO.getSeq())) {
          ruleInstDTO.setFoundInPrimary(Boolean.TRUE);
        } else if (!ruleInstChangeDAOMongoImpl.findAllBySeqNo(masterChangeRequestId, ruleInstDTO.getSeq()).isEmpty()) {
          ruleInstDTO.setFoundInPrimary(Boolean.FALSE);
        } else {
          AttributeFailureDTO attrFailureDTO = new AttributeFailureDTO(ApplicationConstants.LABEL_RULE_INST_SEQ_NO,
              ApplicationConstants.RULE_INST_IMPORT_ERROR_INVALID_SEQUENCE + ruleInstDTO.getSeq());
          validationFailsDTO.getFailures().add(attrFailureDTO);
        }

      } else {
        ruleInstDTO.setSeq(sequenceMongoDAOImpl.increment(Sequence.RULE_INST_SEQUENCE_FIELD.key()));
        ruleInstDTO.setNewSequence(true);
      }
    }
  }
}
