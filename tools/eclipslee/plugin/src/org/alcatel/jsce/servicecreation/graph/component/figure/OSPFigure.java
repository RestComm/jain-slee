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

/**
 * @author sskhiri
 *
 */
public class OSPFigure extends Figure {
	
	/** The height to draw */
	protected int  height_d = 50;
	/** The widgth to draw */
	protected int  width_d = 50;
	/** The title*/
	private Label  title = null;
	/** Define if the figure is selected or not*/
	private boolean selected = false;
	
	private Label label = null;
	private Label labelVendor = null;
	private Label labelVersion = null;
	private CompartmentFigure attributeFigure = new CompartmentFigure();
	
	public OSPFigure(String name, String vendor, String version, Label title, Integer h, Integer w){
		this.height_d = h.intValue();
		this.width_d = w.intValue();
		
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		//setBorder(new LineBorder(ColorConstants.black, 1));
		//setBackgroundColor(classColor);
		setOpaque(false);

		add(title);
		add(attributeFigure);
		label = new Label("Name:" +name,  ImageManager.getInstance().getImage("alcatel/field_private_obj.gif"));
		labelVendor = new Label("Vendor: "+ vendor,  ImageManager.getInstance().getImage("alcatel/field_private_obj.gif"));
		labelVersion = new Label("Version: "+version,  ImageManager.getInstance().getImage("alcatel/field_private_obj.gif"));
		getAttributesCompartment().add(label);
		getAttributesCompartment().add(labelVendor);
		getAttributesCompartment().add(labelVersion);
	}
	
	public CompartmentFigure getAttributesCompartment() {
		return attributeFigure;
	}

	public void toggleSelected() {
		selected = true;
		this.repaint();
	}
	
	public void toggleNotSelected() {
		selected = false;
		this.repaint();
	}
	
	public void paint(Graphics graphics) {
		if(selected){
			graphics.pushState();
			graphics.setForegroundColor(ColorManager.getInstance().getColor(IGlyph.COLOR_GRAY));
			graphics.setLineStyle(SWTGraphics.LINE_DASH);
			graphics.drawRectangle(getBounds().x, getBounds().y, getBounds().width - 1, getBounds().height - 1);
			graphics.popState();
		}
		super.paint(graphics);
	}

	/**
	 * @return Returns the label.
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String  label) {
		this.label.setText(label);
		repaint();
	}

	/**
	 * @return Returns the labelVendor.
	 */
	public Label getLabelVendor() {
		return labelVendor;
	}

	/**
	 * @param labelVendor The labelVendor to set.
	 */
	public void setLabelVendor(String labelVendor) {
		this.labelVendor.setText(labelVendor) ;
		repaint();
	}

	/**
	 * @return Returns the labelVersion.
	 */
	public Label getLabelVersion() {
		return labelVersion;
	}

	/**
	 * @param labelVersion The labelVersion to set.
	 */
	public void setLabelVersion(String labelVersion) {
		this.labelVersion.setText(labelVersion);
		repaint();
	}

}
