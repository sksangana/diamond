/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.diamonmdoffshore.edgeservice.util.PropertiesManager;

/**
 * @author sanganas
 *
 */
public class DBConnectionManager {
	
	private static final Logger logger = Logger.getLogger(DBConnectionManager.class.getName());

	/**
	 * 
	 */
	public DBConnectionManager() {
		// TODO Auto-generated constructor stub
		
		loadJDBCDriver();
	}
	
	private void loadJDBCDriver() {
		
		try {
			Class.forName("com.informix.jdbc.IfxDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getDBConnection() throws SQLException {
		
		Connection connection = null;
		
		String jdbcURL = getInformixDBURL();
		
		try {
			connection = DriverManager.getConnection(jdbcURL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//logger.log(Level.SEVERE, "Cannot get Connection from the DRiverManager .." + e.getSQLState() + "Erorrcode .." + e.getErrorCode());
			
			//System.out.println("ERROR: " + "Cannot get Connection from the DRiverManager .." + e.getSQLState() + "Erorrcode .." + e.getErrorCode());
			throw new SQLException(e);
		}
		
		return connection;
		
	}
	
	public void closeDBConnection(Connection conn) {

		try
		{
			conn.close();
		}
		catch (SQLException e)
		{			System.out.println("ERROR: failed to close the connection!");
			e.printStackTrace();
			
		}

	}
	
	private String getInformixDBURL() {
		
		String url = null;
		
	//	jdbc:informix-sqli://{<ip-address>|<host-name>}:<port-number>[/<dbname>]:INFORMIXSERVER=<server-name>;user=<username>;password=<password>

		PropertiesManager propManager = PropertiesManager.getPropertiesManager();
		
		if(propManager != null) {
			url = "jdbc:informix-sqli://" + propManager.getTIMESERIESDB_HOST() + ":" + propManager.getTIMESERIESDB_PORT() +
					      "/" + propManager.getTIMESERIESDB_NAME() + ":INFORMIX=" + propManager.getTIMESERIESDB_SERVERNAME() +
					      ";user=" + propManager.getTIMESERIESDB_USER() + ";password=" + propManager.getTIMESERIESDB_PASSWORD();

		}
		return url;
		
	}

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		DBConnectionManager dbManager = new DBConnectionManager();
		
		String url = dbManager.getInformixDBURL();
		
		Connection conn = dbManager.getDBConnection();
		
		String schema = null;
		DatabaseMetaData metaData = null;
		try {
			metaData = conn.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> columns = new ArrayList<String>();
		
		String sql ="SELECT c.colname AS columns FROM \"informix\".systables AS t, \"informix\".syscolumns AS c WHERE t.tabname = 'rig_sdi_main_vt' AND t.tabid = c.tabid;";
		
		String cols = "id,name,ts,hr1_set_present_005,mp1_ai_002,mp2_ai_002,mp3_ai_002,mp4_ai_002,sdi_ai_001,sdi_ai_003,sdi_ai_007,sdi_ai_008,sdi_ai_010,sdi_ai_020,sdi_ai_021,sdi_ai_022,sdi_ai_023,sdi_calc_000,sdi_calc_002,sdi_calc_005,sdi_calc_006,sdi_calc_007,sdi_calc_008,sdi_calc_016,sdi_calc_031,sdi_set_present_064,sdi_set_present_072,sdi_set_present_088,sdi_tgl_status_030";


		   Statement stmt = conn.createStatement();
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			ResultSet rs = stmt.executeQuery(sql);        
		while (rs.next()) {
			String colname = rs.getString("columns");
            System.out.print("," + colname);
			columns.add(colname);

		}
		dbManager.closeDBConnection(conn);

		System.out.println("Meta data is ..." + metaData.getDatabaseProductName());
		System.out.println("JDBC URL is .." + url);
		System.out.println("The columns are .." + columns.size());

	}

}
