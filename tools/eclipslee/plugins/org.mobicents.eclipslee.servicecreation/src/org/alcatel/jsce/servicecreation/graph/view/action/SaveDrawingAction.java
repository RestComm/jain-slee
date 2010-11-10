/*
 Copyright 2004
 Universite Libre de Bruxelles
 Department of Informatics and Networks- Faculty of Engineering
 BioMaze Project
 */

package org.alcatel.jsce.servicecreation.graph.view.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 *  Description:
 * <p>
 * Action which export the drawing as jpg file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class SaveDrawingAction extends Action {

	/**
	 * 
	 */
	public SaveDrawingAction() {
		super();
		/*Not implemented*/
	}

	/**
	 * @param text
	 */
	public SaveDrawingAction(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param image
	 */
	public SaveDrawingAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * @param text
	 * @param style
	 */
	public SaveDrawingAction(String text, int style) {
		super(text, style);
	}

	////////////////////////////////////////////////////////////
	//
	//  Init
	//
	////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	public void run() {
		//ExportDrawing.getInstance().exportAsJPG();
		ExportDrawing.getInstance().exportAsPNG();
	}

}
