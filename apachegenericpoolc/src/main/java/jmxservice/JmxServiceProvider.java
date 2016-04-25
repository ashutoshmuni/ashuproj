package jmxservice;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import model.JmxHost;
import model.MBeanData;

import org.apache.log4j.Logger;

import pool.JmxPoolManager;
import util.MBeanC;

public class JmxServiceProvider {
	private static final JmxPoolManager jmxPoolHelper = new JmxPoolManager();
	private final static Logger LOGGER = Logger
			.getLogger(JmxServiceProvider.class);
	private static final MBeanC mBeanC = new MBeanC();

	static {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new MyShutdownHook());
	}
	/**
	 * 
	 * @param host
	 * @return
	 */
	public MBeanData dataProvider(JmxHost host) {
		JMXConnector jmxConnector = null;

		MBeanData mBeanData = new MBeanData();
		try {
			if (mBeanC.getMBeanData(host.getHost()) == null) {
				synchronized (mBeanC) {
					if (mBeanC.getMBeanData(host.getHost()) == null) {
						jmxConnector = jmxPoolHelper
								.getConnectionFromPool(host);
						MBeanServerConnection mBeanServerConnection = jmxConnector
								.getMBeanServerConnection();
						Object mBeanDbData = mBeanServerConnection
								.getAttribute(new ObjectName(
										"java.lang:type=OperatingSystem"),
										"TotalPhysicalMemorySize");
						mBeanData.setmBeanData(mBeanDbData);
						mBeanC.addMBeanData(mBeanData,host.getHost());
						if (LOGGER.isDebugEnabled()) {
							Long cValue = (Long) mBeanData.getmBeanData();
							LOGGER.debug("Value ###################### : "
									+ cValue);
						}
					} else {
						mBeanData = mBeanC.getMBeanData(host.getHost());
						LOGGER.debug("C Value **************  : "
								+ (Long) mBeanC.getMBeanData(host.getHost())
										.getmBeanData());
					}
				}
			} else {
				LOGGER.debug("C Value **************  : "
						+ (Long) mBeanC.getMBeanData(host.getHost())
								.getmBeanData());
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			LOGGER.error(e.getMessage());
		} finally {
			if (jmxConnector != null) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("connection returned");
				}
				jmxPoolHelper.returnConnectionToPool(host, jmxConnector);
			}
		}
		return mBeanData;
	}

	public static class MyShutdownHook extends Thread {
		public void run() {
			LOGGER.debug("Running shutdown hook...");
			/**
             * The pool invalidate functionality.
             */
            jmxPoolHelper.cleanup();
            mBeanC.invalidate();
		}
	}
}
