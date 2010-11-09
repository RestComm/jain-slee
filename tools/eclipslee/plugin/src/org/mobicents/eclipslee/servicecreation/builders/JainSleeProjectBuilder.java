package org.mobicents.eclipslee.servicecreation.builders;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.mobicents.eclipslee.servicecreation.util.SleeProjectAntBuilder;

public class JainSleeProjectBuilder extends IncrementalProjectBuilder {
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
	throws CoreException {
		SleeProjectAntBuilder builder = 
			new SleeProjectAntBuilder(this.getProject());
		try
		{
			boolean log = kind != IncrementalProjectBuilder.AUTO_BUILD;
			builder.run(monitor, log);
		}
		catch(Exception ex)
		{

		}
		return null;
	}
	protected void startupOnInitialize() {
		
	}
	protected void clean(IProgressMonitor monitor) {
		SleeProjectAntBuilder builder = 
			new SleeProjectAntBuilder(this.getProject());
		try
		{
			builder.run(monitor, false, new String[] {"clean"});
		}
		catch(Exception ex)
		{

		}
	}
}

