
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
import org.mobicents.eclipslee.servicecreation.ui.SbbResourceAdaptorTypePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 *  Description:
 * <p>
 * Loads Ressource adatpers for the SBB creation wizard.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class LoadRAJob extends UIJob {
	private SbbResourceAdaptorTypePanel control = null;
	private String project = null;

	/**
	 * @param name
	 */
	public LoadRAJob(String name) {
		super(name);
	}

	

	/**
	 * @param name
	 * @param control
	 * @param project
	 */
	public LoadRAJob(String name, SbbResourceAdaptorTypePanel control, String project) {
		super(name);
		this.control = control;
		this.project = project;
	}



	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask(getName(), 100);
		if (this.project != null) {
			SbbResourceAdaptorTypePanel panel = (SbbResourceAdaptorTypePanel) control;
			if (panel != null) {
				monitor.worked(10);
				panel.clearResourceAdaptorType();

				// Find all available ResourceAdaptorTypes.
				DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault()
						.getComponents(BaseFinder.JAR_DIR, project, monitor);//BINARY
				monitor.worked(90);
				for (int i = 0; i < xml.length; i++) {
					ResourceAdaptorTypeJarXML ev = (ResourceAdaptorTypeJarXML) xml[i];
					ResourceAdaptorTypeXML[] resourceAdaptorTypes = ev
							.getResourceAdaptorTypes();
					for (int j = 0; j < resourceAdaptorTypes.length; j++) {
						panel.addResourceAdaptorType(ev,
								resourceAdaptorTypes[j]);
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
