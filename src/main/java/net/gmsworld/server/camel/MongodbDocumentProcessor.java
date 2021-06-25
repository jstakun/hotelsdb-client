package net.gmsworld.server.camel;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("MongodbJSonProcessor")
@RegisterForReflection
public class MongodbDocumentProcessor implements Processor {

	private static final Logger LOG = Logger.getLogger(MongodbDocumentProcessor.class.getName());
	
	@Override
	public void process(Exchange exchange) throws Exception {
			Object in = exchange.getIn().getBody();
			try {
				 JSONArray arr = new JSONArray(new String((byte[])in));
				 JSONArray out = new JSONArray();
				 for (int i=0;i<arr.length();i++) {
					  JSONObject record = arr.getJSONObject(i);
					  LOG.info(record.toString());
					  final String dataBase64  = record.getJSONObject("bytes").getString("data");
					  byte[] data = Base64.decodeBase64(dataBase64);
				      out.put(new JSONObject(new String(data)));
				 }
				 exchange.getIn().setBody(out.toString());
				 LOG.info("Processed " + arr.length() + " documents.");
			} catch (Exception e) {
				 e.printStackTrace();
				 exchange.getIn().setBody("[]");
			}
	}

}
