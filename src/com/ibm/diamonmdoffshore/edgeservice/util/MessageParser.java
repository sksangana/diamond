/**
 * 
 */
package com.ibm.diamonmdoffshore.edgeservice.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	
	private static final Logger logger = Logger.getLogger(MessageParser.class.getName());

	/**
	 * 
	 */
	public MessageParser() {
		// TODO Auto-generated constructor stub
	}

	
	public String getMessageType(String msg) {
		
		String msgType = null;
		
		JSONParser parser = new JSONParser();
        JSONArray jsonarray = null;
        JSONObject 
        obj = null;
        
		try {
			obj = (JSONObject)parser.parse(msg);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			msgType = getEventType(obj);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MyEventException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			msgType = e.getMessage();
		}
		System.out.println("Event Type Parsed is .." + msgType);
		return msgType;
		
	}
	
	@SuppressWarnings("unused")
	private String getEventType(JSONObject jsonObj) throws ParseException, MyEventException {
		String eventType = null;
		
		@SuppressWarnings("unchecked")
		Set<Object> set = jsonObj.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (jsonObj.get(obj) instanceof JSONArray) {
                // Skip
            	} else {
                if (jsonObj.get(obj) instanceof JSONObject) {
                	getEventType((JSONObject) jsonObj.get(obj));
                } else {
                	if(obj.toString().equalsIgnoreCase("EventType")) {
                		System.out.println("Event Type Found is... " + jsonObj.get(obj));
                		eventType = (String) jsonObj.get(obj);
                		throw new MyEventException(eventType);
                	}
                   
                }
            }
        }
		
		return eventType;
	}
	
	/*
	 * Parse the JSON object and create a Map
	 */
	public HashMap getMapFromJsonObject(JSONObject jsonObj) {
		
		HashMap<String, Object> jsonObjMap = new HashMap<String, Object>();
		
        Set<Object> set = jsonObj.keySet();
        Iterator<Object> iterator = set.iterator();
	    System.out.println("Fields:");
	    while (iterator.hasNext() ){
	    	Object object = iterator.next();
	    	System.out.println("Object is .." + object.toString());
	       if(jsonObj.get(object) instanceof JSONObject) {
	    	   JSONObject json = (JSONObject) jsonObj.get(object);
	    	   System.out.println("JSON Object is .." + json.toJSONString());
	    	   set = json.keySet();
	           iterator = set.iterator();
	           while (iterator.hasNext()) {
	               Object obj = iterator.next();
	                		//System.out.println("Event Type Found is... " + obj.toString());
	                		//System.out.println("Field Value iss .." + json.get(obj));
	                		jsonObjMap.put(obj.toString(), json.get(obj));
	           }
	       }
	    }
		return jsonObjMap;
	}
	
	public LinkedHashMap<String, Object> prepareSDIColumsMap(HashMap map) {

		LinkedHashMap<String, Object> newMap = new LinkedHashMap<>();

		Iterator entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			if (key.equalsIgnoreCase("RigId")) {
				
				Object value = entry.getValue();
				newMap.put("id", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("name")) {
				
				Object value = entry.getValue();
				newMap.put("bob", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("Timestamp")) {
				
				Object value = entry.getValue();
				newMap.put("ts", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("hr1_set_present_005")) {
				
				Object value = entry.getValue();
				newMap.put(key, value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("MP1Speed")) {
				
				Object value = entry.getValue();
				newMap.put("mp1_ai_002", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("MP2Speed")) {
				
				Object value = entry.getValue();
				newMap.put("mp2_ai_002", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("MP3Speed")) {
				
				Object value = entry.getValue();
				newMap.put("mp3_ai_002", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
			if (key.equalsIgnoreCase("MP4Speed")) {
				
				Object value = entry.getValue();
				newMap.put("mp4_ai_002", value);
				System.out.println("Key = " + key + ", Value = " + value);
			}
		}

		System.out.println("The prepSDIColumnsMap size is ..." + newMap.size());
		return newMap;

	}
	
	 @SuppressWarnings("unchecked")
	public static void parseJson(JSONObject jsonObject) throws ParseException {

	        Set<Object> set = jsonObject.keySet();
	        Iterator<Object> iterator = set.iterator();
	        while (iterator.hasNext()) {
	            Object obj = iterator.next();
	            if (jsonObject.get(obj) instanceof JSONArray) {
	                System.out.println(obj.toString());
	                getArray(jsonObject.get(obj));
	            } else {
	                if (jsonObject.get(obj) instanceof JSONObject) {
	                    parseJson((JSONObject) jsonObject.get(obj));
	                } else {
	                	if(obj.toString().equalsIgnoreCase("EventType")) {
	                		System.out.println("Event Type Found is... " + jsonObject.get(obj));
	                	}
	                    System.out.println(obj.toString() + "\t"
	                            + jsonObject.get(obj));
	                }
	            }
	        }
	    }

	    
	    public static void getArray(Object object2) throws ParseException {

	        JSONArray jsonArr = (JSONArray) object2;

	        for (int k = 0; k < jsonArr.size(); k++) {

	            if (jsonArr.get(k) instanceof JSONObject) {
	                parseJson((JSONObject) jsonArr.get(k));
	            } else {
	                System.out.println(jsonArr.get(k));
	            }

	        }
	    }
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		
		
		

        //Write JSON file
//        try (FileWriter file = new FileWriter("employees.json")) {
// 
//            file.write(employeeList.toJSONString());
//            file.flush();
// 
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
	}
	
	class MyEventException extends Exception {
		
		public MyEventException() {
			
		}
		
		public MyEventException(String msg) {
			super(msg);
		}
	}

}
	
