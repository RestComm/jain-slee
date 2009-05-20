package org.mobicents.slee.container.profile;

/**
 * The abstract base class for the Profile CMP Slee 1.0 Wrapper, an object that implements the Profile CMP
 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
 * compatible interface.
 * 
 * @author martins
 *
 */
public abstract class AbstractProfileCmpSlee10Wrapper {

	/**
	 * 
	 */
	protected ProfileObject profileObject;
	
	/**
	 * 
	 * @param profileObject
	 */
	public AbstractProfileCmpSlee10Wrapper(ProfileObject profileObject) {
		this.profileObject = profileObject;
	}
	
}
