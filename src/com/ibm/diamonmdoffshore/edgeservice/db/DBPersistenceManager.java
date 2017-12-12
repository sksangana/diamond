/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.ibm.diamonmdoffshore.edgeservice.util.MessageParser;

/**
 * @author sanganas
 *
 */
public class DBPersistenceManager {

	/**
	 * 
	 */
	public DBPersistenceManager() {
		// TODO Auto-generated constructor stub
	}
	

	public void insertDrillOpsMsg(JSONObject obj, Connection conn) throws SQLException, NumberFormatException, ParseException {
		
		MessageParser parser = new MessageParser();
		HashMap jsonMap = parser.getMapFromJsonObject(obj);
				
		String procedureSql = "execute procedure sp_drillops_ins(?, ?, ?, ?)";
		PreparedStatement ps;
		ps = conn.prepareStatement(procedureSql);
		
		String ts = (String)jsonMap.get("Timestamp");
		String rigId = (String)jsonMap.get("RigId");
		Timestamp timestamp = null;
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate = (Date) dateFormat.parse(ts);
		    System.out.println("Parsed Date ..." + parsedDate);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    System.out.println("Parsed Time Stamp is ..." + timestamp);
		    
		    //Now add extra 1 second to the timestamp.
		    //Timestamp ts_from_ws = new Timestamp(parsedDate);
		    Instant nextSec = timestamp.toInstant().plusSeconds(1);
		    timestamp = Timestamp.from(nextSec);
		    
		    System.out.println("New Time Stamp is ..." + timestamp);


		    
		String sensorName = null;
		Long sensorValue = null;
		Iterator entries = jsonMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			sensorName = (String) entry.getKey();
			//System.out.println("In DB Persistence Sensor Name .." + sensorName);
			if ("TimeStamp".equalsIgnoreCase(sensorName) || "EventType".equalsIgnoreCase(sensorName) || "RigId".equalsIgnoreCase(sensorName)) {
				//skip
				entries.next();	
			} else {
				
				sensorValue = (Long) entry.getValue();
				
				//Now prepare sql to execute
//				String sql = "INSERT INTO  rig_drillops_vt (id, ts, value) " +
//								"SELECT r.id,  '" + timestamp + "', "  + sensorValue.floatValue() + 
//									" FROM rig r, sensor s " +
//										"WHERE r.id = " + rigId + " AND LOWER(s.edge) = LOWER('" + sensorName + "'); ";
//	            System.out.println("Executing SQL Statement :" + sql);
				ps.setString(1, rigId);
				ps.setString(2, sensorName);
				ps.setTimestamp(3, timestamp);
				ps.setFloat(4, sensorValue);
				ResultSet rs = ps.executeQuery();
				
//				String sql = "execute procedure sp_drillops_ins(" + rigId +", '"+ sensorName + "', "+
//								"'" + timestamp +"'::DATETIME YEAR TO FRACTION(5)"+ ", "+ sensorValue + ")";
//				System.out.println("Executing SQL stmt .." + sql);
				//ResultSet rs= stmt.executeQuery(sql);
			   rs.next();
	           System.out.println("Execute update returned code .." + rs.getInt(1));
	           if(rs.getInt(1) == -1) {
	            	System.out.println("SQL ERROR : Insert unsuccessful.  Invalid sensorName :" + sensorName);
	            	throw new SQLException("SQL ERROR : Insert unsuccessful.  Invalid sensorName :" + sensorName);
	            }

			}
			
		}
		
		
	}
	
	public void testingInserts() {
		
		DBConnectionManager dbManager = new DBConnectionManager();
    	try {
		
		Connection conn = dbManager.getDBConnection();
		
    	String     cmd=null;

    	Statement  stmt = null;
//    	"INSERT INTO Registration " +
//        "VALUES (100, 'Zara', 'Ali', 18)";
			stmt = conn.createStatement();
			cmd = "INSERT INTO customer " + "VALUES (1001, 'Santosh', 'Sangana', 'IBM', '11200 Savin hill', '', 'Austin', 'TX', '78739', '5555555555')";
			int rc = stmt.executeUpdate(cmd);
			System.out.println("Inserted into Informix successfully ...");
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String msg = e.getMessage() + "  " + e.getCause() + "  " + e.getErrorCode() + "  " + e.getSQLState();
			System.out.println("ERROR:  SQL Exception : " + msg);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DBPersistenceManager dbManager = new DBPersistenceManager();
		
		dbManager.testingInserts();

	}

}
