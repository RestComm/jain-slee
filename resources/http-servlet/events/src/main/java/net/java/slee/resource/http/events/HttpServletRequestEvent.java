package net.java.slee.resource.http.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * A wrapper event around HttpServletRequest. 
 * 
 * @author Ivelin Ivanov
 *
 */
public interface HttpServletRequestEvent {
	
	/**
	 * 
	 * @return the HttpServletRequest which is associated with the event. There is always one.
	 */
	public HttpServletRequest getRequest(); 
	
	/**
	 * 
	 * @return the HttpServletResponse which is associated with the request. There is always one.
	 */
    public HttpServletResponse getResponse();
	
    /**
     * Returns an event ID, unique in respect to the VM where it was generated 
     */
    public String getId();

}
