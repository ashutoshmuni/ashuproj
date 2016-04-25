/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.enums;

/**
 * The Class ApplicationConstants.
 */
public final class ApplicationConstants {

  /** The Constant MASTER_CHANGE_REQUEST. */
  public static final String MASTER_CHANGE_REQUEST = "master_change_request";

  /** The Constant USERNAMEPARAMETER. */
  public static final String USERNAMEPARAMETER = "SM_USER";

  /** The Constant APPROVERREQUESTID. */
  public static final String APPROVERREQUESTID = "REQUEST_ID";

  /** The Constant VALIDATION_FAIL. */
  public static final String VALIDATION_FAIL = "VALIDATION_FAILURES";

  /** The Constant UNIQUE_KEY. */
  public static final String UNIQUE_KEY = "Unique Key";

  /** The Constant EXCEL_ROW_NUMBER. */
  public static final String EXCEL_ROW_NUMBER = "Excel Row Number";

  /** The Constant USERBEAN. */
  public static final String USERBEAN = "USER_OBJ";

  /** The Constant INPUTATTR. */
  public static final String INPUTATTR = "input";

  /** The Constant OUTPUTATTR. */
  public static final String OUTPUTATTR = "output";

  /** The Constant OUTPUTATTR_NAME. */
  public static final String OUTPUTATTR_NAME = "output.name";

  /** The Constant _id. */
  public static final String _id = "_id";

  /** The Constant DEFAULTPAGENUMBER. */
  public static final int DEFAULTPAGENUMBER = 1;

  /** The Constant DEFAULTPAGESIZE. */
  public static final int DEFAULTPAGESIZE = 10;

  /** The Constant DEFAULTSORTCOLUMN. */
  public static final String DEFAULTSORTCOLUMN = ApplicationConstants._id;

  /** The Constant DEFAULTSORTORDER. */
  public static final String DEFAULTSORTORDER = PAGE_SORT.DESC.getValue();

  /** The Constant HASHCHAR. */
  public static final String HASHCHAR = "#";

  /** The Constant DOTCHAR. */
  public static final String DOTCHAR = ".";

  /** The Constant DOTSTARCHAR. */
  public static final String DOTSTARCHAR = ".*";

  /** The Constants CARET. */
  public static final String CARET = "^";

  /** The Constant VALUECOLUMN. */
  public static final String VALUECOLUMN = "value";

  /** The Constant ATTRVALUECOLUMN. */
  public static final String ATTRVALUECOLUMN = "Value";

  /** The Constant DIMENSION_COLUMN. */
  public static final String DIMENSION_COLUMN = "Is Dimension";

  /** The Constant TREEID_COLUMN. */
  public static final String TREEID_COLUMN = "Tree ID";

  /** The Constant TEMP_ID. */
  public static final String TEMP_ID = "id";

  /** The Constant ATTR_NAME. */
  public static final String ATTR_NAME = "attributeName";

  /** The Constant FAILUREMESSAGE. */
  public static final String FAILUREMESSAGE = "failureMessage";

  /** The Constant WORKFLOW_SHEET. */
  public static final String WORKFLOW_SHEET = "WORKFLOW_SHEET";

  /** The Constant WORKFLOW_. */
  public static final String WORKFLOW_ = "WORKFLOW_";

  /** The Constant WORKFLOW. */
  public static final String WORKFLOW = "workflow";

  /** The Constant WORKFLOWS. */
  public static final String WORKFLOWS = "workflows";

  /** The Constant MASTER_ATTRIBUTES_CACHE_NAME. */
  public static final String MASTER_ATTRIBUTES_CACHE_NAME = "master_attributes_cache";

  /** The Constant COMPARISON_URL. */
  public static final String COMPARISON_URL = "jms.bpm.workflow.request.comparison.url";

  /** The Constant VALID_STATUS_KEY. */
  public static final String VALID_STATUS_KEY = "bpm.workflow.valid.status.";

  /** The Constant JMS Type. */
  public static final String JMS_TYPE_KEY = "jms.bpm.workflow.request.msg.type";

  /** The Constant JMS_MAPGRP_SUCCESS_STATUS_KEY. */
  public static final String JMS_MAPGRP_SUCCESS_STATUS_KEY = "jms.mapgroup.response.success.status";

  /** The Constant CHECK_EDITABLE. */
  public static final String CHECK_EDITABLE = "checkEditable";

  /** The Constant EFFECTIVE_END_DATE. */
  public static final String EFFECTIVE_END_DATE = "effEndDate";

  /** The Constant EFFECTIVE_END_DATE. */
  public static final String EFFECT_DATE = "effectiveDate";

  /** The Constant EFFECTIVE_START_DATE. */
  public static final String EFFECTIVE_START_DATE = "effStartDate";

  /** The Constant EFFECTIVE_START_DATE. */
  public static final String EFFECTIVE_DATE = "Effective Date";

  /** The Constant EFFECTIVE_STATUS. */
  public static final String EFFECTIVE_STATUS = "effStatus";

  /** The Constant DELTA_EFF_START_DATE. */
  public static final String DELTA_EFF_START_DATE = "delta.effStartDate";

  /** The Constant DELTA_EFF_END_DATE. */
  public static final String DELTA_EFF_END_DATE = "delta.effEndDate";

  /** The Constant MAP_DEF_ID. */
  public static final String MAP_DEF_ID = "mapDefId";

  /** The Constant RULE_DEF_ID. */
  public static final String RULE_DEF_ID = "ruleDefId";

  /** The Constant EXECUTION_ORDER. */
  public static final String MAP_ASSOCIATION = "mapAssociation";

