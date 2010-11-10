
/**
 *   Copyright 2005 Alcatel, OSP.
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import org.mobicents.eclipslee.servicecreation.ui.SbbChildPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 *  Description:
 * <p>
 * Loads child components for the child wizard page in the SBB creation wizard.  Loads,  both from the Slee-task xml
 *  files and from the JAR directory in which we have stored all generated SBB.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class LoadChildComponents extends UIJob {
	private SbbChildPanel control = null;
	private String project = null;



	/**
	 * Constructor.
	 * @param name
	 * @param control
	 * @param project
	 */
	public LoadChildComponents(String name, SbbChildPanel control , String project) {
		super(name);
		this.control = control;
		this.project = project;
	}


	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask(getName(), 100);
		SbbChildPanel panel = (SbbChildPanel) control;
		if(project != null &&  panel!=null){
			panel.clearChildren();
			// Find all available children.
			monitor.worked(30);
			DTDXML xml[] = SbbFinder.getDefault().getComponents(BaseFinder.JAR_DIR, project, monitor);//.BINARy
			monitor.worked(90);
			for (int i = 0; i < xml.length; i++) {
				SbbJarXML jarXML = (SbbJarXML) xml[i];
				SbbXML sbbs[] = jarXML.getSbbs();
				for (int j = 0; j < sbbs.length; j++) {
					panel.addAvailableChild(jarXML, sbbs[j]);						
				}
			}
			panel.repack();
		}
		IStatus result = Status.OK_STATUS;
		monitor.done();
		return result;
	}

}
