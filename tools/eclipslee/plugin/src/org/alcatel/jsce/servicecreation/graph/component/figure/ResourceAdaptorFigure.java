/**
 * 
 */
package org.alcatel.jsce.servicecreation.graph.component.figure;

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

/**
 * @author sskhiri
 *
 */
public class ResourceAdaptorFigure extends OSPFigure {


	public static Color classColor = new Color(null,161, 161,161);

	public ResourceAdaptorFigure(String name, String vendor, String version, Integer height, Integer width) {
		super(name, vendor, version,new Label("Resource Adaptor Type",  ImageManager.getInstance().getImage("alcatel/resourceadaptortype16x16.png")),height, width);
	}
	
	public void paint(Graphics graphics) {
		graphics.pushState();
		graphics.setAlpha(150);
		graphics.setBackgroundColor(classColor);
		graphics.fillOval(getBounds().x, getBounds().y, width_d, height_d);
		graphics.popState();
		super.paint(graphics);
	}	

}