  /** The Constant GROUP_TYPE_CODE. */
  public static final String GROUP_TYPE_CODE = "grpTypeCd";

  /** The Constant NAME1. */
  public static final String NAME1 = "Name";

  /** The Constant TARGET_RECORD_CODE. */
  public static final String TARGET_RECORD_CODE = "trgtRecCd";

  /** The Constant FEED_FILE_TYPE_CODE. */
  public static final String FEED_FILE_TYPE_CODE = "feedFileTypeCd";

  /** The Constant COUNTRY_CODE. */
  public static final String COUNTRY_CODE = "ctryCd";

  /** The Constant REGION_CODE. */
  public static final String REGION_CODE = "rgnCd";

  /** The Constant DESCRIPTION. */
  public static final String DESCRIPTION1 = "description";

  /** The Constant RULEINST_READ_BATCH. */
  public static final long RULEINST_READ_BATCH = 10000;

  /** The Constant RULEINST_WRITE_BATCH. */
  public static final long RULEINST_WRITE_BATCH = 5000;

  /** The CONSTANT WEB_SERVICE_REQUEST_INSTANCE *. */
  public static final String WEB_SERVICE_REQUEST_INSTANCE = "webservice.request.body.instance";

  /** The CONSTANT WEB_SERVICE_REQUEST_BRANCH *. */
  public static final String WEB_SERVICE_REQUEST_BRANCH = "webservice.request.body.branch";

  /** The Constant MSG_NO_FILE_RECEIVED. */
  public static final String MSG_NO_FILE_RECEIVED = "No File Received";

  /** The Constant MSG_NO_TASK_ID. */
  public static final String MSG_NO_TASK_ID = "No TaskId to fetch failed imported data";

  /** The Constant MSG_IMPORT_FAILURE. */
  public static final String MSG_IMPORT_FAILURE = "File Import failed. Please view message in task list for details";

  /** The Constant MSG_IMPORT_FAILURE_INVALID_MAPID. */
  public static final String MSG_IMPORT_FAILURE_INVALID_MAPID = "File Import failed. MapId is invalid";

  /** The Constant MSG_FILE_IMPORT_SUCCESSFUL. */
  public static final String MSG_FILE_IMPORT_SUCCESSFUL = "File Import started";

  /** The Constant MSG_FILE_UPLOAD_SUCCESSFUL. */
  public static final String MSG_FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully";

  /** The Constant MSG_FILE_IMPORT_SUCCESSFUL. */
  public static final String MSG_FILE_EXPORT_SUCCESSFUL = "File Export started";

  /** The Constant MSG_FILE_IMPORT_COMPLETED. */
  public static final String MSG_FILE_IMPORT_COMPLETED = "File Import completed";

  /** The Constant MSG_EXPORT_FAILURE. */
  public static final String MSG_EXPORT_FAILURE = "File Export failed. Please view message in task list for details";

  /** The Constant MSG_EXPORT_FAILURE_INVALID_MAPID. */
  public static final String MSG_EXPORT_FAILURE_INVALID_MAPID = "File Export failed. No map found for the mapId.";

  /** The Constant MSG_EXPORT_TEMPLATE_FAILED *. */
  public static final String MSG_EXPORT_TEMPLATE_FAILED = "Template Export failed. No map found for the mapId.";

  /** The Constant MSG_CACHE_REFRESH_FAILED *. */
  public static final String MSG_CACHE_REFRESH_FAILED = "Cache Refresh failed. Please check Cache Process Log for details.";

  /** The Constant MSG_CACHE_REFRESH_SUCCESS. */
  public static final String MSG_CACHE_REFRESH_SUCCESS = "Cache Refresh completed successfully.";

  /** The Constant RULE_DEF_FILE_UPLOAD_APP. */
  public static final String RULE_DEF_FILE_UPLOAD_APP = "RuleDefFileUploadApp";

  /** The Constant MASTER_ATTRIBUTE_CACHE_APP. */
  public static final String MASTER_ATTRIBUTE_CACHE_APP = "MasterAttributeCacheApp";
  /** The Constant RULE_DEF_FILE_IMPORT_APP. */
  public static final String RULE_DEF_FILE_IMPORT_APP = "RuleDefFileImportApp";

  /** The Constant RULE_DEF_FILE_EXPORT_APP. */
  public static final String RULE_DEF_FILE_EXPORT_APP = "RuleDefFileExportApp";

  /** The Constant MIMETYPE. */
  public static final String MIMETYPE = "application/octet-stream";

  /** The Constant HEADERKEY. */
  public static final String HEADERKEY = "Content-Disposition";

  /** The Constant HEADERVALUE. */
  public static final String HEADERVALUE = "attachment; filename=\"%s\"";

  /** The Constant DELTA_ID. */
  public static final String DELTA_ID = "delta._id";

  /** The Constant DELTA_MAPDEF_ID. */
  public static final String DELTA_MAPDEF_ID = "delta.mapDefId";

  /** The Constant DELTA_SEQ. */
  public static final String DELTA_SEQ = "delta.seq";

  /** The Constant SNAPSHOT_ID. */
  public static final String SNAPSHOT_ID = "snapshot._id";

  /** The Constant CHANGE_ID. */
  public static final String CHANGE_ID = "changeId";

  /** The Constant REQUESTED_BY. */
  public static final String REQUESTED_BY = "requestedBy";

  /** The Constant DATE_FORMAT. */
  public static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss";

  /** The Constant FILE_EXT. */
  public static final String FILE_EXT = ".xlsx";

  /** The Constant DELTA. */
  public static final String DELTA = "delta";

  /** The Constant DELTA_PREFIX. */
  public static final String DELTA_PREFIX = "delta.";
  /** The Constant SEQUENCE_NUMBER. */
  public static final String SEQ = "seq";

