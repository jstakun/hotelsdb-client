package net.gmsworld.server.camel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.impl.StringEscapeUtils;

@ApplicationScoped
@Named("JSonArraySplitter")
@RegisterForReflection
public class JSonArraySplitter {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Handler
    public List<String> processMessage(Exchange exchange) {

        List<String> messageList = new ArrayList<String>();

        Object in = exchange.getIn().getBody();
        String inStr =new String((byte[])in);
        String json = "";
 
        if (inStr.startsWith("{")) {
        	JSONObject inJSon = new JSONObject(inStr);
        	byte[] data = Base64.decodeBase64(inJSon.getString("bytes"));
        	json = new String(data);
        } else if (inStr.startsWith("\"{")) {        	
        	try {
        		json = StringEscapeUtils.unescapeJava(inStr.substring(1, inStr.length()-1));
        	} catch (Exception e) {
        		logger.log(Level.SEVERE, "Invalid json input: " + inStr + "!");
        	}
        } else {
        	 logger.log(Level.SEVERE, "Invalid input: " + inStr + ". Expected json!");
        }
        
        JSONArray jsonArray = null;
        if (json.startsWith("[")) {
        	jsonArray = new JSONArray(json);
        } if (json.startsWith("{")) {
        	JSONObject root = new JSONObject(json);         
        	if (root.has("type") && StringUtils.equals("FeatureCollection", root.getString("type"))) {
        		logger.log(Level.INFO, "Processed FeatureCollection " + root.optString("_id"));
        	} else {
        		for (Iterator<String> iter = root.keys(); iter.hasNext();) { 
        			Object o = root.get(iter.next());
        			if (o instanceof JSONArray) {
        				jsonArray = (JSONArray) o;
        				break;
        			}
        		}
        	} 
        	
        	if (jsonArray == null) {
        		logger.log(Level.INFO, "Adding single document");
        		messageList.add(json);
        	}
        }
        
        if (jsonArray != null) {
        	logger.log(Level.INFO, "Splitting json array");
        
        	for(int i=0; i<jsonArray.length(); i++) {
        		String jsonMsg = jsonArray.get(i).toString();
        		messageList.add(jsonMsg);
        	}
        } else {
        	logger.log(Level.WARNING, "No json array available in the document");
        }
    	exchange.getIn().setHeader("DocumentCount", messageList.size());
        
        return messageList;

    }

}

