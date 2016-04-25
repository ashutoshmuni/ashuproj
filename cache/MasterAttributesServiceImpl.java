package com.citi.rulefabricator.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;

import com.citi.rulefabricator.cache.impl.EhCacheHelper;
import com.citi.rulefabricator.dao.dos.AttributeDefDO;
import com.citi.rulefabricator.dao.dos.AttributeDomainDO;
import com.citi.rulefabricator.dao.dos.AttributeDomnDbDO;
import com.citi.rulefabricator.dao.dos.AttributeValueDO;
import com.citi.rulefabricator.dao.jdbc.impl.MasterAttributesDAOJdbcImpl;
import com.citi.rulefabricator.dao.mongo.impl.AttributeDefDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.AttributeDomainDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.AttributeDomainDbDAOMongoImpl;
import com.citi.rulefabricator.dao.mongo.impl.SearchDAOMongoImpl;
import com.citi.rulefabricator.dto.ConditionDTO;
import com.citi.rulefabricator.dto.CriteriaDTO;
import com.citi.rulefabricator.dto.MasterAttrMetaInfoDTO;
import com.citi.rulefabricator.dto.MasterAttributeInfoDTO;
import com.citi.rulefabricator.dto.PageDTO;
import com.citi.rulefabricator.dto.ResponseDTO;
import com.citi.rulefabricator.dto.SearchDTO;
import com.citi.rulefabricator.enums.ApplicationConstants.SEARCH_DATATYPE;
import com.citi.rulefabricator.enums.MongoOperator;
import com.citi.rulefabricator.exceptions.ApplicationException;
import com.citi.rulefabricator.mongo.MongoUtil;
import com.citi.rulefabricator.services.MasterAttributesService;

public class MasterAttributesServiceImpl implements MasterAttributesService, InitializingBean {

    private MongoUtil mongoUtil;

    private AttributeDefDAOMongoImpl attributeDefImpl;

    private AttributeDomainDbDAOMongoImpl attributeDomainDbImpl;

    private AttributeDomainDAOMongoImpl attributeDomainImpl;

    private MasterAttributesDAOJdbcImpl masterAttrDaoImpl;

    private EhCacheHelper<String, List<AttributeValueDO>> ehCacheWrapper;

    private SearchDAOMongoImpl searchDaoMongoImpl;

    private Boolean loadMasterAttrValues;

    @Override
    public void afterPropertiesSet() throws Exception {
        saveMasterAttributes();
        if (loadMasterAttrValues) {
            loadAndRefreshCache();
        }
    }

    @Override
    public void saveMasterAttributes() throws ApplicationException {
        saveAttributeDef();
        saveAttributeDomain();
        saveAttibuteDomainDB();
    }

    @Override
    public ResponseDTO<AttributeDefDO> loadAttributeDef() {

        ResponseDTO<AttributeDefDO> responseDto = new ResponseDTO<AttributeDefDO>();

        List<AttributeDefDO> attrDefList = new ArrayList<AttributeDefDO>();
        List<Document> docList = new ArrayList<Document>();

        docList = attributeDefImpl.load();

        attrDefList = mongoUtil.documentListToPojoList(docList, com.citi.rulefabricator.dao.dos.AttributeDefDO.class);

        responseDto.setData(attrDefList);
        return responseDto;
    }

    @Override
    public ResponseDTO<MasterAttributeInfoDTO> findAllAttributeDef() {

        ResponseDTO<MasterAttributeInfoDTO> response = new ResponseDTO<MasterAttributeInfoDTO>();
        List<Document> resultDocs = attributeDefImpl.load();
        List<MasterAttributeInfoDTO> masterAttrList = null;
        if (null != resultDocs && !resultDocs.isEmpty()) {
            masterAttrList = new ArrayList<MasterAttributeInfoDTO>();
            for (Document resultDoc : resultDocs) {
                AttributeDefDO attrDef = mongoUtil.documentToPojo(resultDoc, AttributeDefDO.class, true);
                masterAttrList.add(new MasterAttributeInfoDTO(attrDef));
            }
            response.setData(masterAttrList);
        }
        return response;
    }

    @Override
    public void saveAttributeDef() throws ApplicationException {
        // Load the data read from web service
        attributeDefImpl.saveAttributeDefList();

    }

