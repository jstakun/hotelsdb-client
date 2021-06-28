package net.gmsworld.server.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("MathProcessor")
@RegisterForReflection
public class MathProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		long distanceLimit = Long.parseLong((String) exchange.getIn().getHeader("distance"));
		exchange.getIn().setHeader("normalizedDistance", (double) ((distanceLimit/1000) / 6378.137d)); 
	}

}