  /** The Constant MASTER_ID. */
  public static final String MASTER_ID = "masterId";

  /** The Constant IDENTIFIER. */
  public static final String IDENTIFIER = "identifier";

  /** The Constant WFSTATUS. */
  public static final String WFSTATUS = "wfStatus";

  /** The Constant DATE$. */
  public static final String DATE = "$date";

  /** The Constant REGEX$. */
  public static final String REGEX = "$regex";

  /** The Constant OPTIONS$. */
  public static final String OPTIONS = "$options";

  /** The Constant CASE_INSENSITIVE. */
  public static final String CASE_INSENSITIVE = "i";

  /** The Constant COUNT. */
  public static final String COUNT = "count";

  /** The Constant COLLECTION_NAME. */
  public static final String COLLECTION_NAME = "collectionName";

  /** The Constant STATUS. */
  public static final String STATUS = "status";

  /** The Constant SNAPSHOT. */
  public static final String SNAPSHOT = "snapshot";

  /** The Constant PRIORITY. */
  public static final String PRIORITY = "priority";

  /** The Constant UPDATED_BY. */
  public static final String UPDATED_BY = "updatedBy";

  /** The Constant UPDATED_DATE. */
  public static final String UPDATED_DATE = "updatedDt";

  /** The Constant CREATED_DATE. */
  public static final String CREATED_DATE = "createdDt";

  /** The Constant CREATED_BY. */
  public static final String CREATED_BY = "createdBy";

  /** The Constant IMPORT_HEADER_SEQUENCE. */
  public static final String IMPORT_HEADER_SEQUENCE = "Sequence Number";

  /** The Constant IMPORT_HEADER_START_DATE. */
  public static final String IMPORT_HEADER_START_DATE = "Start Date";

  /** The Constant IMPORT_HEADER_PRIORITY. */
  public static final String IMPORT_HEADER_PRIORITY = "Priority";

  /** The Constant IMPORT_HEADER_VALUE. */
  public static final String IMPORT_HEADER_VALUE = "Value";

  /** The Constant IMPORT_IS_DIMENSION. */
  public static final String IMPORT_HEADER_IS_DIMENSION = "Is Dimension";

  /** The Constant IMPORT_HEADER_TREE_ID. */
  public static final String IMPORT_HEADER_TREE_ID = "Tree ID";

  /** The Constant ERROR_ATTRIBUTE_VALUE_MISSING. */
  public static final String ERROR_ATTRIBUTE_VALUE_MISSING = "Attribute value is missing";

  /** The Constant ERROR_INVALID_ATTRIBUTE. */
  public static final String ERROR_INVALID_ATTRIBUTE = "Value is invalid : ";

  /** The Constant ID_COLUMN_LABEL. */
  public static final String ID_COLUMN_LABEL = "_id";

  /** The Constant CREATEDBY_COLUMN_LABEL. */
  public static final String CREATEDBY_COLUMN_LABEL = "createdBy";

  /** The Constant TASK_RECORDS_PROCESSED_COLUMN. */
  public static final String TASK_RECORDS_PROCESSED_COLUMN = "noOfRecordsProcessed";

  /** The Constant TYPE_COLUMN_LABEL. */
  public static final String TYPE_COLUMN_LABEL = "type";

  /** The Constant CATEGORY_COLUMN_LABEL. */
  public static final String CATEGORY_COLUMN_LABEL = "category";

  /** The Constant CATEGORYID_COLUMN_LABEL. */
  public static final String CATEGORYID_COLUMN_LABEL = "categoryId";

  /** The Constant MAKER. */
  public static final String MAKER = "maker";

  /** The Constant CHECKER. */
  public static final String CHECKER = "checker";

  /** The Constant REQUEST_NOT_AVAILABLE_DB. */
  public static final String REQUEST_NOT_AVAILABLE_DB = "Request not available in DB!!";

  /** The Constant REQUEST_NOT_AVAILABLE_CHECKER. */
  public static final String REQUEST_NOT_AVAILABLE_CHECKER = "Request not available for checker!!";

  /** The Constant REQUEST_NOT_AVAILABLE_EXISTING_USER. */
  public static final String REQUEST_NOT_AVAILABLE_EXISTING_USER = "Request not available for the existsing user!!";

  /** The Constant CNTRPRTY_GOC. */
  public static final String CNTRPRTY_GOC = "CNTRPRTY_GOC";

  /** The Constant COUNTERPARTYGOC. */
  public static final String COUNTERPARTYGOC = "Counterparty GOC";

  /** The Constant STRING. */
  public static final String STRING = "STRING";

  /** The Constant ADMIN. */
  public static final String ADMIN = "admin";

  /** The Constant CODE. */
  public static final String CODE = "code";

  /** The Constant DESCRIPTION. */
  public static final String DESCRIPTION = "Description";

  /** The Constant DIM_GRP_DESC. */
  public static final String DIM_GRP_DESC = "DIM_GRP_DESC";

  /** The Constant CPRT_GOC. */
  public static final String CPRT_GOC = "CPRT_GOC";

  /** The Constant FILE_EXT_DAT. */
  public static final String FILE_EXT_DAT = ".DAT";

  /** The Constant FILE_EXT_CSV. */
  public static final String FILE_EXT_CSV = ".csv";

  /** The Constant ID. */
  public static final String ID = "_id";

  /** The Constant NAME. */
  public static final String NAME = "name";

  /** The Constant LABEL. */
  public static final String LABEL = "label";

  /** The Constant IN_ID. */
  public static final String IN_ID = "inId";

  /** The Constant OUT_ID. */
  public static final String OUT_ID = "outId";

