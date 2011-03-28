/**
 * 
 */
package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

/**
 * @author Sabri Skhiri dit Gabouje
 *
 */
public class SbbFigure extends OSPFigure {

	public static Color classColor = new Color(null, 255, 255, 206);

	public SbbFigure(String name, String vendor, String version, Integer height, Integer width) {
		super(name,vendor, version, new Label("SBB",  ImageManager.getInstance().getImage("alcatel/sib16x16.png")),height, width);
	}
	
	public void paint(Graphics graphics) {
		graphics.pushState();
		graphics.setBackgroundColor(classColor);
		graphics.setAlpha(200);
		int x = getBounds().x;
		int y = getBounds().y ;//+getBounds().height/2;
		graphics.fillOval(x, y, width_d, height_d);
		graphics.popState();
		//getAttributesCompartment().setBounds(new Rectangle(x,y, getBounds().width, getBounds().height));
		super.paint(graphics);
	}	

	 

}
