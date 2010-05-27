package org.mobicents.slee.container.rmi;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.jboss.invocation.MarshalledInvocation;
import org.jboss.logging.Logger;

/**
 * 
 * @author amit.bhayani
 *
 */
public class RMIServerImpl implements RMIServer {

	protected Object handler;

	protected Map invokerMap = new HashMap();

	protected org.jboss.logging.Logger log;

	protected RemoteStub rmistub;

	protected Object stub;

	protected String key;

	protected Class intf;

	public RMIServerImpl(String replicantName, Class intf, Object handler)
			throws Exception {

		this.handler = handler;
		this.log = Logger.getLogger(this.getClass());
		this.intf = intf;

		//this.key = "/" + replicantName;
		this.key =replicantName;

		// Obtain the hashes for the supported handler interfaces
		Class[] ifaces = handler.getClass().getInterfaces();
		for (int i = 0; i < ifaces.length; i++) {
			Map tmp = MarshalledInvocation.methodToHashesMap(ifaces[i]);
			invokerMap.putAll(tmp);
		}

		this.rmistub = (RemoteStub) UnicastRemoteObject.exportObject(this, 0,
				null, null);// casting is necessary because interface has
		// changed in JDK>=1.2

		RMIServer.rmiServers.put(key, this);

	}

	public Object createStub() {
		RMIClient client = new RMIClient(handler, this, key);

		return Proxy.newProxyInstance(intf.getClassLoader(), new Class[] {
				intf, RMIProxy.class }, client);
	}

	public RMIResponse invoke(MarshalledInvocation mi) throws Exception {

		mi.setMethodMap(invokerMap);
		Method method = mi.getMethod();

		RMIResponse rsp = new RMIResponse();
		rsp.response = method.invoke(handler, mi.getArguments());
		return rsp;
	}

	public void destroy() {
		try {
			RMIServer.rmiServers.remove(key);
			UnicastRemoteObject.unexportObject(this, true);
		} catch (Exception e) {
			log.error("failed to destroy", e);
		}
	}

	public Object getLocal() throws Exception {
		return handler;
	}
	
	


}
