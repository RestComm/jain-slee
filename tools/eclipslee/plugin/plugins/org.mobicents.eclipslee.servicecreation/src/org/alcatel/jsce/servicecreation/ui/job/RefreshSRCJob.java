
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

import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

/**
 *  Description:
 * <p>
 *  This job refresh the src dirtory of the specified project.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class RefreshSRCJob extends UIJob {
	/** The folder to refresh*/
	private IFolder folder = null;

	/**
	 * @param name
	 */
	public RefreshSRCJob(String name, IFolder folder) {
		super(name);
		this.folder = folder;
	}


	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			folder.refreshLocal(Folder.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			SCELogger.logError("Error during the SRc folder refreshing", e);
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

}