    @Override
    public ResponseDTO<AttributeDomainDO> loadAttributeDomain() {

        ResponseDTO<AttributeDomainDO> responseDto = new ResponseDTO<AttributeDomainDO>();

        List<AttributeDomainDO> attrDefList = new ArrayList<AttributeDomainDO>();
        List<Document> docList = new ArrayList<Document>();

        docList = attributeDomainImpl.load();

        attrDefList = mongoUtil.documentListToPojoList(docList, com.citi.rulefabricator.dao.dos.AttributeDomainDO.class);

        responseDto.setData(attrDefList);

        return responseDto;
    }

    @Override
    public void saveAttributeDomain() throws ApplicationException {
        // Load the data read from web service
        attributeDomainImpl.saveAttributeDomain();

    }

    @Override
    public ResponseDTO<AttributeDomnDbDO> loadAttributeDomainDb() {

        ResponseDTO<AttributeDomnDbDO> responseDto = new ResponseDTO<AttributeDomnDbDO>();

        List<AttributeDomnDbDO> attrDefList = new ArrayList<AttributeDomnDbDO>();
        List<Document> docList = new ArrayList<Document>();

        docList = attributeDomainDbImpl.load();

        attrDefList = mongoUtil.documentListToPojoList(docList, com.citi.rulefabricator.dao.dos.AttributeDomnDbDO.class);

        responseDto.setData(attrDefList);

        return responseDto;
    }

    @Override
    public void saveAttibuteDomainDB() throws ApplicationException {
        // Load the data read from web service
        attributeDomainDbImpl.saveAttributeDomainDb();

    }

    public void loadAndRefreshCache() {

        List<MasterAttrMetaInfoDTO> attrMetaInfoList = new ArrayList<>();

        ResponseDTO<AttributeDefDO> attributeDefs = loadAttributeDef();
        ResponseDTO<AttributeDomainDO> attributeDomains = loadAttributeDomain();
        ResponseDTO<AttributeDomnDbDO> attributeDomainDb = loadAttributeDomainDb();
        MasterAttrMetaInfoDTO attrMetaInfoObj = null;

        for (AttributeDefDO attrDef : attributeDefs.getData()) {

            for (AttributeDomainDO attrDomain : attributeDomains.getData()) {

                if (attrDef.getDomain().equals(attrDomain.getName())) {

                    for (AttributeDomnDbDO domainDb : attributeDomainDb.getData()) {

                        if (attrDomain.getConnector() == domainDb.getConnectorId()) {

                            attrMetaInfoObj = new MasterAttrMetaInfoDTO();
                            attrMetaInfoObj.setAttributeCode(attrDef.getCode());
                            attrMetaInfoObj.setAttributeName(attrDef.getName());
                            attrMetaInfoObj.setDomainName(domainDb.getName());
                            attrMetaInfoObj.setDomainTableName(attrDomain.getTable());
                            attrMetaInfoObj.setColumnName(attrDomain.getColumnName());
                            attrMetaInfoObj.setJndi(domainDb.getJndi());

                            attrMetaInfoList.add(attrMetaInfoObj);
                            break;

                        }
                    }
                }
            }
        }

        fetchAndPopulateAttrValInCache(attrMetaInfoList);
    }

    private void fetchAndPopulateAttrValInCache(List<MasterAttrMetaInfoDTO> attrMetaInfoList) {

        ehCacheWrapper.clearCache();

        for (MasterAttrMetaInfoDTO attrMetaInfo : attrMetaInfoList) {
            List<AttributeValueDO> attrValueList = masterAttrDaoImpl.getAttributeValues(attrMetaInfo.getJndi(), attrMetaInfo.getColumnName(),
                    attrMetaInfo.getDomainTableName());
            ehCacheWrapper.put(attrMetaInfo.getAttributeName(), attrValueList);

        }
    }

    @Override
    /**
     * This method returns attribute values for the given attribute code as key.
     * 
     */
    public List<AttributeValueDO> getAttrValuesForCode(String attributeCode) {
        return ehCacheWrapper.get(attributeCode);
    }

    @Override
    public List<AttributeValueDO> getAttrValuesForName(String attributeName) {
        // fetch code for this attribute name
        Document doc = attributeDefImpl.findAttributeByName(attributeName);
        String attributeCode = null;
        if (null != doc) {
            attributeCode = mongoUtil.documentToPojo(doc, AttributeDefDO.class).getCode();
        }
        return ehCacheWrapper.get(attributeCode);
    }

