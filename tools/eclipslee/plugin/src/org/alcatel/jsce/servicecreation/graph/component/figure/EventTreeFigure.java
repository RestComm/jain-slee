/**
 * 
 */
package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author sskhiri
 *
 */
public class EventTreeFigure extends Figure {

	public EventTreeFigure() {

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new CompartmentFigureBorder());
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(2);
	}

	public class CompartmentFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		/*public void paint(IFigure figure, Graphics graphics, Insets insets) {
		 graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
		 tempRect.getTopRight());
		 }*/

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			// TODO Auto-generated method stub

		}
	}
}
