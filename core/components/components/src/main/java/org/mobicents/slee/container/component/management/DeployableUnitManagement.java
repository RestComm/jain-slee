package org.mobicents.slee.container.component.management;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.management.DeployableUnitID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.deployment.DeployableUnit;

public class DeployableUnitManagement {

	private final static Logger logger = Logger
			.getLogger(DeployableUnitManagement.class);

	private final ConcurrentHashMap<DeployableUnitID, DeployableUnit> deployableUnits = new ConcurrentHashMap<DeployableUnitID, DeployableUnit>();

	/**
	 * Adds a deployable unit given its descriptor.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public void addDeployableUnit(final DeployableUnit deployableUnit) {

		if (deployableUnit == null)
			throw new NullPointerException("null deployableUnit");

		if (logger.isDebugEnabled()) {
			logger.debug("Adding DU :  " + deployableUnit.getDeployableUnitID());
		}
		
		deployableUnits.put(deployableUnit.getDeployableUnitID(),
				deployableUnit);
	}

	/**
	 * Get an array containing the deployable unit ids known to the container.
	 * 
	 * @return
	 */
	public DeployableUnitID[] getDeployableUnits() {
		Set<DeployableUnitID> deployableUnitIDs = deployableUnits
				.keySet();
		return deployableUnitIDs
				.toArray(new DeployableUnitID[deployableUnitIDs.size()]);
	}

	/**
	 * Get the deployable unit with the specified id.
	 * 
	 * @param deployableUnitID --
	 *            the deployable unit id
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit(
			DeployableUnitID deployableUnitID) {
		return deployableUnits.get(deployableUnitID);
	}

	/**
	 * Removes the deployable unit with the specified id
	 * @param deployableUnitID
	 */
	public void removeDeployableUnit(DeployableUnitID deployableUnitID) {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing DU with id:  " + deployableUnitID);
		}

		if (deployableUnitID == null)
			throw new NullPointerException("null id");

		deployableUnits.remove(deployableUnitID);
		
	}

	@Override
	public String toString() {
		return "Deployable Unit Management: " + "\n+-- Deployable Unit IDs: "
				+ deployableUnits.keySet();
	}
}
