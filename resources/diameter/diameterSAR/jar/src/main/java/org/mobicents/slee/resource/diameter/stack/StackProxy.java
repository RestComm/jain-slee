package org.mobicents.slee.resource.diameter.stack;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jdiameter.api.Configuration;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.MetaData;
import org.jdiameter.api.Mode;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;

public class StackProxy implements Stack{

	protected Stack wrapped=null;
	
	
	public StackProxy(Stack wrapped) {
		super();
		this.wrapped = wrapped;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public Logger getLogger() {
	   return this.wrapped.getLogger();
	}

	public MetaData getMetaData() {
		return this.wrapped.getMetaData();
	}

	public SessionFactory getSessionFactory() throws IllegalDiameterStateException {
		return this.wrapped.getSessionFactory();
	}

	public SessionFactory init(Configuration config) throws IllegalDiameterStateException, InternalException {
		return this.wrapped.init(config);
		}

	public boolean isActive() {
		return this.wrapped.isActive();
	}

	public void start() throws IllegalDiameterStateException, InternalException {
		// TODO Auto-generated method stub
		
	}

	public void start(Mode mode, long timeout, TimeUnit unit) throws IllegalDiameterStateException, InternalException {
		// TODO Auto-generated method stub
		
	}

	public void stop(long timeout, TimeUnit unit) throws IllegalDiameterStateException, InternalException {
		// TODO Auto-generated method stub
		
	}

	public boolean isWrapperFor(Class<?> iface) throws InternalException {
		return this.wrapped.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws InternalException {
		return this.wrapped.unwrap(iface);
	}

}
