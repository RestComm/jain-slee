package org.mobicents.resources.sip.tests.sipUnit.events;

import java.io.Serializable;

import javax.sip.address.Address;
import javax.slee.resource.ActivityHandle;

public class SuperviseActivitiesEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Address fromAddress,toAddress,conAddress,routeAddress;
	protected String name;
	protected ActivityHandle sipActivityHandle;
	public Address getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}
	public Address getToAddress() {
		return toAddress;
	}
	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}
	public Address getConAddress() {
		return conAddress;
	}
	public void setConAddress(Address conAddress) {
		this.conAddress = conAddress;
	}
	public Address getRouteAddress() {
		return routeAddress;
	}
	public void setRouteAddress(Address routeAddress) {
		this.routeAddress = routeAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ActivityHandle getSipActivityHandle() {
		return sipActivityHandle;
	}
	public void setSipActivityHandle(ActivityHandle sipActivityHandle) {
		this.sipActivityHandle = sipActivityHandle;
	}
	public SuperviseActivitiesEvent(Address fromAddress, Address toAddress,
			Address conAddress, Address routeAddress, String name,
			ActivityHandle sipActivityHandle) {
		super();
		this.fromAddress = fromAddress;
		this.toAddress = toAddress;
		this.conAddress = conAddress;
		this.routeAddress = routeAddress;
		this.name = name;
		this.sipActivityHandle = sipActivityHandle;
	}
	public SuperviseActivitiesEvent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
