
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 * Job which draw the specified txt in the SBB graphical view.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class DrawTextJob extends UIJob {
	/** The text to show*/
	private String msg = null;
	/** The image to display*/
	private Image image = null;

	public DrawTextJob(Display display, String name, String msg, Image image) {
		super(display, name);
		this.msg = msg;
		this.image = image;
	}

	/**
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus runInUIThread(IProgressMonitor monitor) {
		ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener().addLabelInView(this.msg, this.image);
		return Status.OK_STATUS;
	}

}
