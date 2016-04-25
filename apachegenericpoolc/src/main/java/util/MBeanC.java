package util;

import model.MBeanData;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.log4j.Logger;

import pool.TestJmxPool;

public class MBeanC {
	private final static Logger LOGGER = Logger.getLogger(MBeanC.class);
	private JCS cache; 
	public MBeanC(){
		
	      try {
	    	// Load the cache
			cache = JCS.getInstance( "mBeanCache" );
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addMBeanData( MBeanData mBeanData, String key )
	  {
		LOGGER.debug("in addMBeanData key : " + key);
	    try
	    {
	      cache.put( key, mBeanData );
	    }
	    catch( CacheException e )
	    {
	      e.printStackTrace();
	    }
	    LOGGER.debug("Out addMBeanData");
	  }
	  public MBeanData getMBeanData( String key )
	  {
	    return ( MBeanData )cache.get( key );
	  }
	  public void removeMBeanData( String key )
	  {
	    try
	    {
	      cache.remove( key );
	    }
	    catch( CacheException e )
	    {
	      e.printStackTrace();
	    }
	  }	
	  
	  public void invalidate(){
		  LOGGER.debug("******** In Invaldiating ");
		  cache.dispose();
	  }
}
