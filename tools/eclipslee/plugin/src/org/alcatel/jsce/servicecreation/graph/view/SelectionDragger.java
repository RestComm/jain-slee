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
package org.alcatel.jsce.servicecreation.graph.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.servicecreation.graph.component.figure.EventMasterFigure;
import org.alcatel.jsce.servicecreation.graph.component.figure.OSPFigure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 *  Description:
 * <p>
 * Allows to drag selectionFigure on a view.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */

public class SelectionDragger extends MouseMotionListener.Stub implements MouseListener {
	private GraphDrawingViewer representor;
	private Point last = null;
	private List currentSelection = null;
	private IFigure selectionFigure = null;
	private IFigure currentDrawing = null;

	public SelectionDragger(IFigure figure, GraphDrawingViewer parent, IFigure drawing) {
		representor = parent;
		this.selectionFigure = figure;
		this.currentDrawing = drawing;
		currentSelection = new ArrayList();
	}

	////////////////////////////////////////////////////////////
	//
	// MouseListener Implementation 
	//
	////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
	 */
	public void mousePressed(MouseEvent me) {
		if (representor.isActingSelection()) {
			last = me.getLocation();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDoubleClicked(MouseEvent me) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener.Stub#mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDragged(MouseEvent me) {
		if (representor.isActingSelection()) {
			Point p = me.getLocation();
			if (last == null) {
				last = p;
			}
			Dimension delta = p.getDifference(last);
			last = p;
			Figure f = ((Figure) me.getSource());
			if (currentSelection != null) {
				if (!currentSelection.contains(f)) {
					/*It is the selection rectangle*/
					f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
				} else {
					/*It is a selectionFigure from the selection, then we have to move the selection 
					 * rectangle too*/
					//System.out.println( "delta for selection : "+ delta.toString());
					selectionFigure.setBounds(selectionFigure.getBounds().getTranslated(delta.width, delta.height));
				}
				for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
					IFigure element = (IFigure) iter.next();
					currentDrawing.remove(element);
					currentDrawing.add(element,element.getBounds().getTranslated(delta.width, delta.height) );
				}
			}
		}
	}

	////////////////////////////////////////////////////////////
	//
	// Selection Management
	//
	////////////////////////////////////////////////////////////

	public List getCurrentSelection() {
		return currentSelection;
	}

	/**
	 * Set the current selection and toggle all figure in a selected state.
	 * @param currentSelection
	 */
	public void setCurrentSelection(List currentSelection) {
		this.currentSelection = currentSelection;
		for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
			IFigure figure = (IFigure) iter.next();
			if(figure instanceof OSPFigure){
				OSPFigure figure_i = (OSPFigure) figure;
				figure_i.toggleSelected();
			}else{
				if(figure instanceof EventMasterFigure){
					EventMasterFigure figure_i = (EventMasterFigure) figure;
					figure_i.toggleSelected();
				}
			}
		}
	}

	/**
	 * Active the dragging system for figures.
	 */
	public void setActive() {
		if (currentDrawing.getChildren().contains(selectionFigure)) {
			selectionFigure.addMouseMotionListener(this);
			selectionFigure.addMouseListener(this);
		}
		if (currentSelection != null) {
			for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
				Figure selected = (Figure) iter.next();
				selected.addMouseMotionListener(this);
				selected.addMouseListener(this);
			}
		}
	}

	/**
	 * De-actecive the dragging system for the old selection.
	 * Called by graphic drawing viewer part when remove the current selection. (If the user has clicked out of the previous slection area.
	 */
	public void setNotActive() {
		if (currentDrawing.getChildren().contains(selectionFigure)) {
			selectionFigure.removeMouseMotionListener(this);
			selectionFigure.removeMouseListener(this);
		}

		if (currentSelection != null) {
			//System.out.println("Removing Mouse linster from " + currentSelection.size());
			for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
				Figure selected = (Figure) iter.next();
				selected.removeMouseMotionListener(this);
				selected.removeMouseListener(this);
			}
		}
	}

	/**
	 * Remove the current selection and set the figures to a non-selected state.
	 */
	public void removeSelection() {
		for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
			IFigure figure = (IFigure) iter.next();
			if(figure instanceof OSPFigure){
				OSPFigure figure_i = (OSPFigure) figure;
				figure_i.toggleNotSelected();
			}else{
				if(figure instanceof EventMasterFigure){
					EventMasterFigure figure_i = (EventMasterFigure) figure;
					figure_i.toggleNotSelected();
				}
			}
		}
		currentSelection.clear();
	}

	/**
	 * @param point
	 * @return true if the point is contained into one of the selected figure.
	 */
	public boolean isPressedInSelection(Point point) {
		for (Iterator iter = currentSelection.iterator(); iter.hasNext();) {
			IFigure figure = (IFigure) iter.next();
			if(figure.containsPoint(point))
				return true;
		}
		return false;
	}

}
