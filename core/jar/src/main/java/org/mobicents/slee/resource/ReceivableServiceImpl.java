package org.mobicents.slee.resource;

import javax.slee.ServiceID;
import javax.slee.resource.ReceivableService;

/**
 * Implementation of the SLEE 1.1 specs {@link ReceivableService} class.
 * @author martins
 *
 */
public class ReceivableServiceImpl implements ReceivableService {

	private final ServiceID service;
	private final ReceivableEvent[] receivableEvents;
	
	public ReceivableServiceImpl(ServiceID service,ReceivableEvent[] receivableEvents) {
		this.service = service;
		this.receivableEvents = receivableEvents;
	}
	
	public ReceivableEvent[] getReceivableEvents() {
		return receivableEvents;
	}

	public ServiceID getService() {
		return service;
	}

	@Override
	public int hashCode() {
		return service.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ReceivableServiceImpl)obj).service.equals(this.service);
		}
		else {
			return false;
		}
	}
}
