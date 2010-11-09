package org.mobicents.eclipslee.servicecreation.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class SleeProjectAntBuilder {
	
	private IProject project;
	private String buildXmlPath;
	
	public SleeProjectAntBuilder(IProject project)
	{
		this.project = project;
		this.buildXmlPath = project.getLocation().toOSString()
			+ "/build.xml";;
	}
	
	public SleeProjectAntBuilder(IProject project, String buildXmlPath)
	{
		this.project = project;
		this.buildXmlPath = buildXmlPath;
	}
	
	public void run(IProgressMonitor monitor, boolean logToConsole, String[] targets) throws InvocationTargetException {
		try {
			AntRunner antRunner = new AntRunner();
			if( targets != null ) antRunner.setExecutionTargets(targets);
			antRunner.setBuildFileLocation(buildXmlPath);
			monitor.setTaskName("Building SLEE project");
			antRunner.run(monitor);
			if(logToConsole) antRunner.addBuildLogger("org.eclipse.ui.externaltools.internal.ant.logger.AntProcessBuildLogger");
			monitor.setTaskName("Refreshing available SLEE components");

			// Refresh the jars after a rebuild
			project.getFolder("jars").refreshLocal(IResource.DEPTH_INFINITE, null);
			
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		} finally {
			monitor.done();
		}
	}
	
	public void run(IProgressMonitor monitor, boolean logToConsole) throws InvocationTargetException {
		run(monitor, logToConsole, null);
	}
}
