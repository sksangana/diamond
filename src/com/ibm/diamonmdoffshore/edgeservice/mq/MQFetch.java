/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.mq;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.constants.MQConstants;
import com.ibm.msg.client.wmq.compat.base.internal.MQC;

/**
 * @author sanganas
 *
 */
public class MQFetch {

	private static final Logger logger = Logger.getLogger(MQFetch.class.getName());
	
	private int Queue_Depth;
	
	
	/**
	 * 
	 */
	public MQFetch() {
		// TODO Auto-generated constructor stub
	}
	
	public String getMessage() throws MQException{
		
		String msgText = null;
		
		MQConnectionManager qManager = MQConnectionManager.getMQConnectionManager();
		MQQueue queue = qManager.getAccessQueue();
		
		
		
		try {
			int qDepth = queue.getCurrentDepth();
			
		} catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean thereAreMessages = true;
		//while (thereAreMessages) {
			MQMessage rcvMessage = new MQMessage();

			// Specify default get message options
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT  ;
			//gmo.options = MQC.MQGMO_WAIT;
			// gmo.matchOptions=MQC.MQMO_NONE;
			gmo.waitInterval = 5000;
			//gmo.waitInterval=MQConstants.MQWI_UNLIMITED;

			// Get the message off the queue.
				queue.get(rcvMessage, gmo);
				logger.log(Level.INFO, "Successfully getting the message ..");

			try {
				msgText = rcvMessage.readUTF();//
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("The message is: " + msgText);
		//}
		
		return msgText;
	}

	/**
	 * @return the queue_Depth
	 */
	public int getQueue_Depth() {
		MQConnectionManager qManager = new MQConnectionManager();
		MQQueue queue = qManager.getAccessQueue();
		try {
			Queue_Depth =  queue.getCurrentDepth();
		} catch (MQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Queue_Depth;
	}

	/**
	 * @param queue_Depth the queue_Depth to set
	 */
	public void setQueue_Depth(int queue_Depth) {
		Queue_Depth = queue_Depth;
	}

}
