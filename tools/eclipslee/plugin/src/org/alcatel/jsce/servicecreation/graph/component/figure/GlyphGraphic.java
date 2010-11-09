
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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 *  Description:
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 * <p>
 * This Class represent a grpahical glyph according the definition of an @link org.eclipse.draw2d.Figure of draw2D.
 * It defines the common Glyph beheviour.
 * @see ulb.vbm.glyph.Glyph 
 * </p>
 */
public abstract class GlyphGraphic extends Figure {
	protected boolean presentIcon = false, presentLabel = false, borderVisible=true, presentBorderColor=false;
	protected boolean presentFillColor = false;
	protected Image image = null;
	protected String label, alignment=IGlyph.TXT_CENTER, borderEffect=IGlyph.NONE;
	protected int labelSize = 0, imageSize=0;
	protected Color BorderColor=null, fillColor=null;
	protected IFigure contener;

	
	/**
	 * 
	 */
	public GlyphGraphic() {
		super();
	}
	/**
	 * @return Returns the borderVisible.
	 */
	public boolean isBorderVisible() {
		return borderVisible;
	}
	/**
	 * @param borderVisible The borderVisible to set.
	 */
	public void setBorderVisible(boolean borderVisible) {
		this.borderVisible = borderVisible;
	}
	/**
	 * @return Returns the fillColor.
	 */
	public Color getFillColor() {
		return fillColor;
	}
	/**
	 * @param fillColor The fillColor to set.
	 */
	public void setFillColor(Color fillColor, String colorName) {
		if(!colorName.equals(IGlyph.COLOR_NONE)){
			this.fillColor = fillColor;
			setPresentFillColor(true);
		}
		
	}
	/**
	 * @return Returns the presentBorderColor.
	 */
	public boolean isPresentBorderColor() {
		return presentBorderColor;
	}
	/**
	 * @param presentBorderColor The presentBorderColor to set.
	 */
	public void setPresentBorderColor(boolean presentColor) {
		this.presentBorderColor = presentColor;
	}
	/**
	 * @return Returns the presentIcon.
	 */
	public boolean isPresentIcon() {
		return presentIcon;
	}
	/**
	 * @param presentIcon The presentIcon to set.
	 */
	public void setPresentIcon(boolean presentIcon) {
		this.presentIcon = presentIcon;
	}
	/**
	 * @return Returns the presentLabel.
	 */
	public boolean isPresentLabel() {
		return presentLabel;
	}
	/**
	 * @param presentLabel The presentLabel to set.
	 */
	public void setPresentLabel(boolean presentLabel) {
		this.presentLabel = presentLabel;
	}
	/**
	 * @return Returns the alignment.
	 */
	public String getAlignment() {
		return alignment;
	}
	/**
	 * @param alignment The alignment to set.
	 */
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}
	/**
	 * @return Returns the borderColor.
	 */
	public Color getBorderColor() {
		return BorderColor;
	}
	/**
	 * @param borderColor The borderColor to set.
	 */
	public void setBorderColor(Color borderColor) {
		BorderColor = borderColor;
		setPresentBorderColor(true);
	}
	/**
	 * @return Returns the borderEffect.
	 */
	public String getBorderEffect() {
		return borderEffect;
	}
	/**
	 * @param borderEffect The borderEffect to set.
	 */
	public void setBorderEffect(String borderEffect) {
		this.borderEffect = borderEffect;
	}
	/**
	 * @return Returns the presentFillColor.
	 */
	public boolean isPresentFillColor() {
		return presentFillColor;
	}
	/**
	 * @param presentFillColor The presentFillColor to set.
	 */
	public void setPresentFillColor(boolean presentFillColor) {
		this.presentFillColor = presentFillColor;
	}
	
	
}

