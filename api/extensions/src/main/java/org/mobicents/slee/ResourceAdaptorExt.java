package org.mobicents.slee;

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

public class ResourceAdaptorExt implements ResourceAdaptor {

	@Override
	public void activityEnded(ActivityHandle handle) {
		
	}

	@Override
	public void activityUnreferenced(ActivityHandle handle) {
		
	}

	@Override
	public void administrativeRemove(ActivityHandle handle) {
		
	}

	@Override
	public void eventProcessingFailed(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags, FailureReason reason) {
		
	}

	@Override
	public void eventProcessingSuccessful(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags) {
		
	}

	@Override
	public void eventUnreferenced(ActivityHandle handle,
			FireableEventType eventType, Object event, Address address,
			ReceivableService service, int flags) {
		
	}

	@Override
	public Object getActivity(ActivityHandle handle) {
		return null;
	}

	@Override
	public ActivityHandle getActivityHandle(Object activity) {
		return null;
	}

	@Override
	public Marshaler getMarshaler() {
		return null;
	}

	@Override
	public Object getResourceAdaptorInterface(String className) {
		return null;
	}

	@Override
	public void queryLiveness(ActivityHandle handle) {
		
	}

	@Override
	public void raActive() {
		
	}

	@Override
	public void raConfigurationUpdate(ConfigProperties properties) {
		
	}

	@Override
	public void raConfigure(ConfigProperties properties) {
		
	}

	@Override
	public void raInactive() {
		
	}

	@Override
	public void raStopping() {
		
	}

	@Override
	public void raUnconfigure() {
		
	}

	@Override
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		
	}

	@Override
	public void serviceActive(ReceivableService serviceInfo) {
		
	}

	@Override
	public void serviceInactive(ReceivableService serviceInfo) {
		
	}

	@Override
	public void serviceStopping(ReceivableService serviceInfo) {
		
	}

	@Override
	public void setResourceAdaptorContext(ResourceAdaptorContext context) {
		
	}

	@Override
	public void unsetResourceAdaptorContext() {
		
	}

}
