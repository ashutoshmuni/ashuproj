/** 
 * Copyright (C) Citi Group, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * 
 */
package com.citi.rulefabricator.poi;

import java.util.List;
import java.util.Map;

/**
 * The Interface ExcelRowContentCallBack.
 */
public interface ExcelRowContentCallBack {
	
	/**
	 * Process row.
	 *
	 * @param rowNum the row num
	 * @param map the map
	 * @throws Exception the exception
	 */
	void processRow(int rowNum, Map<String, List<String>> map) throws Exception;
}
