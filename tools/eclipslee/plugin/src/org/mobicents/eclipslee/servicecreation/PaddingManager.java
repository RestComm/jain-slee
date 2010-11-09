
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
package org.mobicents.eclipslee.servicecreation;

import org.eclipse.draw2d.geometry.Insets;

/**
 *  Description:
 * <p>
 * Manager which holds data about the padding of a state editor client.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class PaddingManager {
	/** Data*/
	private int bottom = 50, rigth=50, left=50, top=50;

	/**
	 * 
	 */
	public PaddingManager() {
		super();
	}

	/**
	 * @return Returns the bottom.
	 */
	public int getBottom() {
		return bottom;
	}

	/**
	 * @param bottom The bottom to set.
	 */
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	/**
	 * @return Returns the left.
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @param left The left to set.
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * @return Returns the rigth.
	 */
	public int getRigth() {
		return rigth;
	}

	/**
	 * @param rigth The rigth to set.
	 */
	public void setRigth(int rigth) {
		this.rigth = rigth;
	}

	/**
	 * @return Returns the top.
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @param top The top to set.
	 */
	public void setTop(int top) {
		this.top = top;
	}

	/**
	 * @return the inset based on the padding values.
	 */
	public Insets getInstet() {
		return new Insets(top, left, bottom, rigth);
	}

}
