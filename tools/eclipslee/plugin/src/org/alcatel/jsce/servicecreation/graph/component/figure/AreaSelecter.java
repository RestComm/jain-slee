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
package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.alcatel.jsce.servicecreation.graph.view.GraphDrawingViewer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Description:
 * <p>
 * Allows to select an area on the graphical view.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 */
public class AreaSelecter extends MouseMotionListener.Stub implements MouseListener {
	private Point last = null;
	private boolean presentLast = false;
	/** The representor part */
	private GraphDrawingViewer representorPart = null;

	/**
	 * 
	 */
	public AreaSelecter(GraphDrawingViewer representor, IFigure currentDrawing) {
		currentDrawing.addMouseMotionListener(this);
		currentDrawing.addMouseListener(this);
		this.representorPart = representor;
	}

	// //////////////////////////////////////////////////////////
	//
	// MouseListener Implementation
	//
	// //////////////////////////////////////////////////////////

	/**
	 * Behaviour when the mouse is clicked in the Selection area.
	 * 
	 * @param me the mouse event
	 */
	public void mousePressed(MouseEvent me) {
		last = me.getLocation();
		IFigure rep = ((IFigure) me.getSource());
		rep.translateFromParent(last);
		if (representorPart.isPresentSelecter()) {
			if (representorPart.isPressedInSelecter(last)) {
				presentLast = false;
			} else {
				presentLast = true;
				representorPart.addSelecter(last.x, last.y, 3, 3);
				representorPart.setActingSelection(true);
			}
		} else {
			presentLast = true;
			representorPart.removeGraphicSelection();
			representorPart.addSelecter(last.x, last.y, 3, 3);
			representorPart.setActingSelection(true);
		}

	}

	
	/**
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me) {
		if (representorPart.isPresentSelecter() && presentLast) {
			representorPart.sendSelectedArea();
			presentLast = false;
		}

	}

	/**
	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDoubleClicked(MouseEvent me) {
		/*
		 * last = me.getLocation(); Draw2DGlyphRepresentor rep = ((Draw2DGlyphRepresentor) me.getSource()); if
		 * (rep.isPresentSelecter()) { if(rep.isPressedInSelecter(me.getLocation())){ presentLast = false; }else{
		 * presentLast = true; rep.addSelecter(last.x, last.y, 3, 3); rep.setActingSelection(true); } }else{ presentLast =
		 * true; rep.addSelecter(last.x, last.y, 3, 3); rep.setActingSelection(true); }
		 */

	}


	
	/**
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDragged(MouseEvent me) {
		if (presentLast) {
			// draw rectangle
			if (me.getSource() instanceof IFigure) {
				Point cur = me.getLocation();
				IFigure rep = ((IFigure) me.getSource());
				rep.translateFromParent(cur);
				Dimension dist = last.getDifference(cur);
				int x = last.x;
				int y = last.y;
				if (dist.width > 0) {
					x = x - dist.width;
				}
				if (dist.height > 0) {
					y = y - dist.height;
				}
				representorPart.updateSelecter(x, y, Math.abs(dist.height), Math
						.abs(dist.width));
			}

		}
	}

}
