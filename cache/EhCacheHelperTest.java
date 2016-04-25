package com.citi.rulefabricator.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.citi.rulefabricator.dao.dos.AttributeValueDO;
import com.citi.rulefabricator.dao.dos.MasterAttributesDO;
import com.citi.rulefabricator.dao.jdbc.impl.DynamicDataSourceDAOJdbcImpl;
import com.citi.rulefabricator.dao.jdbc.mapper.MasterAttributeRowMapper;
import com.citi.rulefabricator.enums.ApplicationConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-tests.xml" })
public class EhCacheHelperTest {

    @Autowired
    private EhCacheHelper<String, List<AttributeValueDO>> ehCacheHelper;

    private String cacheName = ApplicationConstants.MASTER_ATTRIBUTES_CACHE_NAME;
    private MasterAttributesDO masterAttr;
    private DynamicDataSourceDAOJdbcImpl<MasterAttributesDO> masterAttributeDAOImpl;

    @Autowired
    DataSource masterAttributeDataSource;

    @Before
    public void setUp() {

        masterAttr = new MasterAttributesDO();
        masterAttr.setId(1);
        masterAttr.setAttrCd("CNTRPRTY_GOC");
        masterAttr.setAttrNm("14560");

        masterAttributeDAOImpl = new DynamicDataSourceDAOJdbcImpl<MasterAttributesDO>(masterAttributeDataSource, new MasterAttributeRowMapper());
    }

    @Test
    public void testPut() {

        // List<MasterAttributesDO> results =
        // masterAttributeDAOImpl.search("select * from masterattributes");
        Map<String, List<AttributeValueDO>> testDataMap = getTestData();
        for (String key : testDataMap.keySet()) {
            ehCacheHelper.put(key, testDataMap.get(key));
        }

        // ehCacheHelper.put(masterAttr.getAttrCd(), new
        // ArrayList<AttributeValueDO>(), cacheName);
    }

    @Test
    public void testGet() {
        Map<String, List<AttributeValueDO>> testDataMap = getTestData();
        for (String key : testDataMap.keySet()) {
            List<AttributeValueDO> attrValueLst = ehCacheHelper.get(key);
            System.out.println("Attribute list Size::::================================== " + attrValueLst.size());
            Assert.assertEquals("Equal Attribute List Size", testDataMap.get(key).size(), attrValueLst.size());
        }

        // List<AttributeValueDO> attrValueLst =
        // ehCacheHelper.get(masterAttr.getAttrCd(), cacheName);
    }

    @Ignore
    public void testPutMasterAttribute() {

        List<MasterAttributesDO> results = masterAttributeDAOImpl.search("select * from masterattributes");

        for (MasterAttributesDO attr : results) {

            // ehCacheHelper.put(attr.getAttrCd(), attr, cacheName);
        }
    }

    @Ignore
    public void testGetMasterAttribute() {

        List<MasterAttributesDO> results = masterAttributeDAOImpl.search("select * from masterattributes");

        for (MasterAttributesDO attr : results) {

            Object out = ehCacheHelper.get(attr.getAttrCd());

        }

    }

    private Map<String, List<AttributeValueDO>> getTestData() {

        Map<String, List<AttributeValueDO>> testDataMap = new HashMap<>();

        List<AttributeValueDO> valueList = new ArrayList<>();
        AttributeValueDO valueDo = new AttributeValueDO();
        valueDo.setValue("123");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("3435");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("14540006");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("14540009");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("14540766");
        valueList.add(valueDo);

        testDataMap.put("GOC_CODE", valueList);
        // ///
        valueList = new ArrayList<>();
        valueDo = new AttributeValueDO();
        valueDo.setValue("07");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("123");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("998");
        valueList.add(valueDo);

        testDataMap.put("LCL_LV_ID", valueList);
        // ///
        valueList = new ArrayList<>();
        valueDo = new AttributeValueDO();
        valueDo.setValue("001");
        valueList.add(valueDo);

        valueDo = new AttributeValueDO();
        valueDo.setValue("007");
        valueList.add(valueDo);

        testDataMap.put("ACC_BRANCH", valueList);

        return testDataMap;

    }

    @After
    public void tearDown() {

        ehCacheHelper = null;
        cacheName = null;
        masterAttr = null;
        // cacheManager.shutdown();
    }
}
