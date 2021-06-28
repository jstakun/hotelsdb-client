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
import org.apache.camel.Message;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import io.quarkus.runtime.annotations.RegisterForReflection;

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
        } else {
        	 logger.log(Level.SEVERE, "Invalid input: " + inStr + ". Expected json!");
        }
        
        JSONArray jsonArray = null;
        if (json.startsWith("[")) {
        	jsonArray = new JSONArray(json);
        } if (json.startsWith("{")) {
        	JSONObject root = new JSONObject(json);         
        	for (Iterator<String> iter = root.keys(); iter.hasNext();) { 
        		Object o = root.get(iter.next());
        		if (o instanceof JSONArray) {
        			jsonArray = (JSONArray) o;
        			break;
        		}
        	}
        	
        	if (jsonArray == null) {
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

