package org.mobicents.slee.annotations.examples;

import org.mobicents.slee.annotations.UsageParameter;

public interface ExampleUsageParametersInterface {

	@UsageParameter
	public void incrementX();
	
	@UsageParameter(notificationsEnabled=true)
	public void incrementY();
	
}
