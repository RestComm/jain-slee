package org.mobicents.slee;

import javax.slee.SbbID;
import javax.slee.ServiceID;

/**
 * An event indicating an update in a Service's SBB configuration, fired in the service activity.
 * @author Eduardo Martins
 *
 */
public interface ServiceConfigurationUpdateEvent {

	/**
	 * the event type name
	 */
	public static final String EVENT_TYPE_NAME = "org.mobicents.slee.ServiceConfigurationUpdateEvent";
	
	/**
	 * the event type vendor
	 */
	public static final String EVENT_TYPE_VENDOR = "org.mobicents.slee";
	
	/**
	 * the event type version
	 */
	public static final String EVENT_TYPE_VERSION = "1.1";
	
	/**
	 * Retrieves the ID of the Sbb which configuration was updated.
	 * @return
	 */
	public ConfigProperties getConfigProperties();
	
	/**
	 * Retrieves the ID of the Service's Sbb which configuration was updated.
	 * @return
	 */
	public SbbID getSbbID();	
	
	/**
	 * Retrieves the ID of the Service which configuration was updated.
	 * @return
	 */
	public ServiceID getServiceID();
	
}
