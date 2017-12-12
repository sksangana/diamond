/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * This class reads the <code>Connection.properties</code> file and loads the
 * required properties to run the TimeSeriesLoader . 
 * This class implements a Singleton pattern for returning the PropertiesManager object
 * 
 * @author sanganas
 *
 */
public class PropertiesManager {
	
	

	

	private static final Logger logger = Logger.getLogger(PropertiesManager.class.getName());
	
	private String MQ_QUEUE_MANAGER;
	
	private String MQ_QUEUE_NAME;
	
	private String MQ_OPEN_OPTIONS;
	
	private String LOG_PROP_LOCAIION;
	
	private String LOG_FILES_DEST;
	
	private String TIMESERIESDB_HOST;
	
	private String TIMESERIESDB_PORT;
	
	private String TIMESERIESDB_NAME;
	
	private String TIMESERIESDB_SERVERNAME;
	
	private String TIMESERIESDB_USER;
	
	private String TIMESERIESDB_PASSWORD;
	
	/**
	 * @return the lOG_PROP_LOCAIION
	 */
	public String getLOG_PROP_LOCAIION() {
		return LOG_PROP_LOCAIION;
	}


	/**
	 * @param lOG_PROP_LOCAIION the lOG_PROP_LOCAIION to set
	 */
	public void setLOG_PROP_LOCAIION(String lOG_PROP_LOCAIION) {
		LOG_PROP_LOCAIION = lOG_PROP_LOCAIION;
	}


	/**
	 * @return the lOG_FILES_DEST
	 */
	public String getLOG_FILES_DEST() {
		return LOG_FILES_DEST;
	}


	/**
	 * @param lOG_FILES_DEST the lOG_FILES_DEST to set
	 */
	public void setLOG_FILES_DEST(String lOG_FILES_DEST) {
		LOG_FILES_DEST = lOG_FILES_DEST;
	}


	/**
	 * @return the mQ_QUEUE_MANAGER
	 */
	public String getMQ_QUEUE_MANAGER() {
		return MQ_QUEUE_MANAGER;
	}


	/**
	 * @param mQ_QUEUE_MANAGER the mQ_QUEUE_MANAGER to set
	 */
	public void setMQ_QUEUE_MANAGER(String mQ_QUEUE_MANAGER) {
		MQ_QUEUE_MANAGER = mQ_QUEUE_MANAGER;
	}


	/**
	 * @return the mQ_QUEUE_NAME
	 */
	public String getMQ_QUEUE_NAME() {
		return MQ_QUEUE_NAME;
	}


	/**
	 * @param mQ_QUEUE_NAME the mQ_QUEUE_NAME to set
	 */
	public void setMQ_QUEUE_NAME(String mQ_QUEUE_NAME) {
		MQ_QUEUE_NAME = mQ_QUEUE_NAME;
	}

	
	
	/**
	 * @return the mQ_OPEN_OPTIONS
	 */
	public String getMQ_OPEN_OPTIONS() {
		return MQ_OPEN_OPTIONS;
	}


	/**
	 * @param mQ_OPEN_OPTIONS the mQ_OPEN_OPTIONS to set
	 */
	public void setMQ_OPEN_OPTIONS(String mQ_OPEN_OPTIONS) {
		MQ_OPEN_OPTIONS = mQ_OPEN_OPTIONS;
	}


	/**
	 * 
	 */
	private PropertiesManager() {
		// TODO Auto-generated constructor stub

		loadProperties();
		
		loadLogProperties();
	}
	
