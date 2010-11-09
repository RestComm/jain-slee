
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

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 *  Description:
 * <p>
 * A box figure
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */

public class GlyphBox extends GlyphGraphic {
    private int height = 0, width = 0;
    private String label1;
    private int step = 8;
    private int labelSize1 = 0, labelHSizeMin = 16;
    /**
     * labelSizeLocal is the (real) size of the label in pixel according to the
     * composite.
     */
    private int labelSizeLocal = 0;
    private boolean resized = false;

    /**
     * The
     * 
     * @link GlyphBox contructor. <br>
     * 
     * @param h
     *                  The height of the Box
     * @param w
     *                  The width of the Box
     * @param x
     *                  The x position
     * @param y
     *                  The y position
     * @param image
     *                  The icon. If no icon is present, image = null. If the image is
     *                  biger than the box size, the image will be automatically
     *                  re-comptuted.
     * @param label
     *                  The label to draw in the Box. If the text is to big to enter
     *                  in the Box, the size of the box will be automatically
     *                  re-adjusted.
     * @throws ClassCastException *
     * @since 22/06/04
     */
    public GlyphBox(int h, int w, int x, int y, Image image, String label) throws ClassCastException {
        super();
        this.width = w;
        this.height = h;
        if (label != null) {
            presentLabel = true;
            this.label1 = label;
            labelSize1 = 0;

            if (h < labelHSizeMin) {
                this.height = labelHSizeMin;
            }
        }
        if (image != null) {
            this.image = image;
            presentIcon = true;
        }
        if (this.width < labelSize1 + step) {
            this.width = labelSize1 + step;
        }
        this.setBounds(new Rectangle(x, y, this.width, height));
        this.setLocation(new Point(x, y));
    }

    /**
     * Resize the figure.
     */
    private void reSizeMe(int x, int y) {
        if (!resized) {
            resized = true;
            Font font1 = getFont();
            if (isPresentLabel() & font1 != null) {
                labelSize1 = FigureUtilities.getTextWidth(label1, font1);
                if (this.height < labelHSizeMin) {
                    this.height = labelHSizeMin;
                }
                if (this.width < labelSize1 + step) {
                    this.width = labelSize1 + step;
                }
                this.setBounds(new Rectangle(x, y, this.width, this.height));
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    protected void paintFigure(Graphics graphics) {
        reSizeMe(getBounds().getLocation().x, getBounds().getLocation().y);
        graphics.pushState();
        int w = getBounds().width - 2;
        int h = getBounds().height - 2;
        if (isPresentFillColor()) {
            graphics.setBackgroundColor(fillColor);
            graphics.pushState();
            graphics.fillRectangle(getLocation().x + 1, getLocation().y + 1, w - 1, h - 1);
            graphics.popState();
        }
        if (isBorderVisible()) {
            graphics.pushState();
            if (!getBorderEffect().equals(IGlyph.NONE)) {
                if (getBorderEffect().equals(IGlyph.DOTED)) {
                    graphics.setLineStyle(SWTGraphics.LINE_DASH);
                } else {
                    /* Other effect ... */
                }
            }// end of effects
            if (isPresentBorderColor()) {
                graphics.setForegroundColor(BorderColor);
            }
            graphics.drawRectangle(getLocation().x, getLocation().y, w, h);
            graphics.popState();
        }
        if (presentIcon) {

            Rectangle destination = new Rectangle(getLocation().x + 2,
                    getLocation().y + 2, w - 2, h - 2);
            graphics.drawImage(image, new Rectangle(image.getBounds()), destination);
        }

        if (presentLabel) {
            labelSizeLocal = FigureUtilities.getTextWidth(label1, getFont());
            double frwrd = step, hfrwrd = 0;
            hfrwrd = (double) (getBounds().height - labelHSizeMin) / 2;
            if (alignment.equals(IGlyph.TXT_CENTER)) {
                frwrd = (double) (getBounds().width - labelSizeLocal) / 2.0;
            } else {
                if (alignment.equals(IGlyph.TXT_RIGHT)) {
                    frwrd = (double) (getBounds().width - labelSizeLocal);
                } else {
                    if (alignment.equals(IGlyph.TXT_LEFT)) {
                        frwrd = 2.0;
                    }
                }
            }
            if (frwrd > 0) {
                graphics.drawText(label1, new Point(getLocation().x
                        + frwrd, getLocation().y + hfrwrd));
            }
        }
        graphics.popState();

    }
}