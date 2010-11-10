
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
package org.alcatel.jsce.servicecreation.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 *  Description:
 * <p>
 * Class used to create the OSP SCE-SE perspective.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPPerspectiveFactory implements IPerspectiveFactory {
	
	/** The sbb graph view*/
	private static String SBBGRAPH_VIEW = "org.alcatel.jsce.servicecreation.graph.view";

	/**
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
		//Get the editor area
		String editorArea = layout.getEditorArea();
		
		//Add the outline
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, 0.25f, editorArea);
		layout.addView(IPageLayout.ID_RES_NAV, IPageLayout.LEFT, 0.25f, editorArea);
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.66f, editorArea);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(SBBGRAPH_VIEW);
		/*layout.addView(IPageLayout.ID_PROBLEM_VIEW, IPageLayout.BOTTOM, 0.66f, editorArea);
		layout.addView(IPageLayout.ID_PROBLEM_VIEW, IPageLayout.BOTTOM, 0.66f, editorArea);*/

		//Add action
		layout.addShowInPart(SBBGRAPH_VIEW);
		layout.getViewLayout(SBBGRAPH_VIEW).setCloseable(true);

	}

}
