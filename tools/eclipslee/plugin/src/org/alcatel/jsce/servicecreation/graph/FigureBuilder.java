
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.util.JainUtils;
import org.alcatel.jsce.servicecreation.graph.component.figure.EventMasterFigure;
import org.alcatel.jsce.servicecreation.graph.component.figure.ProfileFigure;
import org.alcatel.jsce.servicecreation.graph.component.figure.ResourceAdaptorFigure;
import org.alcatel.jsce.servicecreation.graph.component.figure.SbbFigure;
import org.alcatel.jsce.util.image.ImageManager;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;

/**
 *  Description:
 * <p>
 * Build a Figure to each node and edge of the graph it recieves.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class FigureBuilder {
	/** The hash map node -> figure*/
	private HashMap nodeFigureMap = null;
	/**The figure -> node*/
	private HashMap figureNodeMap = null;

	/**
	 * 
	 */
	public FigureBuilder() {
		nodeFigureMap = new HashMap();
		figureNodeMap = new HashMap();
	}

	/**
	 * Build figure to each node and edge according thier type.
	 * @param graph the graph to paint
	 * @param drawing2 the figure on which we will paint
	 */
	public IFigure buildFigure(DirectedGraph graph, Figure drawing) {
		cleanFigure(drawing);
		nodeFigureMap.clear();
		figureNodeMap.clear();
		drawing.setBackgroundColor(ColorConstants.white);
		buildNodeEdgeFigure(graph, drawing);
		return drawing;
	}

	/**
	 * Clean the correpsonding figure.
	 * @param drawing
	 */
	public void cleanFigure(Figure drawing) {
		drawing.removeAll();
		drawing.setBackgroundColor(ColorConstants.white);
		drawing.setLayoutManager(new XYLayout());
		drawing.setOpaque(true);
	}

	private void buildNodeEdgeFigure(DirectedGraph graph, Figure drawing) {
		//		 For each node
		NodeList nodes = graph.nodes;
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			Node node_i = (Node) iter.next();
			if (node_i.data instanceof HashMap) {
				HashMap map_i = (HashMap) node_i.data;
				Integer typeID = (Integer) map_i.get("TYPE");
				String name = (String) map_i.get("NAME");
				String vendor = (String) map_i.get("VENDOR");
				String version = (String) map_i.get("VERSION");
				Integer height = (Integer) map_i.get("H");
				Integer width = (Integer) map_i.get("W");
				switch (typeID.intValue()) {
				case JainUtils.SBB_TYPEID:
					SbbFigure sbbFigure = new SbbFigure(name, vendor, version, height,width);
					drawing.add(sbbFigure, new Rectangle(node_i.x, node_i.y, node_i.width, node_i.height));
					nodeFigureMap.put(node_i, sbbFigure);
					figureNodeMap.put(sbbFigure, node_i);
					break;
				case JainUtils.PROFILE_TYPEID:
					ProfileFigure profileFigure = new ProfileFigure(name, vendor, version, height,width);
					drawing.add(profileFigure, new Rectangle(node_i.x, node_i.y, node_i.width, node_i.height));
					nodeFigureMap.put(node_i, profileFigure);
					figureNodeMap.put(profileFigure, node_i);
					break;
				case JainUtils.EVENT_TYPEID:
					EventMasterFigure eventFigure = new EventMasterFigure( height,width);
					HashMap[] eventMaps = (HashMap[]) map_i.get("LIST");
					for (int i = 0; i < eventMaps.length; i++) {
						HashMap map = eventMaps[i];
						String eventName = (String) map.get("NAME");
						String eventVendor = (String) map.get("VENDOR");
						String eventVersion = (String) map.get("VERSION");
						eventFigure.addEvent(eventName, eventVersion, eventVendor);
					}
					drawing.add(eventFigure, new Rectangle(node_i.x, node_i.y, node_i.width, node_i.height));
					nodeFigureMap.put(node_i, eventFigure);
					figureNodeMap.put(eventFigure, node_i);
					break;
				case JainUtils.RES_TYPE_TYPEID:
					ResourceAdaptorFigure raFigure = new ResourceAdaptorFigure(name, vendor, version,  height,width);
					drawing.add(raFigure, new Rectangle(node_i.x, node_i.y, node_i.width, node_i.height));
					nodeFigureMap.put(node_i, raFigure);
					figureNodeMap.put(raFigure, node_i);
					break;
				default:
					Figure defaultFigure = new Figure();
					defaultFigure.add(new Label(ImageManager.getInstance().getImage("alcatel/notKnown.gif")));
					nodeFigureMap.put(node_i, defaultFigure);
					figureNodeMap.put(defaultFigure, node_i);
					break;
				}
			} else {
				SCELogger.logError("Error while building graph figure", new IllegalStateException(
						"No hash map in node data"));
			}
		}

		// For each edge
		EdgeList edges = graph.edges;
		for (Iterator iter = edges.iterator(); iter.hasNext();) {
			Edge edge_i = (Edge) iter.next();
			PolylineConnection conn = connection(edge_i);
			conn.setForegroundColor(ColorConstants.gray);
			PolygonDecoration dec = new PolygonDecoration();
			conn.setTargetDecoration(dec);
			conn.setStart(edge_i.getPoints().getFirstPoint());
			IFigure target = (IFigure) nodeFigureMap.get(edge_i.target);
			conn.setEnd(target.getBounds().getCenter());
			drawing.add(conn);
		}
		
	}

	/**
	 * Builds a connection for the given edge
	 * @param e the edge
	 * @return the connection
	 */
	private PolylineConnection connection(Edge e) {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		conn.setSourceAnchor(new ChopboxAnchor((Figure) nodeFigureMap.get(e.source)));
		conn.setTargetAnchor(new ChopboxAnchor((Figure) nodeFigureMap.get(e.target)));
		List bends = new ArrayList();
		NodeList nodes = e.vNodes;
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.getNode(i);
				int x = n.x;
				int y = n.y;
				bends.add(new AbsoluteBendpoint(x, y));
				bends.add(new AbsoluteBendpoint(x, y + n.height));
			}
		}
		conn.setRoutingConstraint(bends);
		return conn;
	}

	public void buildCompoundFigure(CompoundDirectedGraph graph, Figure drawing) {
		drawing.removeAll();
		drawing.setBackgroundColor(ColorConstants.white);
		drawing.setLayoutManager(new XYLayout());
		drawing.setOpaque(true);
		nodeFigureMap.clear();
		drawing.setBackgroundColor(ColorConstants.white);
		
		for (int i = 0; i < graph.subgraphs.size(); i++) {
			Subgraph s = (Subgraph)graph.subgraphs.get(i);
			buildSubgraphFigure(drawing, s);
		}
		buildNodeEdgeFigure(graph, drawing);
		
	}

	private void buildSubgraphFigure(Figure drawing, Subgraph s) {
		Figure figure = new Figure();	
		figure.setBorder(new LineBorder(ColorConstants.gray, s.insets.left));
		figure.add(new Label("Child SBB"));
		drawing.add(figure, new Rectangle(s.x, s.y, s.width, s.height));
		nodeFigureMap.put(s, figure);
		
	}

	/**
	 * @return Returns the figureNodeMap.
	 */
	public HashMap getFigureNodeMap() {
		return figureNodeMap;
	}

	/**
	 * @param figureNodeMap The figureNodeMap to set.
	 */
	public void setFigureNodeMap(HashMap figureNodeMap) {
		this.figureNodeMap = figureNodeMap;
	}

	/**
	 * @return Returns the nodeFigureMap.
	 */
	public HashMap getNodeFigureMap() {
		return nodeFigureMap;
	}

	/**
	 * @param nodeFigureMap The nodeFigureMap to set.
	 */
	public void setNodeFigureMap(HashMap nodeFigureMap) {
		this.nodeFigureMap = nodeFigureMap;
	}

}