  /** The Constant NEXT. */
  public static final String NEXT = "next";

  /** The Constant GOC_MANAGED_SEGMENT_CODE. */
  public static final String GOC_MANAGED_SEGMENT_CODE = "GOC Managed Segment Code";

  /** The Constant EMPTY_STRING. */
  public static final String EMPTY_STRING = "";

  /** The Constant CW77508. */
  public static final String CW77508 = "CW77508";

  /** The Constant CPRT_GOC_BU_ICE_CD. */
  public static final String CPRT_GOC_BU_ICE_CD = "CPRT_GOC_BU_ICE_CD";

  /** The Constant FM_DWHDT_DTL_GOC. */
  public static final String FM_DWHDT_DTL_GOC = "FM_DWHDT_DTL_GOC";

  /** The Constant GOC. */
  public static final String GOC = "GOC";

  /** The Constant ONE_DIGIT. */
  public static final String ONE_DIGIT = "1";

  /** The Constant ROWNUM. */
  public static final String ROWNUM = "_rowNum";

  /** The Constant ROW_NUM. */
  public static final String ROW_NUM = "rowNum";

  /** The Constant NOT_FOUND_AT_ROW_1. */
  public static final String NOT_FOUND_AT_ROW_1 = "' not found at row 1";

  /** The Constant FILEIMPORTAPP. */
  public static final String FILEIMPORTAPP = "FileImportApp";

  /** The Constant FILEEXPORTAPP. */
  public static final String FILEEXPORTAPP = "FileExportApp";

  /** The Constant GOC_AFF_ICE_CD. */
  public static final String GOC_AFF_ICE_CD = "GOC_AFF_ICE_CD";

  /** The Constant NUMBER. */
  public static final String NUMBER = "NUMBER";

  /** The Constant GOC_DSMT. */
  public static final String GOC_DSMT = "GOC_DSMT";

  /** The Constant GOC_FRM. */
  public static final String GOC_FRM = "GOC_FRM";

  /** The Constant GOC_MNG_SEG_CD. */
  public static final String GOC_MNG_SEG_CD = "GOC_MNG_SEG_CD";

  /** The Constant MASTER_ATTR_VAL. */
  public static final String MASTER_ATTR_VAL = "master_attributes_values";

  /** The Constant DISPLAY_ORDER. */
  public static final String DISPLAY_ORDER = "displayOrder";

  /** The Constant ANCHOR. */
  public static final String ANCHOR = "anchor";

  /** The Constant COMPARE. */
  public static final String COMPARE = "compare";

  /** The Constant LABEL_MAP_ID. */
  public static final String LABEL_MAP_ID = "Map ID";

  /** The Constant LABEL_EFFECTIVE_START_DATE. */
  public static final String LABEL_EFFECTIVE_START_DATE = "Effective Start Date";

  /** The Constant LABEL_EFFECTIVE_END_DATE. */
  public static final String LABEL_EFFECTIVE_END_DATE = "Effective End Date";

  /** The Constant LABEL_RULE_INST_SEQ_NO. */
  public static final String LABEL_RULE_INST_SEQ_NO = "Sequence Number";

  /** The Constant LABEL_RULE_INST_PRIORITY. */
  public static final String LABEL_RULE_INST_PRIORITY = "Priority";

  /** The Constant LABEL_RULE_INST_VALID_RECORD. */
  public static final String LABEL_RULE_INST_VALID_RECORD = "Valid Record";

  /** The Constant TEMPLATE_NAME. */
  public static final String TEMPLATE_NAME = "templateName";

  /** The constant TEMPLATE. */
  public static final String TEMPLATE = "template";

  /** The Constant TEMPLATE_EXISTS. */

  public static final String IN_PROGRESS = "In Progress";

  /** The Constant UPLOAD_FAILED. */
  public static final String UPLOAD_FAILED = "Upload failed !! ";

  /** The Constant TEMPLATE_EXISTS. */
  public static final String TEMPLATE_EXISTS = "TemplateWithNameExists";

  /** The Constant MAP_DEF. */
  public static final String MAP_DEF = "map_def";

  /** The Constant MAP_DEF_CHANGE. */
  public static final String MAP_DEF_CHANGE = "map_def_change";

  /** The Constant MAPS_MAPDEFID. */
  public static final String MAPS_MAPDEFID = "maps.mapDefId";

  /** The Constant MAPS. */
  public static final String MAPS = "maps";

  /** The Constant MAP_GROUP_DEF. */
  public static final String MAP_GROUP_DEF = "map_group_def";

  /** The Constant RULE_INST. */
  public static final String RULE_INST = "rule_inst";

  /** The Constant RULE_INST_CHANGE. */
  public static final String RULE_INST_CHANGE = "rule_inst_change";

  /** The Constant RULE_DEF. */
  public static final String RULE_DEF = "rule_def";

  /** The Constant MAP_DEF_LABEL. */
  public static final String MAP_DEF_LABEL = "Map";

  /** The Constant MAP_GROUP_DEF_LABEL. */
  public static final String MAP_GROUP_DEF_LABEL = "Map Group";

  /** The Constant RULE_INST_LABEL. */
  public static final String RULE_INST_LABEL = "Map Data";

  /** The Constant RULE_DEF_LABEL. */
  public static final String RULE_DEF_LABEL = "Map Definition";

  /** The Constant END_OF_TIME. */
  public static final String END_OF_TIME = "12/31/9999";

  /** The Constant taskId. */
  public static final String TASKID = "taskId";

  /** The Constant EXPORT_FILE_PREFIX. */
  public static final String EXPORT_FILE_PREFIX = "RF_";

