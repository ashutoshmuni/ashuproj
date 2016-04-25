/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ApplicationProperties.
 */
public class ApplicationProperties {

    /** The properties. */
    private static Properties properties;
    
    /** The initialized. */
    private static boolean initialized = false;
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProperties.class);

    /**
     * Instantiates a new application properties.
     *
     * @param properties the properties
     */
    public ApplicationProperties(Properties properties) {
        LOGGER.info("Loading ApplicationProperties.class ...");
        if (!initialized) {
            ApplicationProperties.properties = properties;
            initialized = true;
            String logFilePath = ApplicationProperties.properties.getProperty("rulesfabricator.log.file");

            System.setProperty("rulesfabricator_logfile_path", logFilePath);
        }
        LOGGER.info("ApplicationProperties.class Loaded...");
    }

    /**
     * Gets the property.
     *
     * @param key the key
     * @return the property
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public static Properties getProperties() {
        return properties;
    }

}
