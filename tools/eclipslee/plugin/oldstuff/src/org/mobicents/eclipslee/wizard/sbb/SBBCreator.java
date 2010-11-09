/*
 * Created on 30-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.eclipslee.wizard.sbb;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogSettings;
import org.mobicents.eclipslee.MainPlugin;

/**
 * @author Paolo Costa
 * paolo_cst@yahoo.it
 *
 * This class is a subclass of DialogSetting. It implements methods to create the SBB skeleton
 */
public class SBBCreator extends DialogSettings{
	
	public SBBCreator(String sectionName){
		super (sectionName);
		}
	
	public IFile createJavaFile(IProgressMonitor monitor)throws IOException, Exception {
		String fileName = new String(this.getSection("path").get("sbb_name") + ".java");
		IContainer container = getContainer();
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = fillJavaFile();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
			return file;
		} catch (IOException e) {
		    Exception ex = new Exception ("Error generating SBB: template java file is missing!", e);
		    throw ex; 
		}	
	}

	public IFile createXMLFile(IProgressMonitor monitor) throws IOException, Exception {
		String fileName = new String("sbb-jar.xml");
		IContainer container = getContainer();
		final IFile file = container.getFile(new Path(fileName));
		try {
			InputStream stream = fillXMLFile();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
			return file;
		} catch (IOException e) {
		    Exception ex = new Exception ("Error generating SBB: template XML file is missing!", e);
		    throw ex; 
		}	
	}
	

	
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream fillJavaFile() throws IOException {
	    IPath resourcePath = new Path("/dtd/SimpleSBB.java");
		InputStream is = MainPlugin.getDefault().openStream(resourcePath);
	    return is;
	}
	
	private InputStream fillXMLFile() throws IOException {
	    IPath resourcePath = new Path("/dtd/sbb-jar-template.xml");
		InputStream is = MainPlugin.getDefault().openStream(resourcePath);
	    return is;
	}
	
	private IContainer getContainer() throws CoreException {
		String containerName = new String(this.getSection("path").get("container"));		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		return (IContainer) resource;
		}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "Eclipslee", IStatus.OK, message, null);
		throw new CoreException(status);
	}
}
