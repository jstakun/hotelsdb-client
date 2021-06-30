package net.gmsworld.server.camel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("UtilsBean")
@RegisterForReflection
public class UtilBean {
	public long getTimestamp() {
		   return System.currentTimeMillis();
	}
}
