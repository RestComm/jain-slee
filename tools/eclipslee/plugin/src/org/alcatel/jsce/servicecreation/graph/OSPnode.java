
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
package org.alcatel.jsce.servicecreation.graph;

import org.alcatel.jsce.util.JainUtils;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;

/**
 *  Description:
 * <p>
 * Represents an OSP node
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPnode extends Node {
	/** The type of the node. @see org.alcatel.jsce.util.JainUtils*/
	private int typeID = JainUtils.SBB_TYPEID;


	/**
	 * @param data
	 */
	public OSPnode(Object data, int type) {
		super(data);
		this.typeID = type;
	}

	/**
	 * @param parent
	 */
	public OSPnode(Subgraph parent) {
		super(parent);
	}

	/**
	 * @param data
	 * @param parent
	 */
	public OSPnode(Object data, Subgraph parent) {
		super(data, parent);
	}

}
