package org.rhq.plugins.jslee.utils;

import org.rhq.core.pluginapi.inventory.ResourceComponent;

public interface JainSleeServerUtils<T extends ResourceComponent> extends ResourceComponent<T> {
	public MBeanServerUtils getMBeanServerUtils();

}
