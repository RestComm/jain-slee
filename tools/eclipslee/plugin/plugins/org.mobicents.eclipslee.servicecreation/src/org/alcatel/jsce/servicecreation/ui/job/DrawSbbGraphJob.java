
/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.servicecreation.ui.job;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 *  Description:
 * <p>
 * The aim of this job is to draw in run ui thread the SBB graph in 
 * the drawing area.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class DrawSbbGraphJob extends UIJob {
	/** The sbb file to draw*/
	private IFile file = null;

	/**
	 * @param jobDisplay
	 * @param name
	 */
	public DrawSbbGraphJob(Display jobDisplay, String name, IFile sbbFile) {
		super(jobDisplay, name);
		this.file = sbbFile;
	}

	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {

		ICompilationUnit unit = null;
		try {
			unit = JavaCore.createCompilationUnitFrom(file);
		} catch (Exception e) {
			// Suppress Exception. The next check checks for null unit.
		}

		if (unit != null) { // .java file
			SbbJarXML sbbJarXML = SbbFinder.getSbbJarXML(unit);
			if (sbbJarXML == null) {
				// SCELogger.logInfo("Unable to find the corresponding sbb-jar.xml for this java class.");
				return Status.OK_STATUS;
			} else {
				ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener().drawSbbGraph(sbbJarXML,
						file.getProject().getName());
			}
		} else {
			// It might be a XML
			try {
				SbbJarXML sbbJarXML = new SbbJarXML(file);
				ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener().setSbbLoaded(file);
				ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener().drawSbbGraph(sbbJarXML,
						(file).getProject().getName());
			} catch (Exception e) {
				// Noting
			}
		}

		return Status.OK_STATUS;
	}
	
	

}
