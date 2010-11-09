/**
 * 
 */
package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;

/**
 * @author sskhiri
 *
 */
public class EventMasterFigure extends Figure {
	/** The height to draw */
	protected int height_d = 50;
	/** The widgth to draw */
	protected int width_d = 50;

	public static Color classColor = new Color(null,185, 211, 238);

	private EventTreeFigure attributeFigure = new EventTreeFigure();
	private boolean selected = false;

	public EventMasterFigure(Integer height, Integer width) {
		this.height_d = height.intValue();
		this.width_d = width.intValue();

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);

		add(new Label("Events set", ImageManager.getInstance().getImage("alcatel/event16x16.png")));
		add(attributeFigure);
	}

	public EventTreeFigure getAttributeFigure() {
		return attributeFigure;
	}

	public void addEvent(String name, String version, String vendor) {
		attributeFigure.add(new Label(name + ", " + vendor + ", " + version, ImageManager.getInstance().getImage(
				"alcatel/event16x16.png")));
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#paint(org.eclipse.draw2d.Graphics)
	 */
	public void paint(Graphics graphics) {
		graphics.pushState();
		graphics.setBackgroundColor(classColor);
		graphics.setForegroundColor(new Color(null, 250,250,250));
		graphics.setAlpha(180);
		graphics.fillGradient(getBounds().x, getBounds().y, width_d, height_d, true);
		//graphics.drawOval(getBounds().x, getBounds().y, width_d, height_d);
		graphics.popState();
		if(selected){
			graphics.pushState();
			graphics.setForegroundColor(ColorManager.getInstance().getColor(IGlyph.COLOR_GRAY));
			graphics.setLineStyle(SWTGraphics.LINE_DASH);
			graphics.drawRectangle(getBounds().x, getBounds().y, getBounds().width - 1, getBounds().height - 1);
			graphics.popState();
		}
		super.paint(graphics);
	}

	public void toggleSelected() {
		selected  = true;
		this.repaint();
	}
	
	public void toggleNotSelected() {
		selected = false;
		this.repaint();
	}

}