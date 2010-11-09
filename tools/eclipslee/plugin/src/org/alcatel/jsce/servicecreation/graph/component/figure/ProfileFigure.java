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
public class ProfileFigure extends OSPFigure{


	public static Color classColor = new Color(null,159,182,205);

	public ProfileFigure(String name, String vendor, String version, Integer height, Integer width) {
		super(name, vendor, version,new Label("Profile Spec",  ImageManager.getInstance().getImage("alcatel/object16x16.png")),height, width);
	}
	
	public void paint(Graphics graphics) {
		graphics.pushState();
		graphics.setBackgroundColor(classColor);
		graphics.setForegroundColor(new Color(null, 250,250,250));
		graphics.setAlpha(200);
		graphics.fillGradient(getBounds().x, getBounds().y, width_d, height_d, true);
		graphics.popState();
		super.paint(graphics);
	}	

	 

}