  /** The Constant UNDERSCORECHAR. */
  public static final String UNDERSCORECHAR = "_";

  /** The Constant EXCELEXTN. */
  public static final String EXCELEXTN = ".xlsx";

  /**
   * The Constant FAILURE_ID. Represents failure field of a rule instance document
   */
  public static final String FAILURE_ID = "failureId";

  /** The Constant FAILURES. */
  public static final String FAILURES = "failures";

  /** The Constant FAILURE_MESSAGE. */
  public static final String FAILURE_MESSAGE = "Failure Message";

  /** The Constant MSG_FILE_UPLOAD_STARTED. */
  public static final String MSG_FILE_UPLOAD_STARTED = "File uploaded started";

  /** The Constant MSG_UPLOAD_FAILURE_INVALID_MAPID. */
  public static final String MSG_UPLOAD_FAILURE_INVALID_MAPID = "File Upload failed. MapId is invalid";

  /** The Constant CANCEL_SUCCESSFUL. */
  public static final String CANCEL_SUCCESSFUL = "Task Canceled";

  /** The Constant CANCEL_FAILURE. */
  public static final String CANCEL_FAILURE = "Task Cancel failed";

  /** The Constants MERGE_STATUS. */
  public static final String CHANGE_MERGE_STATUS = "mergeStatus";

  /** The Constant CHANGE. */
  public static final String CHANGE = "_change";

  /** The Constant UNKNOWN_ERRORCODE. */
  public static final String UNKNOWN_ERRORCODE = "100000";

  /** The Constant UNDEFINED_ERROR. */
  public static final String UNDEFINED_ERROR = "Undefined error encountered, please correct the applicationError.properties";

  /** The Constant MATCH_START. */
  public static final String MATCH_START = "^";

  /** The Constant APPROVED_BY. */
  public static final String APPROVED_BY = "approvedBy";

  /** The Constant MODIFIED_BY. */
  public static final String MODIFIED_BY = "modifiedBy";

  /** The Constant APPROVED_DATE. */
  public static final String APPROVED_DATE = "approvedDt";

  /** The Constant REQUESTED_DATE. */
  public static final String REQUESTED_DATE = "requestedDt";

  /** The Constant MODIFIED_DATE. */
  public static final String MODIFIED_DATE = "modifiedDt";

  /** The Constant APPROVED_FROM_DATE. */
  public static final String APPROVED_FROM_DATE = "approvedFrmDt";

  /** The Constant REQUESTED_FROM_DATE. */
  public static final String REQUESTED_FROM_DATE = "requestedFrmDt";

  /** The Constant APPROVED_TO_DATE. */
  public static final String APPROVED_TO_DATE = "approvedToDt";

  /** The Constant REQUESTED_TO_DATE. */
  public static final String REQUESTED_TO_DATE = "requestedToDt";

  /** The Constant MAP_NAME. */
  public static final String MAP_NAME = "mapName";

  /** The Constant MAP_DESCRIPTION. */
  public static final String MAP_DESCRIPTION = "mapDesc";

  /** The Constant RULE_INST_VALID_RECORD_TRUE. */
  public static final String RULE_INST_VALID_RECORD_TRUE = "True";

  /** The Constant RULE_INST_VALID_RECORD_FALSE. */
  public static final String RULE_INST_VALID_RECORD_FALSE = "False";

  /** The Constant REQUEST. */
  public static final String REQUEST = "request";

  /** The Constant NEW_DATA. */
  public static final String NEW_DATA = "new_data";

  /** The Constant AUTH_DETAILS. */
  public static final String AUTH_DETAILS = "auth_details";

  /** The Constant TREE_STRUCT_PREFIX. */
  public static final String TREE_STRUCT_PREFIX = "TS_";

  /** The Constant UPDATED_BY. */
  public static final String UPDATED_MAPS = "updatedMaps";

  /** The Constant ACTIVE. */
  public static final String ACTIVE = "ACTIVE";

  /** The Constant INACTIVE. */
  public static final String INACTIVE = "INACTIVE";

  /** The Constant PIPE. */
  public static final String PIPE = "|";

  /** The Constant RESTRICTION_TYPE_INDICATOR. */
  public static final String RESTRICTION_TYPE_INDICATOR = "I";

  /** The Constant COLUMNS_MISMATCH. */
  public static final String COLUMNS_MISMATCH = "No of Colums in the Sheet doesnot match that of the current Rule";

  /** The Constant INPUT_ATTRS_MISSING. */
  public static final String INPUT_ATTRS_MISSING = " No columns found for validating input attribute headers";

  /** The Constant OUTPUT_ATTRS_MISSING. */
  public static final String OUTPUT_ATTRS_MISSING = " No columns found for validating input attribute headers";

  /** The Constant JMS_TYPE. */
  public static final String JMS_TYPE = "JMS_TYPE";

  /** The Constant JMS_REPLY_TO. */
  public static final String JMS_REPLY_TO = "JMS_REPLY_TO";

  /** The Constant JMS_CORRELATION_ID. */
  public static final String JMS_CORRELATION_ID = "JMS_CORRELATION_ID";

  /** The Constant MAPGROUP_JMS_TYPE_KEY. */
  public static final String MAPGROUP_JMS_TYPE_KEY = "jms.mapgroup.request.msg.type";

  /** The Constant MULTI_PART_FILE. */
  public static final String MULTI_PART_FILE = "multiPartFile";

  /** The Constant SPACE. */
  public static final String SPACE = " ";

  /** The Constant BLANK. */
  public static final String BLANK = "";

  /** The Constant QUERY1. */
  public static final String QUERY1 = "QUERY1";

  /** The Constant QUERY2. */
  public static final String QUERY2 = "QUERY2";

