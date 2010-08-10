package org.rhq.plugins.jslee.utils;

import javax.slee.resource.ResourceAdaptorID;

import org.rhq.core.pluginapi.inventory.ResourceComponent;

public interface ResourceAdaptorUtils<T extends ResourceComponent> extends ResourceComponent<T>, JainSleeServerUtils<T> {
	public ResourceAdaptorID getResourceAdaptorID();
}
