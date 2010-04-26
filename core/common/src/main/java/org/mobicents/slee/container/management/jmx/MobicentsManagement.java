package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class MobicentsManagement extends ServiceMBeanSupport implements
		MobicentsManagementMBean {

	// Number of minutes after lingering entities of inactive service will be
	// removed.
	public static double entitiesRemovalDelay = 1;

	// mobicents version
	private String mobicentsVersion = Version.instance.toString();

	public double getEntitiesRemovalDelay() {
		return MobicentsManagement.entitiesRemovalDelay;
	}

	public void setEntitiesRemovalDelay(double entitiesRemovalDelay) {
		MobicentsManagement.entitiesRemovalDelay = entitiesRemovalDelay;
	}

	public String getVersion() {
		return mobicentsVersion;
	}

	public String dumpState() throws ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txManager = sleeContainer
				.getTransactionManager();
		try {
			txManager.begin();
			return sleeContainer.dumpState();
		} catch (Exception e) {
			throw new ManagementException("Failed to get container state", e);
		} finally {
			try {
				txManager.commit();
			} catch (Exception e) {
				throw new ManagementException("Failed to get container state",
						e);
			}
		}
	}

}
