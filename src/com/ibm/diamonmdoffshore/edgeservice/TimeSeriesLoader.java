/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.diamonmdoffshore.edgeservice.db.DBConnectionManager;
import com.ibm.diamonmdoffshore.edgeservice.db.DBPersistenceManager;
import com.ibm.diamonmdoffshore.edgeservice.mq.MQConnectionManager;
import com.ibm.diamonmdoffshore.edgeservice.mq.MQFetch;
import com.ibm.diamonmdoffshore.edgeservice.util.MessageParser;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;

/**
 * @author sanganas
 *
 */
public class TimeSeriesLoader {

	private static final Logger logger = Logger.getLogger(TimeSeriesLoader.class.getName());

	private static List<JSONObject> DrillOpsList  = new ArrayList<JSONObject>();
	private static List<JSONObject> RimmDrillList = new ArrayList<JSONObject>();;
	private static List<JSONObject> SysMonList = new ArrayList<JSONObject>();;


	/**
	 * 
	 */
	public TimeSeriesLoader() {
		// TODO Auto-generated constructor stub
	}

	public static void addToDrillOpsList(JSONObject obj) {

		DrillOpsList.add(obj);
	}

	public static void addToRimmDrillList(JSONObject obj) {

		RimmDrillList.add(obj);
	}

	public static void addToSysMonList(JSONObject obj) {

		SysMonList.add(obj);
	}

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logger.info("Starting the Time Series Loader ...");

		MQConnectionManager qManager = MQConnectionManager.getMQConnectionManager();
		MQQueue queue = qManager.getAccessQueue();

		logger.log(Level.INFO, "Connected to the MQ QueueManager ..." + qManager.getQueueManagerName());
		logger.log(Level.INFO, "Connected to Queue ..." + qManager.getQueueName());

		/*
		 * Begin the transaction The idea here is to put Reading a message from
		 * the Queue and Parsing the message and persisting in to the Time Store
		 * Database is considered as one transaction
		 */

		try {
			boolean thereAreMessages = true;
//			while (thereAreMessages) {

				try {
					//qManager.getqManager().begin();
					MQFetch mqReader = new MQFetch();
					// int qDepth = queue.getCurrentDepth();
					String msgText = null;
					msgText = mqReader.getMessage();
					System.out.println("Message Fetched is ..." + msgText);
					if (msgText != null) {

						logger.log(Level.INFO, "Message Fetched is .." + msgText);

						JSONParser parser = new JSONParser();
						JSONObject obj = null;
						//try {
							obj = (JSONObject) parser.parse(msgText);
						//} 
						MessageParser msgParser = new MessageParser();
						String eventType = msgParser.getMessageType(msgText);
						System.out.println("Event Type is ..." + eventType);
						if (eventType.equalsIgnoreCase("DrillOps")) {
							addToDrillOpsList(obj);
						}
						if (eventType.equalsIgnoreCase("RimDrill")) {
							addToRimmDrillList(obj);
						}
						if (eventType.equalsIgnoreCase("MQ")) {
							addToSysMonList(obj);
						}


						DBConnectionManager dbManager = new DBConnectionManager();
						Connection conn = dbManager.getDBConnection();
						DBPersistenceManager dbPersistManager = new DBPersistenceManager();
                        conn.setAutoCommit(false);
						dbPersistManager.insertDrillOpsMsg(obj, conn);
						
						System.out.println("Inserted Successfully ...");
						conn.commit();
						dbManager.closeDBConnection(conn);
						qManager.getqManager().commit();
					}
				} 
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					String msg = "ERROR : Data Parsing : "  + e1.getMessage() + " " + e1.getCause();
					logger.log(Level.SEVERE, msg);;
				}catch (MQException e) {
					e.printStackTrace();
					if (e.reasonCode == e.MQRC_NO_MSG_AVAILABLE) {
						System.out.println("no more message available or retrived");
						thereAreMessages = false;

					} else {
						String msg = "ERROR : Message Queue Error " + e.getMessage() + " " + e.getErrorCode() + " " + e.getReason();
						logger.log(Level.SEVERE, msg);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					String msg = e.getMessage() + "  " + e.getCause() + "  " + e.getErrorCode() + "  " + e.getSQLState();
					e.printStackTrace();
//					System.out.println("Inser failed Cause is .." + e.getCause());
//					System.out.println("Messages is ..." + e.getMessage());
//					System.out.println("Erro code is ..." + e.getErrorCode());
//					System.out.println("SQL state is ..." + e.getSQLState());
					System.out.println("ERROR:  SQL Exception : " + msg);
					try {
						qManager.getqManager().backout();
					} catch (MQException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("MQ backout failed .." + e.getMessage());
					}
				} 
//			} 
		} finally {
			// TODO: handle finally clause
			try {
				qManager.getqManager().disconnect();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("The Total Objects in Drill Ops List are ... " + DrillOpsList.size());
		System.out.println("The Total Objects in RimmDrill List are ... " + RimmDrillList.size());
		System.out.println("The Total Objects in SysMon List are ... " + SysMonList.size());
	
	}
}
