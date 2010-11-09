package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;



public class EventFigure extends OSPFigure {


	public static Color classColor = new Color(null,124, 205,124);

	public EventFigure(String name, String vendor, String version) {
		super(name, vendor, version,new Label("Event",  ImageManager.getInstance().getImage("alcatel/event16x16.png")), new Integer(80), new Integer(80));
	}
	
	public void paint(Graphics graphics) {
		graphics.pushState();
		graphics.setBackgroundColor(classColor);
		graphics.fillOval(getBounds());
		graphics.popState();
		super.paint(graphics);
	}	

	 

}
