/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citi.rulefabricator.enums.ApplicationConstants;
import com.citi.rulefabricator.exceptions.SystemRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MongoUtil.
 */
public class MongoUtil {

    /** The object mapper. */
    private ObjectMapper objectMapper;
    
    /** The ingnore null object mapper. */
    private ObjectMapper ingnoreNullObjectMapper;
    
    /** The ignore empty object mapper. */
    private ObjectMapper ignoreEmptyObjectMapper;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);

    /**
     * Json to document.
     *
     * @param json the json
     * @return the document
     */
    public Document jsonToDocument(String json) {

        if (null != json) {
            return Document.parse(json);
        }
        return null;
    }

    // pass a flag in the method for ignore null instead of creating
    // another method
    /**
     * Pojo to document.
     *
     * @param <T> the generic type
     * @param entity the entity
     * @return the document
     */
    public <T> Document pojoToDocument(T entity) {

        try {

            String jsonString = objectMapper.writeValueAsString(entity);
            return jsonToDocument(jsonString);

        } catch (IOException e) {
            LOGGER.error("MongoUtil::pojoToDocument - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6001, e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Pojo to document ignore null.
     *
     * @param <T> the generic type
     * @param entity the entity
     * @return the document
     */
    public <T> Document pojoToDocumentIgnoreNull(T entity) {

        Document document = null;
        try {

            String jsonString = ingnoreNullObjectMapper.writeValueAsString(entity);
            document = jsonToDocument(jsonString);
        } catch (IOException e) {
            LOGGER.error("MongoUtil::pojoToDocumentIgnoreNull - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6001, e.getMessage(), e.getStackTrace());
        }
        return document;
    }

    /**
     * Pojo list to document list.
     *
     * @param entityList the entity list
     * @return the list
     */
    public List<Document> pojoListToDocumentList(List<? extends Object> entityList) {
        List<Document> result = null;
        if (null != entityList) {
            result = new ArrayList<Document>();
            for (Object entity : entityList) {
                result.add(pojoToDocumentIgnoreNull(entity));
            }
        }
        return result;
    }

    /**
     * Pojo to document ignore empty.
     *
     * @param <T> the generic type
     * @param entity the entity
     * @return the document
     */
    public <T> Document pojoToDocumentIgnoreEmpty(T entity) {

        Document document = null;
        try {

            String jsonString = ignoreEmptyObjectMapper.writeValueAsString(entity);
            document = jsonToDocument(jsonString);
        } catch (IOException e) {
            LOGGER.error("MongoUtil::pojoToDocumentIgnoreNull - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6001, e.getMessage(), e.getStackTrace());
        }
        return document;
    }

    /**
     * Document to pojo.
     *
     * @param <T> the generic type
     * @param mongoDoc the mongo doc
     * @param clazz the clazz
     * @return the t
     */
    public <T> T documentToPojo(Document mongoDoc, Class<T> clazz) {
        String json = mongoDoc.toJson();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("MongoUtil::documentToPojo - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6002, e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Document to pojo.
     *
     * @param <T> the generic type
     * @param mongoDoc the mongo doc
     * @param clazz the clazz
     * @param ignoreNull the ignore null
     * @return the t
     */
    public <T> T documentToPojo(Document mongoDoc, Class<T> clazz, boolean ignoreNull) {
        String json = mongoDoc.toJson();
        try {
            if (ignoreNull) {
                return ignoreEmptyObjectMapper.readValue(json, clazz);
            }
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("MongoUtil::documentToPojo - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6002, e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Document to pojo list.
     *
     * @param <T> the generic type
     * @param mongoDoc the mongo doc
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> documentToPojoList(Document mongoDoc, Class<T> clazz) {
        String json = mongoDoc.toJson();
        try {
            return Arrays.asList(objectMapper.readValue(json, clazz));
        } catch (IOException e) {
            LOGGER.error("MongoUtil::documentToPojoList - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6002, e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Document list to pojo list.
     *
     * @param <T> the generic type
     * @param docs the docs
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> documentListToPojoList(List<Document> docs, Class<T> clazz) {
        try {
            List<T> returnDocs = new ArrayList<T>();
            if (docs != null) {
                String json;

                for (Document doc : docs) {
                    json = doc.toJson();
                    returnDocs.add(objectMapper.readValue(json, clazz));
                }
            }

            return returnDocs;
        } catch (IOException e) {
            LOGGER.error("MongoUtil::documentListToPojoList - Error file not found ", e);
            throw new SystemRuntimeException(ApplicationConstants.ERRORCODE_6002, e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * Gets the object mapper.
     *
     * @return the object mapper
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Sets the object mapper.
     *
     * @param objectMapper the new object mapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sets the ingnore null object mapper.
     *
     * @param ingnoreNullObjectMapper the new ingnore null object mapper
     */
    public void setIngnoreNullObjectMapper(ObjectMapper ingnoreNullObjectMapper) {
        this.ingnoreNullObjectMapper = ingnoreNullObjectMapper;
    }

    /**
     * Sets the ignore empty object mapper.
     *
     * @param ignoreEmptyObjectMapper the new ignore empty object mapper
     */
    public final void setIgnoreEmptyObjectMapper(final ObjectMapper ignoreEmptyObjectMapper) {
        this.ignoreEmptyObjectMapper = ignoreEmptyObjectMapper;
    }

}
