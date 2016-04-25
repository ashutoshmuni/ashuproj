package model;

import java.io.Serializable;

public class MBeanData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object mBeanData;

	public Object getmBeanData() {
		return mBeanData;
	}

	public void setmBeanData(Object mBeanData) {
		this.mBeanData = mBeanData;
	}
}
