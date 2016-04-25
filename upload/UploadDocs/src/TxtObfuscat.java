/*******************************************************************
*                                                                  *
*                           NOTICE                                 *
*                                                                  *
*   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS                  *
*   CONFIDENTIAL INFORMATION OF SOFTBRANDS AND/OR ITS AFFILIATES   *
*   OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR       *
*   WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND            *
*   ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH       *
*   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.                 *
*   ALL OTHER RIGHTS RESERVED.                                     *
*                                                                  *
*   (C) COPYRIGHT 2012 SOFTBRANDS.  ALL RIGHTS RESERVED.           *
*   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE                 *
*   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF SOFTBRANDS          *
*   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS             *
*   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE              *
*   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                       *
*                                                                  *
********************************************************************/
//$Header:   V:/NGPMS/archives/sourcecode/com/dstm/common/tools/deploy/TxtObfuscat.java-arc   1.1   Jan 20 2011 10:43:08   cyao  $


//package com.dstm.common.tools.deploy;

import com.dstm.common.tools.pw.PwEncrypt;


public class TxtObfuscat {

	public static String decode(String s) throws Exception
	{

		if (s == null || s.length() == 0)
			return null;

		PwEncrypt enc = PwEncrypt.getInstance();

		return enc.decryptPassword(s);
	}

	public static String encode(String s) throws Exception
	{

		if (s == null || s.length() == 0)
			return null;

		PwEncrypt enc = PwEncrypt.getInstance();

		return enc.encryptPassword(s);
	}





}
