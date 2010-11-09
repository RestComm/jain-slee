
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
import org.mobicents.eclipslee.servicecreation.ui.SbbProfilePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ProfileSpecFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;

/**
 *  Description:
 * <p>
 *  Loads the profile specs for the SBB creation wizard. Both, from the Jar directory in which 
 *  we have sotred the generated profiles.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class LoadProfileSpecComponents extends UIJob {
	private SbbProfilePanel control = null;
	private String projectName = null;

	
	/**
	 * Constructor.
	 * @param name
	 * @param control
	 * @param project
	 */
	public LoadProfileSpecComponents(String name, SbbProfilePanel control , String project) {
		super(name);
		this.control = control;
		projectName = project;
	}


	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask(getName(), 100);
		SbbProfilePanel panel = (SbbProfilePanel) control;
		if (projectName != null && panel != null) {
			panel.clearProfiles();
			monitor.worked(30);
			// Find all available profiles.
			DTDXML xml[] = ProfileSpecFinder.getDefault().getComponents(
					BaseFinder.BINARY, projectName, monitor);
			for (int i = 0; i < xml.length; i++) {
				ProfileSpecJarXML ev = (ProfileSpecJarXML) xml[i];
				ProfileSpecXML[] events = ev.getProfileSpecs();
				for (int j = 0; j < events.length; j++) {
					panel.addAvailableProfile(ev, events[j]);
				}
			}
			panel.repack();
		} 
		IStatus result = Status.OK_STATUS;
		monitor.done();
		return result;
	}

}
