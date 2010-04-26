/**
 * 
 */
package org.mobicents.slee.container.component.du;

import javax.slee.management.DeployableUnitID;

/**
 * @author martins
 *
 */
public interface DeployableUnitManagement {

	/**
	 * Adds a deployable unit.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public void addDeployableUnit(DeployableUnit deployableUnit);
	
	/**
	 * Get the deployable unit with the specified id.
	 * 
	 * @param deployableUnitID --
	 *            the deployable unit id
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit(
			DeployableUnitID deployableUnitID);

	/**
	 * Retrieves the {@link DeployableUnit} builder.
	 * @return
	 */
	public DeployableUnitBuilder getDeployableUnitBuilder();

	/**
	 * Retrieves the deployable unit descriptor factory.
	 * @return
	 */
	public DeployableUnitDescriptorFactory getDeployableUnitDescriptorFactory();
	
	/**
	 * Get an array containing the deployable unit ids known to the container.
	 * 
	 * @return
	 */
	public DeployableUnitID[] getDeployableUnits();
	
	/**
	 * Removes the deployable unit with the specified id
	 * @param deployableUnitID
	 */
	public void removeDeployableUnit(DeployableUnitID deployableUnitID);
}
