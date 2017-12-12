/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.mq;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.diamonmdoffshore.edgeservice.util.PropertiesManager;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
 * @author sanganas
 *
 */
public class MQConnectionManager {

	private static final Logger logger = Logger.getLogger(MQConnectionManager.class.getName());
	
	private String QueueManagerName;
	
	private String QueueName;
	
	private String QueueOpenOptions;
	
	private MQQueue AccessQueue;
	
	public static MQConnectionManager mqManager = null;
	
	private MQQueueManager qManager;
	
	private MQQueue queue;
	
	
	
	/** This is Singleton class. this makes sure there is only one instance of the
     * MQConnectionManager object and it is loaded only once.
     * 
     * @return MQConnectionManager
     */
    public static MQConnectionManager getMQConnectionManager() {
        
        if(mqManager == null) {
            return mqManager = new MQConnectionManager();
        } else {
            return mqManager;
        }
    
    }
	
    
	
	/**
	 * 
	 */
	public MQConnectionManager() {
		// TODO Auto-generated constructor stub
		
		try {
			PropertiesManager propManager = PropertiesManager.getPropertiesManager();
			
			this.setQueueManagerName( propManager.getMQ_QUEUE_MANAGER());
			this.setQueueName(propManager.getMQ_QUEUE_NAME());
			this.setQueueOpenOptions(propManager.getMQ_OPEN_OPTIONS());
			
			logger.log(Level.INFO, "Successfully got the MQ connection attributes ...");
			
			this.OpenAccessQueue();
			
			logger.log(Level.INFO, "Successfully Opened th Queue for Access ..");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Cannot get connection", e);
		}
	}
	
	@SuppressWarnings("unused")
	private void OpenAccessQueue() {
		
		MQQueueManager qMgr = null;
		MQQueue queue = null;
	      // Create a connection to the QueueManager
		logger.log(Level.INFO, "Connecting to queue manager: " + this.getQueueManagerName());
	    //System.out.println("Connecting to queue manager: " + this.getQueueManagerName());
	    try {
			qMgr = new MQQueueManager(this.getQueueManagerName());
			this.setqManager(qMgr);
		} catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Cannot Connect to queue manager: " + this.getQueueManagerName());
		}
	      
	   // Now specify the queue that we wish to open and the open options
	      int qOpenOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INQUIRE;
	      //int QOpenOptions = Integer.parseInt();
	      try {
			queue = qMgr.accessQueue(this.getQueueName(), qOpenOptions);
			this.setQueue(queue);
		} catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "Cannot open the Quque: " + this.getQueueName());
		}
	     this.setAccessQueue(queue); 
	}

	/**
	 * @return the queueManager
	 */
	public String getQueueManagerName() {
		return QueueManagerName;
	}

	/**
	 * @param queueManager the queueManager to set
	 */
	public void setQueueManagerName(String queueManager) {
		QueueManagerName = queueManager;
	}

	/**
	 * @return the queueName
	 */
	public String getQueueName() {
		return QueueName;
	}

	/**
	 * @param queueName the queueName to set
	 */
	public void setQueueName(String queueName) {
		QueueName = queueName;
	}

	/**
	 * @return the queueOpenOptions
	 */
	public String getQueueOpenOptions() {
		return QueueOpenOptions;
	}

	/**
	 * @param queueOpenOptions the queueOpenOptions to set
	 */
	public void setQueueOpenOptions(String queueOpenOptions) {
		QueueOpenOptions = queueOpenOptions;
	}
	


	/**
	 * @return the accessQueue
	 */
	public MQQueue getAccessQueue() {
		

		return AccessQueue;
	}

	/**
	 * @param accessQueue the accessQueue to set
	 */
	public void setAccessQueue(MQQueue accessQueue) {
		AccessQueue = accessQueue;
	}



	/**
	 * @return the qManager
	 */
	public MQQueueManager getqManager() {
		return qManager;
	}



	/**
	 * @param qManager the qManager to set
	 */
	public void setqManager(MQQueueManager qManager) {
		this.qManager = qManager;
	}



	/**
	 * @return the queue
	 */
	public MQQueue getQueue() {
		return queue;
	}



	/**
	 * @param queue the queue to set
	 */
	public void setQueue(MQQueue queue) {
		this.queue = queue;
	}

}