    @Override
    public ResponseDTO<MasterAttributeInfoDTO> findAttributesStartingWith(String startsWith) {

        List<MasterAttributeInfoDTO> attrList = new ArrayList<MasterAttributeInfoDTO>();
        ResponseDTO<MasterAttributeInfoDTO> response = new ResponseDTO<MasterAttributeInfoDTO>();
        CriteriaDTO criteria = new CriteriaDTO(attributeDefImpl.getCollectionName());
        ConditionDTO condition = new ConditionDTO();
        condition.setColumn("code");
        condition.setDataType(SEARCH_DATATYPE.STRING.getValue());
        condition.setOperator(MongoOperator.STARTS_WITH);
        condition.setValue1(startsWith);
        criteria.getConditions().add(condition);
        SearchDTO search = new SearchDTO(criteria, new PageDTO());
        List<Document> documents = searchDaoMongoImpl.listDocuments(search);
        if (null != documents) {
            for (Document doc : documents) {
                AttributeDefDO attrDef = mongoUtil.documentToPojo(doc, AttributeDefDO.class, true);
                MasterAttributeInfoDTO attr = new MasterAttributeInfoDTO(attrDef);
                attrList.add(attr);
            }
        }
        response.setData(attrList);
        return response;
    }

    /**
     * @return the mongoUtil
     */
    public MongoUtil getMongoUtil() {
        return mongoUtil;
    }

    /**
     * @param mongoUtil
     *            the mongoUtil to set
     */
    public void setMongoUtil(MongoUtil mongoUtil) {
        this.mongoUtil = mongoUtil;
    }

    /**
     * @return the attributeDefImpl
     */
    public AttributeDefDAOMongoImpl getAttributeDefImpl() {
        return attributeDefImpl;
    }

    /**
     * @param attributeDefImpl
     *            the attributeDefImpl to set
     */
    public void setAttributeDefImpl(AttributeDefDAOMongoImpl attributeDefImpl) {
        this.attributeDefImpl = attributeDefImpl;
    }

    /**
     * @return the attributeDomainDbImpl
     */
    public AttributeDomainDbDAOMongoImpl getAttributeDomainDbImpl() {
        return attributeDomainDbImpl;
    }

    /**
     * @param attributeDomainDbImpl
     *            the attributeDomainDbImpl to set
     */
    public void setAttributeDomainDbImpl(AttributeDomainDbDAOMongoImpl attributeDomainDbImpl) {
        this.attributeDomainDbImpl = attributeDomainDbImpl;
    }

    /**
     * @return the attributeDomainImpl
     */
    public AttributeDomainDAOMongoImpl getAttributeDomainImpl() {
        return attributeDomainImpl;
    }

    /**
     * @param attributeDomainImpl
     *            the attributeDomainImpl to set
     */
    public void setAttributeDomainImpl(AttributeDomainDAOMongoImpl attributeDomainImpl) {
        this.attributeDomainImpl = attributeDomainImpl;
    }

    /**
     * @return the masterAttrDaoImpl
     */
    public MasterAttributesDAOJdbcImpl getMasterAttrDaoImpl() {
        return masterAttrDaoImpl;
    }

    /**
     * @param masterAttrDaoImpl
     *            the masterAttrDaoImpl to set
     */
    public void setMasterAttrDaoImpl(MasterAttributesDAOJdbcImpl masterAttrDaoImpl) {
        this.masterAttrDaoImpl = masterAttrDaoImpl;
    }

    /**
     * @return the ehCacheWrapper
     */
    public EhCacheHelper<String, List<AttributeValueDO>> getEhCacheWrapper() {
        return ehCacheWrapper;
    }

    /**
     * @param ehCacheWrapper
     *            the ehCacheWrapper to set
     */
    public void setEhCacheWrapper(EhCacheHelper<String, List<AttributeValueDO>> ehCacheWrapper) {
        this.ehCacheWrapper = ehCacheWrapper;
    }

    public SearchDAOMongoImpl getSearchDaoMongoImpl() {
        return searchDaoMongoImpl;
    }

    public void setSearchDaoMongoImpl(SearchDAOMongoImpl searchDaoMongoImpl) {
        this.searchDaoMongoImpl = searchDaoMongoImpl;
    }

    public Boolean getLoadMasterAttrValues() {
        return loadMasterAttrValues;
    }

    public void setLoadMasterAttrValues(Boolean loadMasterAttrValues) {
        this.loadMasterAttrValues = loadMasterAttrValues;
    }

}
