package org.mobicents.eclipslee.servicecreation.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.tools.ant.Project;
import org.eclipse.ant.core.AntCorePlugin;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;

public class SleeProjectWizardBuilder {
	private String buildXmlPath;
	
	private IContainer container;
	
	private IWizardContainer wizardContainer;
	
	public SleeProjectWizardBuilder(IContainer container, IWizardContainer wizardContainer)
	{
		this.container = container;
		this.wizardContainer = wizardContainer;
		IProject project = container.getProject();
		
		this.buildXmlPath = project.getLocation().toOSString()
			+ "/build.xml";
	}
	public void build()
	{
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				SleeProjectAntBuilder builder = 
					new SleeProjectAntBuilder(container.getProject(), buildXmlPath);
				builder.run(monitor, false);
			}
		};
		
		try {
			wizardContainer.run(true, true, op);
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			if (realException != null)
				MessageDialog.openError(wizardContainer.getShell(), "Error", realException.getMessage());
			else
				MessageDialog.openError(wizardContainer.getShell(), "Error", e.getMessage());
		}

	}
}