	private void loadLogProperties() {
		
		final InputStream inputStream = this.getClass().getResourceAsStream("/logging.properties");
		try
		{
		    LogManager.getLogManager().readConfiguration(inputStream);
		    inputStream.close();
		    String level = LogManager.getLogManager().getProperty("java.util.logging.FileHandler.level");
		    System.out.println("Log Level .. " + level);
		}
		catch (final IOException e)
		{
		    Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
		    Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	public static PropertiesManager propManager = null;
	
    /** This is Singleton class. this makes sure there is only one instance of the
     * PropertiesManager object and it is loaded only once.
     * 
     * @return Properties Manager
     */
    public static PropertiesManager getPropertiesManager() {
        
        if(propManager == null) {
            return propManager = new PropertiesManager();
        } else {
            return propManager;
        }
    
    }
	
    private void loadProperties() {
        try {
            if (logger.isLoggable(Level.FINEST)) {
            	String msg = "About to load configuration file Connection.properties";
            	logger.log(Level.FINEST, msg);
            }
            Properties prop = new Properties();
            InputStream in = this.getClass().getResourceAsStream("/Connection.properties");
            if (in == null) {
            	String msg = "Cannot load Connection.properties";
            	logger.log(Level.SEVERE, msg);
            } else {
            	//Now load and set the properties
                prop.load(in);
                init(prop);
                in.close();
                String logMsg = "Properties loaded successful.  ";
                logger.log(Level.INFO, logMsg);
            }
        } catch(Throwable e) {
        	String msg = "Cannot load configuration file Connection.properties";
        	logger.log(Level.SEVERE, msg, e);
         }
    }
    
    private void init(Properties prop) {
    	if(prop != null) {
    		
    		setMQ_QUEUE_MANAGER(prop.getProperty("MQ_QUEUE_MANAGER"));
    		setMQ_QUEUE_NAME(prop.getProperty("MQ_QUEUE_NAME"));
    		setMQ_OPEN_OPTIONS(prop.getProperty("MQ_OPEN_OPTIONS"));
    		setLOG_PROP_LOCAIION(prop.getProperty("LOG_PROP_LOCAIION"));
    		setLOG_FILES_DEST(prop.getProperty("LOG_PROP_LOCAIION"));
    		setTIMESERIESDB_HOST(prop.getProperty("TIMESERIESDB_HOST"));
    		setTIMESERIESDB_PORT(prop.getProperty("TIMESERIESDB_PORT"));
    		setTIMESERIESDB_NAME(prop.getProperty("TIMESERIESDB_NAME"));
    		setTIMESERIESDB_SERVERNAME(prop.getProperty("TIMESERIESDB_SERVERNAME"));
    		setTIMESERIESDB_USER(prop.getProperty("TIMESERIESDB_USER"));
    		setTIMESERIESDB_PASSWORD(prop.getProperty("TIMESERIESDB_PASSWORD"));
    	}
    }
    
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Test main() program
				PropertiesManager propMgr = new PropertiesManager();
				propMgr.loadProperties();
				System.out.println("Queue Name ..." + propMgr.getMQ_QUEUE_NAME());
	}


	/**
	 * @return the tIMESERIESDB_HOST
	 */
	public String getTIMESERIESDB_HOST() {
		return TIMESERIESDB_HOST;
	}


	/**
	 * @param tIMESERIESDB_HOST the tIMESERIESDB_HOST to set
	 */
	public void setTIMESERIESDB_HOST(String tIMESERIESDB_HOST) {
		TIMESERIESDB_HOST = tIMESERIESDB_HOST;
	}


	/**
	 * @return the tIMESERIESDB_PORT
	 */
	public String getTIMESERIESDB_PORT() {
		return TIMESERIESDB_PORT;
	}


	/**
	 * @param tIMESERIESDB_PORT the tIMESERIESDB_PORT to set
	 */
	public void setTIMESERIESDB_PORT(String tIMESERIESDB_PORT) {
		TIMESERIESDB_PORT = tIMESERIESDB_PORT;
	}


	/**
	 * @return the tIMESERIESDB_NAME
	 */
	public String getTIMESERIESDB_NAME() {
		return TIMESERIESDB_NAME;
	}


	/**
	 * @param tIMESERIESDB_NAME the tIMESERIESDB_NAME to set
	 */
	public void setTIMESERIESDB_NAME(String tIMESERIESDB_NAME) {
		TIMESERIESDB_NAME = tIMESERIESDB_NAME;
	}


	/**
	 * @return the tIMESERIESDB_SERVERNAME
	 */
	public String getTIMESERIESDB_SERVERNAME() {
		return TIMESERIESDB_SERVERNAME;
	}


	/**
	 * @param tIMESERIESDB_SERVERNAME the tIMESERIESDB_SERVERNAME to set
	 */
	public void setTIMESERIESDB_SERVERNAME(String tIMESERIESDB_SERVERNAME) {
		TIMESERIESDB_SERVERNAME = tIMESERIESDB_SERVERNAME;
	}


	/**
	 * @return the tIMESERIESDB_USER
	 */
	public String getTIMESERIESDB_USER() {
		return TIMESERIESDB_USER;
	}


	/**
	 * @param tIMESERIESDB_USER the tIMESERIESDB_USER to set
	 */
	public void setTIMESERIESDB_USER(String tIMESERIESDB_USER) {
		TIMESERIESDB_USER = tIMESERIESDB_USER;
	}


	/**
	 * @return the tIMESERIESDB_PASSWORD
	 */
	public String getTIMESERIESDB_PASSWORD() {
		return TIMESERIESDB_PASSWORD;
	}


	/**
	 * @param tIMESERIESDB_PASSWORD the tIMESERIESDB_PASSWORD to set
	 */
	public void setTIMESERIESDB_PASSWORD(String tIMESERIESDB_PASSWORD) {
		TIMESERIESDB_PASSWORD = tIMESERIESDB_PASSWORD;
	}

}
