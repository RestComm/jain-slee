package org.mobicents.slee.resource.map;

import javax.slee.Address;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPResourceAdaptor implements ResourceAdaptor {

	public MAPResourceAdaptor() {
		// TODO Auto-generated constructor stub
	}

	public void activityEnded(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityHandle getActivityHandle(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getResourceAdaptorInterface(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void raActive() {
		// TODO Auto-generated method stub

	}

	public void raConfigurationUpdate(ConfigProperties arg0) {
		// TODO Auto-generated method stub

	}

	public void raConfigure(ConfigProperties arg0) {
		// TODO Auto-generated method stub

	}

	public void raInactive() {
		// TODO Auto-generated method stub

	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		// TODO Auto-generated method stub

	}

	public void raVerifyConfiguration(ConfigProperties arg0) throws InvalidConfigurationException {
		// TODO Auto-generated method stub

	}

	public void serviceActive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceInactive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceStopping(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void setResourceAdaptorContext(ResourceAdaptorContext arg0) {
		// TODO Auto-generated method stub

	}

	public void unsetResourceAdaptorContext() {
		// TODO Auto-generated method stub

	}

}