  /** The Constant QUERY3. */
  public static final String QUERY3 = "QUERY3";

  /** The Constant QUERY_NAME. */
  public static final String QUERY_NAME = "queryName";

  /** The Constant ACTOR_REF. */
  public static final String ACTOR_REF = "Actor_ref";

  /** The Constant PROCESS_NAME. */
  public static final String PROCESS_NAME = "process";

  /** The Constant FDL_PROCESS. */
  public static final String FDL_PROCESS = "FDL_RSLVR";

  /** The Constant L2G_PROCESS. */
  public static final String L2G_PROCESS = "L2G_RESOLVER";

  /** The Constant CORRELATION_ID. */
  public static final String CORRELATION_ID = "correlationId";

  /** The Constant RESPONSE_MESSAGE. */
  public static final String RESPONSE_MESSAGE = "responseMsg";

  /** The Constant CHANGE_DOCUMENT_MERGE. */
  public static final String CHANGE_DOCUMENT_MERGE = "merge";

  /** The Constant CHANGE_DOCUMENT_MERGE_ID. */
  public static final String CHANGE_DOCUMENT_MERGE_ID = "merge._id";

  /** The Constant SQL_QUERY. */
  public static final String SQL_QUERY = "SQL_QUERY";

  /** The Constant MONGO_QUERY. */
  public static final String MONGO_QUERY = "MongoQuery";

  /** The Constant RF_USER. */
  public static final String RF_USER = "RF_USER";

  /** The Constant ATTRIB_DEF_LENGTH. */
  public static final String ATTRIB_DEF_LENGTH = "length";

  /** The Constant FILE_GENERATION_ERRORS. */
  public static final String FILE_GENERATION_ERRORS = "File Generation errors";

  /** The Constant FILE_GENERATION_STARTED. */
  public static final String FILE_GENERATION_STARTED = "File_Gen_Started";

  /** The Constant FILE_GENERATION_SUCCESS. */
  public static final String FILE_GENERATION_SUCCESS = "File_Gen_Completed";

  /** The Constant FILE_GENERATION_FAILED. */
  public static final String FILE_GENERATION_FAILED = "File_Gen_Erroredout";

  /** The Constant RULE_DEF_CHANGE. */
  public static final String RULE_DEF_CHANGE = "rule_def_change";

  /** The Constant MAP_GROUP_DEF_CHANGE. */
  public static final String MAP_GROUP_DEF_CHANGE = "map_group_def_change";

  /** The Constant RULES_FABRICATOR. */
  public static final String RULES_FABRICATOR = "RulesFabricator";

  /** The Constant PORTAL. */
  public static final String PORTAL = "Portal";

  /** The Constant OUTBOUNDS_ERROR_CODE. */
  public static final String OUTBOUNDS_ERROR_CODE = "LM-0028";

  /** The Constant OUTBOUNDS_PROCESS_STATUS. */
  public static final String OUTBOUNDS_PROCESS_STATUS = "PROCESS_STATUS";

  /** ERROR CODE. */
  public static final String ERRORCODE_5005 = "error.5005";

  /** The Constant ERRORCODE_6001. */
  public static final String ERRORCODE_6001 = "error.6001";

  /** The Constant ERRORCODE_6002. */
  public static final String ERRORCODE_6002 = "error.6002";

  /** The Constant OUTBOUND_ITEM_INPROGRESS. */
  public static final String OUTBOUND_ITEM_INPROGRESS = "outbounds.dequeue.error.item.inprogress";

  /** The Constant OUTBOUNDS_LOCK_TIMEOUT. */
  public static final String OUTBOUNDS_LOCK_TIMEOUT = "outbounds.dequeue.error.lock.timeout";

  /** The Constant OUTBOUNDS_THREAD_INTERRUPTED. */
  public static final String OUTBOUNDS_THREAD_INTERRUPTED = "outbounds.dequeue.error.lock.thread.interrupted";

  /** The Constant RULE_INST_IMPORT_ERROR_INVALID_SEQUENCE. */
  public static final String RULE_INST_IMPORT_ERROR_INVALID_SEQUENCE = "No such value found in the system : ";

  /**
   * Instantiates a new application constants.
   */
  private ApplicationConstants() {
  }

  /**
   * The Enum PAGE_SORT.
   */
  public static enum PAGE_SORT {

    /** The asc. */
    ASC("ASC", 1),
    /** The desc. */
    DESC("DESC", -1);

    /** The value. */
    private String value;

    /** The int value. */
    private int intValue;

