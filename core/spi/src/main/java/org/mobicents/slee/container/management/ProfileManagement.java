/**
 * 
 */
package org.mobicents.slee.container.management;

import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.profile.UnrecognizedProfileSpecificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.profile.ProfileTable;

/**
 * @author martins
 * 
 */
public interface ProfileManagement extends SleeContainerModule {

	/**
	 * 
	 * @return
	 */
	public ProfileFacility getProfileFacility();

	/**
	 * 
	 * @param profileTableName
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedProfileTableNameException
	 */
	public ProfileTable getProfileTable(String profileTableName) throws NullPointerException, UnrecognizedProfileTableNameException;

	/**
	 * 
	 * @return
	 */
	public ProfileTableActivityContextInterfaceFactory getProfileTableActivityContextInterfaceFactory();
	
	/**
	 * Installs profile into container, creates default profile and reads
	 * backend storage to search for other profiles - it creates MBeans for all
	 * 
	 * @param component
	 * @throws DeploymentException
	 *             - this exception is thrown in case of error FIXME: on check
	 *             to profile - if we have one profile table active and we
	 *             encounter another, what shoudl happen? is there auto init for
	 *             all in back end memory?
	 */
	public void installProfileSpecification(
			ProfileSpecificationComponent component) throws DeploymentException;

	/**
	 * 
	 * @param component
	 * @throws UnrecognizedProfileSpecificationException
	 */
	public void uninstallProfileSpecification(
			ProfileSpecificationComponent component)
			throws UnrecognizedProfileSpecificationException;

}
