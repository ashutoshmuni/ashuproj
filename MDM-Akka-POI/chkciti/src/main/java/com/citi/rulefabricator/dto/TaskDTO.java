/* 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
package com.citi.rulefabricator.dto;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.citi.rulefabricator.util.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Class TaskDTO.
 */
public class TaskDTO extends BaseDTO /*implements Comparator<TaskDTO>*/ {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7074092894655825682L;

    /**
     * The Enum TaskStatus.
     */
    public enum TaskStatus {

        /** The NEW. */
        NEW("NEW"),
        /** The completed. */
        COMPLETED("COMPLETED"),
        /** The failed. */
        FAILED("FAILED"),
        /** The timeout. */
        TIMEOUT("TIMEOUT"),
        /** The in progress. */
        IN_PROGRESS("IN_PROGRESS");

        /** The value. */
        private String value;

        /**
         * Instantiates a new task status.
         *
         * @param value
         *            the value
         */
        private TaskStatus(String value) {
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
         * @return the document
         */
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * The Enum TaskType.
     */
    public enum TaskType {

        /** The import. */
        IMPORT("IMPORT"),
        /** The export. */
        EXPORT("EXPORT"),
        /** The data refresh. */
        DATA_REFRESH("DATA_REFRESH"),
        /** The outbound. */
        OUTBOUND("OUTBOUND"),

        /** Validation Task type. */
        VALIDATION("VALIDATION");

        /** The value. */
        private String value;

        /**
         * Instantiates a new task type.
         *
         * @param value
         *            the value
         */
        private TaskType(String value) {
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
         * @return the document
         */
        public String toString() {
            return this.value + "";
        }
    }

    /**
     * The Enum TaskCategory.
     */
    public enum TaskCategory {

        /** The map group def. */
        MAP_GROUP_DEF("MAP_GROUP_DEF"),
        /** The map def. */
        MAP_DEF("MAP_DEF"),
        /** The rule def. */
        RULE_DEF("RULE_DEF"),
        /** The rule inst. */
        RULE_INST("RULE_INST"),
        
        /**  Outbound generation. */
        OUTBOUND_GEN("OUTBOUND_GEN");

        /** The value. */
        private String value;

        /**
         * Instantiates a new task category.
         *
         * @param value
         *            the value
         */
        private TaskCategory(String value) {
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
         * @return the document
         */
        public String toString() {
            return this.value + "";
        }
    }

    /**
     * The Enum TaskSource.
     */
    public enum TaskSource {

        /** The manual. */
        MANUAL("MANUAL"),
        /** The scheduled. */
        SCHEDULED("SCHEDULED"),
        /** The ondemand. */
        ONDEMAND("ON DEMAND");

        /** The value. */
        private String value;

        /**
         * Instantiates a new task source.
         *
         * @param value
         *            the value
         */
        private TaskSource(String value) {
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
         * @return the document
         */
        public String toString() {
            return this.value + "";
        }
    }

    /** The task status. */
    private TaskStatus status;

    /** The task type. */
    private TaskType type;

    /** The task category. */
    private TaskCategory category;

    /** The category id. */
    private Long categoryId;

    /** The category name. */
    private String categoryName;

    /** The task source. */
    private TaskSource source;

    /** The message. */
    private String message;

    /** The message. */
    private String detailmessage;

    /** The start dt. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_TIME_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date startDt;

    /** The end dt. */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonUtils.DEFAULT_DATE_TIME_FORMAT, timezone = CommonUtils.DEFAULT_TIME_ZONE)
    private Date endDt;

    /** The no of records processed. */
    private Long noOfRecordsProcessed;

    /** The file path. */
    private String filePath;

    /** The input. */
    private Map<String, String> input = new HashMap<String, String>();

    /** The output. */
    private Map<String, String> output = new HashMap<String, String>();
    
    /** The master id. */
    private Long masterId;

    /**
     * Gets the task status.
     *
     * @return the task status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the task status.
     *
     * @param status the new status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets the task type.
     *
     * @return the task type
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Sets the task type.
     *
     * @param type the new type
     */
    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * Gets the task category.
     *
     * @return the task category
     */
    public TaskCategory getCategory() {
        return category;
    }

    /**
     * Sets the task category.
     *
     * @param category the new category
     */
    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    /**
     * Gets the task source.
     *
     * @return the task source
     */
    public TaskSource getSource() {
        return source;
    }

    /**
     * Sets the task source.
     *
     * @param source the new source
     */
    public void setSource(TaskSource source) {
        this.source = source;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the detailmessage.
     *
     * @return the detailmessage
     */
    public String getDetailmessage() {
        return detailmessage;
    }

    /**
     * Sets the detailmessage.
     *
     * @param detailmessage the new detailmessage
     */
    public void setDetailmessage(String detailmessage) {
        this.detailmessage = detailmessage;
    }

    /**
     * Gets the start dt.
     *
     * @return the start dt
     */
    public Date getStartDt() {
        return startDt;
    }

    /**
     * Sets the start dt.
     *
     * @param startDt
     *            the new start dt
     */
    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    /**
     * Gets the end dt.
     *
     * @return the end dt
     */
    public Date getEndDt() {
        return endDt;
    }

    /**
     * Sets the end dt.
     *
     * @param endDt
     *            the new end dt
     */
    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    /**
     * Gets the no of records processed.
     *
     * @return the no of records processed
     */
    public Long getNoOfRecordsProcessed() {
        return noOfRecordsProcessed;
    }

    /**
     * Sets the no of records processed.
     *
     * @param noOfRecordsProcessed
     *            the new no of records processed
     */
    public void setNoOfRecordsProcessed(Long noOfRecordsProcessed) {
        this.noOfRecordsProcessed = noOfRecordsProcessed;
    }

    /**
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath
     *            the new file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the category id.
     *
     * @return the category id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category id.
     *
     * @param categoryId
     *            the new category id
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the category name.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name.
     *
     * @param categoryName
     *            the new category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the input.
     *
     * @return the input
     */
    public Map<String, String> getInput() {
        return input;
    }

    /**
     * Sets the input.
     *
     * @param input
     *            the input
     */
    public void setInput(Map<String, String> input) {
        this.input = input;
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    public Map<String, String> getOutput() {
        return output;
    }

    /**
     * Sets the output.
     *
     * @param output
     *            the output
     */
    public void setOutput(Map<String, String> output) {
        this.output = output;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
/*    @Override
    public int compare(TaskDTO o1, TaskDTO o2) {
        return o2.get_id().intValue() - o1.get_id().intValue();
    }
*/
    /**
     * Gets the master id.
     *
     * @return the masterId
     */
    public Long getMasterId() {
        return masterId;
    }

    /**
     * Sets the master id.
     *
     * @param masterId            the masterId to set
     */
    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

}
