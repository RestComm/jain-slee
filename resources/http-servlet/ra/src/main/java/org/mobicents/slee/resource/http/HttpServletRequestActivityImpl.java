package org.mobicents.slee.resource.http;

import java.util.concurrent.atomic.AtomicLong;

import net.java.slee.resource.http.HttpServletRequestActivity;

public class HttpServletRequestActivityImpl implements HttpServletRequestActivity {

	private static AtomicLong idGenerator = new AtomicLong(Long.MIN_VALUE);
	
	private static Long getID() {
		long id = idGenerator.incrementAndGet();
		if (id > (Long.MAX_VALUE-10000)) {
			idGenerator.compareAndSet(id, Long.MIN_VALUE);
		}
		return Long.valueOf(id);
	}
	
	private final Object id; 
	
	public HttpServletRequestActivityImpl() {
		id = getID();
	}
	
	public HttpServletRequestActivityImpl(Object id) {
		this.id = id;
	}
	
	public Object getRequestID() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((HttpServletRequestActivityImpl)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
}