    /**
     * Instantiates a new page sort.
     * 
     * @param value
     *          the value
     * @param intValue
     *          the int value
     */
    private PAGE_SORT(String value, int intValue) {
      this.value = value;
      this.intValue = intValue;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * Gets the int value.
     * 
     * @return the int value
     */
    public int getIntValue() {
      return this.intValue;
    }

    /**
     * 
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }
  }

  /**
   * The Enum RESPONSE_STATUS.
   */
  public static enum RESPONSE_STATUS {

    /** The success. */
    SUCCESS(200),
    /** The client error. */
    CLIENT_ERROR(400),
    /** The server error. */
    SERVER_ERROR(500);

    /** The value. */
    private int value;

    /**
     * Instantiates a new response status.
     * 
     * @param value
     *          the value
     */
    private RESPONSE_STATUS(int value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public int getValue() {
      return this.value;
    }

    /**
     * 
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }
  }

  /**
   * The Enum SEARCH_DATATYPE.
   */
  public static enum SEARCH_DATATYPE {

    /** The string. */
    STRING("STRING"),
    /** The date. */
    DATE("DATE"),
    /** The integer. */
    INTEGER("INTEGER"),
    /** The double. */
    DOUBLE("DOUBLE"),
    /** The datetime. */
    DATETIME("DATETIME"),
    /** The long. */
    LONG("LONG"),
    /** The boolean. */
    BOOLEAN("BOOLEAN");

    /** The value. */
    private String value;

    /**
     * Instantiates a new search datatype.
     * 
     * @param value
     *          the value
     */
    private SEARCH_DATATYPE(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * 
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }
  }

  /**
   * The Enum PAGE_MODE.
   */
  public static enum PAGE_MODE {

    /** The read. */
    READ(1),
    /** The edit. */
    EDIT(2);

    /** The value. */
    private int value;

    /**
     * Instantiates a new page mode.
     * 
     * @param value
     *          the value
     */
    private PAGE_MODE(int value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public int getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }

    /**
     * Gets the page mode.
     * 
     * @param value
     *          the value
     * @return the page mode
     */
    public static PAGE_MODE getPageMode(int value) {
      for (PAGE_MODE page : PAGE_MODE.values()) {
        if (value == page.getValue()) {
          return page;
        }
      }
      return PAGE_MODE.READ;
    }
  }

  /**
   * The Enum WORKITEM_STATUS.
   */
  public static enum WORKITEM_STATUS {

    /** The draft. */
    DRAFT("DRAFT"),
    /** The submitted. */
    SUBMITTED("SUBMITTED"),
    /** The claimed. */
    CLAIMED("CLAIMED"),
    /** The abort. */
    ABORT("ABORT"),
    /** The resubmitted. */
    RESUBMITTED("RESUBMITTED"),
    /** The approved. */
    APPROVED("APPROVED"),
    /** The rejected. */
    REJECTED("REJECTED"),
    /** The released. */
    RELEASED("RELEASED"),
    /** The started. */
    STARTED("STARTED"),
    /** The rework. */
    REWORK("REWORK");

    /** The value. */
    private String value;

    /**
     * Instantiates a new workitem status.
     * 
     * @param value
     *          the value
     */
    private WORKITEM_STATUS(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }
  }

  /**
   * The Enum CHANGE_STATUS.
   */
  public static enum CHANGE_STATUS {

    /** The new. */
    NEW("N"),
    /** The update. */
    UPDATE("U"),
    /** The delete. */
    DELETE("D");

    /** The value. */
    private String value;

    /**
     * Instantiates a new change status.
     * 
     * @param value
     *          the value
     */
    private CHANGE_STATUS(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum ATTRIBUTE_DOMAIN_TYPE.
   */
  public static enum ATTRIBUTE_DOMAIN_TYPE {

    /** The Domain. */
    DOMAIN("domain"),
    /** The AltDomain. */
    ALT_DOMAIN("altDomain"),
    /** The dimensionGroup. */
    DIMENSION_GROUP("dimensionGroup"),
    /** The domainClassifSchemaName. */
    DOMAIN_CLASSIFICATION_SCHEME_NAME("domainClassifSchemeName"),
    /** The domainClassifSchemaName. */
    TREE("tree");

    /** The value. */
    private String value;

    /**
     * Instantiates a new domain type.
     * 
     * @param value
     *          the value
     */
    private ATTRIBUTE_DOMAIN_TYPE(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }
  }

  /**
   * The Enum JMS_MSG_TYPE.
   */
  public static enum JMS_MSG_TYPE {

    /** The request. */
    REQUEST("Request"),
    /** The response. */
    RESPONSE("Response"),
    /** The acknowledgment. */
    ACKNOWLEDGMENT("Acknowledgment"),
    /** The error. */
    ERROR("Error");

    /** The value. */
    private String value;

    /**
     * Instantiates a new jms msg type.
     * 
     * @param value
     *          the value
     */
    private JMS_MSG_TYPE(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum EXEC_STATUS.
   */
  public static enum EXEC_STATUS {

    /** The success. */
    SUCCESS,
    /** The in progress. */
    IN_PROGRESS,
    /** The failure. */
    FAILURE;
  }

  /**
   * The Enum HEADER.
   */
  public static enum HEADER {

    /** The xpath. */
    XPATH,
    /** The label. */
    LABEL,
    /** The none. */
    NONE;
  }

  /**
   * The Enum MERGE_STATUS.
   */
  public static enum MERGE_STATUS {

    /** The pending. */
    PENDING("P"),
    /** The complete. */
    COMPLETE("C");

    /** The value. */
    private String value;

    /**
     * Instantiates a new merge status.
     * 
     * @param value
     *          the value
     */
    private MERGE_STATUS(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum PAGE_TYPE.
   */
  public static enum PAGE_TYPE {
    /** MAKER. */
    MAKER(1),
    /** CHECKER. */
    CHECKER(2),
    /** AUDIT. */
    AUDIT(3);

    /** The value. */
    private int value;

    /**
     * Instantiates a new PAGE_TYPE.
     * 
     * @param value
     *          the value
     */
    private PAGE_TYPE(int value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public int getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value + "";
    }
  }

  /**
   * The Enum PROCESS_STATUS.
   */
  public static enum PROCESS_STATUS {

    /** The success. */
    SUCCESS("Successful"),
    /** The started. */
    STARTED("Started"),
    /** The failure. */
    FAILURE("Failed");

    /** The value. */
    private String value;

    /**
     * Instantiates a new process status.
     *
     * @param value
     *          the value
     */
    private PROCESS_STATUS(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }
  }

  /**
   * The Enum CACHE_PROCESS_TYPE.
   */
  public static enum CACHE_PROCESS_TYPE {

    /** The success. */
    STARTUP_DELTA_REFRESH("STARTUP DELTA REFRESH"),
    /** The started. */
    SCHEDULED_DELTA_REFRESH("SCHEDULED DELTA REFRESH"),
    /** The failure. */
    SCHEDULED_LOOKUP_VALUES_REFRESH("SCHEDULED LOOKUP VALUES REFRESH"),
    /** The success. */
    SCHEDULED_ALL_ATTRIBUTES_REFRESH("SCHEDULED ALL ATTRIBUTES REFRESH");

    /** The value. */
    private String value;

    /**
     * Instantiates a new cache process type.
     *
     * @param value
     *          the value
     */
    private CACHE_PROCESS_TYPE(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }
  }

  /**
   * The Enum MERGE_STATUS.
   */
  public static enum EXPORT_HISTORY {

    /** The status. */
    STATUS("Staus"),
    /** The updated by. */
    UPDATED_BY("Updated By"),
    /** The updated date. */
    UPDATED_DATE("Updated Date"),
    /** The comments. */
    COMMENTS("Comments");

    /** The value. */
    private String value;

    /**
     * Instantiates a new merge status.
     * 
     * @param value
     *          the value
     */
    private EXPORT_HISTORY(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum DIMENSION_FLAG.
   */
  public static enum DIMENSION_FLAG {

    /** The yes. */
    YES("Y"),
    /** The no. */
    NO("N");

    /** The value. */
    private String value;

    /**
     * Instantiates a new dimension flag.
     *
     * @param value
     *          the value
     */
    private DIMENSION_FLAG(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum QUERY.
   */
  public static enum QUERY {

    /** The QUER y1. */
    QUERY1("Query 1"),
    /** The QUER y2. */
    QUERY2("Query 2"),
    /** The QUER y3. */
    QUERY3("Query 3"),
    /** The QUER y4. */
    QUERY4("Query 4"),
    /** The QUER y5. */
    QUERY5("Query 5"),
    /** The QUER y6. */
    QUERY6("Query 6");

    /** The value. */
    private String value;

    /**
     * Instantiates a new query.
     *
     * @param value
     *          the value
     */
    private QUERY(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum FDL_RSLVR.
   */
  public static enum FDL_RSLVR {

    /** The fbb dim group data. */
    FBB_DIM_GROUP_DATA("fbb_dim_group_data"),
    /** The tree strc partition v. */
    TREE_STRC_PARTITION_V("tree_strc_partition_v"),
    /** The fbb final dim group info fdl pmf. */
    FBB_FINAL_DIM_GROUP_INFO_FDL_PMF("fbb_final_dim_group_info_FDL_PMF"),
    /** The fbb final dim group setup info. */
    FBB_FINAL_DIM_GROUP_SETUP_INFO("fbb_final_dim_group_setup_info"),
    /** The fbb tree custom info. */
    FBB_TREE_CUSTOM_INFO("fbb_tree_custom_info");

    /** The value. */
    private String value;

    /**
     * Instantiates a new fdl rslvr.
     *
     * @param value
     *          the value
     */
    private FDL_RSLVR(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * The Enum L2G_RESOLVER.
   */
  public static enum L2G_RESOLVER {

    /** The FB b_ di m_ grou p_ dat a_ l2 g_ l2 g_ resolver. */
    FBB_DIM_GROUP_DATA_L2G_L2G_RESOLVER("fbb_dim_group_data_L2G_L2G_RESOLVER"),
    /** The TRE e_ str c_ partitio n_ v_ l2 g_ l2 g_ resolver. */
    TREE_STRC_PARTITION_V_L2G_L2G_RESOLVER("tree_strc_partition_v_L2G_L2G_RESOLVER"),
    /** The FB b_ fina l_ di m_ grou p_ inf o_ l2 g_ l2 g_ resolver. */
    FBB_FINAL_DIM_GROUP_INFO_L2G_L2G_RESOLVER("fbb_final_dim_group_info_L2G_L2G_RESOLVER"),
    /** The FB b_ fina l_ di m_ grou p_ setu p_ inf o_ l2 g_ l2 g_ resolver. */
    FBB_FINAL_DIM_GROUP_SETUP_INFO_L2G_L2G_RESOLVER("fbb_final_dim_group_setup_info_L2G_L2G_RESOLVER"),
    /** The FB b_ regiona l_ ma p_ i d_ l2 g_ l2 g_ resolver. */
    FBB_REGIONAL_MAP_ID_L2G_L2G_RESOLVER("fbb_regional_map_id_L2G_L2G_RESOLVER"),
    /** The FB b_ go c_ regio n_ l2 g_ l2 g_ resolver. */
    FBB_GOC_REGION_L2G_L2G_RESOLVER("fbb_goc_region_L2G_L2G_RESOLVER");

    /** The value. */
    private String value;

    /**
     * Instantiates a new l2 g_ resolver.
     *
     * @param value
     *          the value
     */
    private L2G_RESOLVER(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
      return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     * @return the String
     */
    public String toString() {
      return this.value;
    }
  }

  /**
   * Gets the rule def effective status.
   *
   * @param status
   *          the status
   * @return the rule def effective status
   */
  public static String getRuleDefEffectiveStatus(String status) {
    if ("ACTIVE".equals(status)) {
      return "A";
    } else {
      return "I";
    }
  }

  /**
   * Trim.
   *
   * @param value
   *          the value
   * @param length
   *          the length
   * @return the string
   */
  public static String trim(String value, int length) {
    String returnVal = null;

    if (null != value) {
      final int valueLen = value.length();
      if (valueLen > length) {
        returnVal = value.substring(0, length);
      } else {
        returnVal = value;
      }
    }

    return returnVal;
  }

}
