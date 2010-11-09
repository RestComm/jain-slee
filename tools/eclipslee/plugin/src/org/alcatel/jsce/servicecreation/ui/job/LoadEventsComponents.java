
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
import org.mobicents.eclipslee.servicecreation.ui.SbbEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;

/**
 *  Description:
 * <p>
 * Loads all  event components available for the events wizrad page.Both from the Slee-task xml
 *  file and from the JAR directory in which we have stored all generated events.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class LoadEventsComponents extends UIJob {	
	private SbbEventsPanel control = null;
	private String project = null;

	/**
	 * Constructor.
	 * @param name
	 * @param control
	 * @param project
	 */
	public LoadEventsComponents(String name, SbbEventsPanel control , String project) {
		super(name);
		this.control = control;
		this.project = project;
	}

	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask(getName(), 100);
		if (project != null) {
			SbbEventsPanel panel = (SbbEventsPanel) control;
			if(panel!=null){
				monitor.worked(10);
				panel.clearEvents();
				// Find all available events.
				DTDXML xml[] = EventFinder.getDefault().getComponents(
						BaseFinder.JAR_DIR | BaseFinder.SLEEDTD_DIR, project, monitor); //BINARY
				monitor.worked(100);
				for (int i = 0; i < xml.length; i++) {
					EventJarXML ev = (EventJarXML) xml[i];
					EventXML[] events = ev.getEvents();
					for (int j = 0; j < events.length; j++) {
						panel.addEvent(ev, events[j]);
					}
				}
				panel.repack();
			}
		}
		IStatus result = Status.OK_STATUS;
		monitor.done();
		return result;
	}

}
