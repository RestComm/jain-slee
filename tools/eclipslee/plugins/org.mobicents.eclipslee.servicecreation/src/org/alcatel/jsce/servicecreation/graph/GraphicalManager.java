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

import org.alcatel.jsce.servicecreation.ui.job.DrawSbbGraphJob;
import org.alcatel.jsce.util.JainUtils;
import org.alcatel.jsce.servicecreation.graph.component.figure.ColorManager;
import org.alcatel.jsce.servicecreation.graph.component.figure.IGlyph;
import org.alcatel.jsce.util.image.ImageManager;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 * Description:
 * <p>
 * The graphical manager is responsable for building graph and figure associeted to an SBB.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 */
public class GraphicalManager implements IResourceChangeListener, ISelectionListener {
	/** Allow to create the associeted graph */
	private GraphBuilder graphBuilder = null;
	/** Allow to associate a Figure to each node and edge of the graph */
	private FigureBuilder figureBuilder = null;
	/** The currect figure */
	private Figure currentDrawing = null;
	/** The sbb loaded in figure */
	private SbbXML currentSsbbXML = null;
	/** The ifile coresponding tp the sbb */
	private IFile sbbLoaded = null;
	/** The font we store */
	private Font font = null;

	/**
	 * 
	 */
	public GraphicalManager() {
		init();
	}

	// /////////////////////////////////////////
	//
	// initialization
	//
	// ////////////////////////////////////////
	private void init() {
		this.graphBuilder = new GraphBuilder();
		this.figureBuilder = new FigureBuilder();
		this.currentDrawing = new Figure();
		// this.currentDrawing .setBackgroundColor(ColorConstants.white);
		this.currentDrawing.setOpaque(true);
		this.currentSsbbXML = null;
		this.font = new Font(Display.getDefault(), "Tahoma", 13, SWT.NORMAL);
	}

	// /////////////////////////////////////////
	//
	// Drawing graph
	//
	// ////////////////////////////////////////

	/**
	 * @param sbbXML
	 *            the corresponding SBB
	 * @param projectName the name of the project
	 * @return the drawing which represent the SBB dependencies.
	 */
	public IFigure drawSbbGraph(SbbXML sbbXML, String projectName) {
		// if(currentSsbbXML!=null){
		// if(currentSsbbXML.getName().equals(sbbXML.getName()) && currentSsbbXML.getVendor().equals(sbbXML.getVendor())
		// && currentSsbbXML.getVersion().equals(sbbXML.getVersion())){
		// //The same sbb must not be repaint
		// return currentDrawing;
		// }
		//		
		this.currentSsbbXML = sbbXML;

		// 1. building the graph node and edge
		DirectedGraph graph = this.graphBuilder.buildSbbGraph(sbbXML, projectName);

		// 2. building the graph figure
		return this.figureBuilder.buildFigure(graph, this.currentDrawing);
	}

	/**
	 * @param sbbXML
	 *            the corresponding SBB
	 * @param drawing
	 *            the figure in which we must paint the draw
	 * @param projectName
	 *            the project name (used to retrieve sbb child) *
	 */
	public void drawCompoundSbbgraph(SbbXML sbbXML, Figure drawing, String projectName) {
		// 1. building the graph node and edg
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		HashMap drawingMap = new HashMap();
		drawingMap.put("TYPE", new Integer(JainUtils.UNKWNOWN_TYPEID));
		Subgraph drawingSubgraph = new Subgraph(drawingMap);
		nodes.add(drawingSubgraph);

		this.graphBuilder.buildCompundSbbGraph(drawingSubgraph, nodes, edges, sbbXML, projectName);
		CompoundDirectedGraph graph = new CompoundDirectedGraph();
		graph.nodes = nodes;
		graph.edges = edges;
		new CompoundDirectedGraphLayout().visit(graph);

		// 2. building the graph figure
		this.figureBuilder.buildCompoundFigure(graph, drawing);

	}

	/**
	 * @return Returns the currentDrawing.
	 */
	public IFigure getCurrentDrawing() {
		return currentDrawing;
	}

	// /////////////////////////////////////////
	//
	// Iresource Change listener
	//
	// ////////////////////////////////////////

	/**
	 * This listener is registred by the plug-in in order to folow change in file system. It will be uesd by the
	 * graphical manager in order to know if the current graphical sbb view must be update.
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			IResourceDelta delta = event.getDelta();
			if (delta != null) {
				analyzeResouceChanged(delta);
			}
			break;
		case IResourceChangeEvent.PRE_DELETE:
			/*
			 * this.figureBuilder.cleanFigure(this.currentDrawing); this.currentDrawing.add(new
			 * Label(ImageManager.getInstance().getImage("alcatel/logo_alcatel.gif")));
			 */
			break;

		default:
			break;
		}
	}

	public void addLabelInView(String msg, final Image image) {
		// Setting default image
		this.currentDrawing.removeAll();
		this.currentDrawing.setLayoutManager(new FlowLayout());
		this.currentDrawing.setBackgroundColor(ColorConstants.white);
		Label label = new Label(msg);
		label.setFont(this.font);
		label.setForegroundColor(ColorManager.getInstance().getColor(IGlyph.COLOR_BLUE));
		label.setIcon(image);
		this.currentDrawing.add(label);
	}

	// /////////////////////////////////////////
	//
	// Selection listener implementation
	//
	// ////////////////////////////////////////

	private void analyzeResouceChanged(IResourceDelta delta) {
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			IResourceDelta childre_i = children[i];
			analyzeResouceChanged(childre_i);
			IResource resource_i = childre_i.getResource();
			if (resource_i != null) {
				if (resource_i.getProjectRelativePath().segmentCount() > 0) {
					IFile fileChanged = resource_i.getProject().getFile(resource_i.getProjectRelativePath());
					if (fileChanged.equals(getSbbLoaded())) {
						drawSbbInIFile(fileChanged);
					}
				}

			}
		}

	}

	/**
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		pageSelectionChanged(part, selection);

	}

	/**
	 * Called when the selection changed.
	 * 
	 * @param part
	 * @param selection
	 */
	protected void pageSelectionChanged(IWorkbenchPart part, ISelection selection) {
		/*
		 * this.drawing.removeAll(); this.drawing.setLayoutManager(new FlowLayout()); this.drawing.add(new
		 * Label(ImageManager.getInstance().getImage("alcatel/logo_alcatel.gif")));
		 */
		if (part == this)
			return;
		if (selection == null && selection.isEmpty()) {
			// SCELogger.logInfo("Please select an SBB's Java or XML file first.");
			return;
		}

		if (!(selection instanceof IStructuredSelection)) {
			// SCELogger.logInfo("Please select an SBB's Java or XML file first.");
			return;
		}

		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			// SCELogger.logInfo("This plugin only supports editing of one service building block at a time.");
			return;
		}

		// Setting default image
		this.currentDrawing.removeAll();
		this.currentDrawing.setLayoutManager(new FlowLayout());
		this.currentDrawing.setBackgroundColor(ColorConstants.white);
		this.currentDrawing.add(new Label(ImageManager.getInstance().getImage("alcatel/mobicents_logo.gif")));

		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();

		if (obj instanceof IFile) {
			if (((IFile) obj).getFileExtension().equals("xml") || ((IFile) obj).getFileExtension().equals("java"))
				drawSbbInIFile((IFile) obj);
		} else {
			if (obj instanceof ICompilationUnit) {
				ICompilationUnit unit = (ICompilationUnit) obj;
				try {
					setSbbLoaded(unit.getCorrespondingResource().getProject().getFile(
							unit.getCorrespondingResource().getProjectRelativePath()));
					drawContentCompilationUnit(unit);
				} catch (JavaModelException e) {
					SCELogger.logError("Error while currentDrawing sbb", e);
				}
			} else {

			}
		}

	}

	/**
	 * Draw the file in the sbb graphical view if it is an SBB.
	 * 
	 * @param file
	 */
	private void drawSbbInIFile(IFile file) {
		DrawSbbGraphJob drawJob = new DrawSbbGraphJob(PlatformUI.getWorkbench().getDisplay(), "Redrawing SBB graph",
				file);
		drawJob.schedule();
	}

	/**
	 * Remove all the content of the drawing.
	 */
	private void removeALLInView() {
		// Setting default image
		this.currentDrawing.removeAll();
		this.currentDrawing.setLayoutManager(new FlowLayout());
		this.currentDrawing.setBackgroundColor(ColorConstants.white);
		this.currentDrawing.add(new Label(ImageManager.getInstance().getImage("alcatel/logo_alcatel.gif")));
	}

	/**
	 * Draw the graph of the sbb contained in this project.
	 * 
	 * @param sbbJarXML
	 * @param projectName
	 */
	public void drawSbbGraph(SbbJarXML sbbJarXML, String projectName) {
		SbbXML[] sbbXMLs = sbbJarXML.getSbbs();
		if (sbbXMLs.length > 0) {
			drawSbbGraph(sbbXMLs[0], projectName);
			// ServiceCreationPlugin.getDefault().getMainControl().buildSbbCompoundGraphFigure(sbbXMLs[0],
			// this.currentDrawing, projectName);
		}

	}

	private void drawContentCompilationUnit(ICompilationUnit unit) throws JavaModelException {
		if (unit != null) { // .java file
			SbbJarXML sbbJarXML = SbbFinder.getSbbJarXML(unit);
			if (sbbJarXML == null) {
				// SCELogger.logInfo("Unable to find the corresponding sbb-jar.xml for this java class.");
				return;
			} else {
				drawSbbGraph(sbbJarXML, unit.getCorrespondingResource().getProject().getName());
			}
		}

	}

	/**
	 * @return Returns the sbbLoaded.
	 */
	public IFile getSbbLoaded() {
		return sbbLoaded;
	}

	/**
	 * @param sbbLoaded
	 *            The sbbLoaded to set.
	 */
	public void setSbbLoaded(IFile sbbLoaded) {
		this.sbbLoaded = sbbLoaded;
	}

	/**
	 * @return Returns the sbbXML.
	 */
	public SbbXML getSbbXML() {
		return currentSsbbXML;
	}

	/**
	 * @param sbbXML
	 *            The sbbXML to set.
	 */
	public void setSbbXML(SbbXML sbbXML) {
		this.currentSsbbXML = sbbXML;
	}

	/**
	 * List all nodes which intersect the selection area.
	 * 
	 * @param children
	 *            the list of figure contained in the drawing
	 * @param selection
	 *            the selection area bound
	 * @return the list of nodes.
	 */
	public List getNodeInstersected(List children, Rectangle selection) {
		HashMap figureNodeMap = figureBuilder.getFigureNodeMap();
		List selected = new ArrayList();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			IFigure child = (IFigure) iter.next();
			if (selection.intersects(child.getBounds())) {
				Node node = (Node) figureNodeMap.get(child);
				if (node != null) {
					selected.add(node);
					// System.out.println("Selected :" + node.getLabel());
				}
			}
		}
		return selected;
	}

	/**
	 * @param selectedNodes
	 *            the list of nodes for which we are looking for figures.
	 * @return the list of figure corresponding to the list of nodes
	 */
	public List getFiguresFromNodes(List selectedNodes) {
		HashMap nodeFigureMap = figureBuilder.getNodeFigureMap();
		List figures = new ArrayList();
		for (Iterator iter = selectedNodes.iterator(); iter.hasNext();) {
			Node node_i = (Node) iter.next();
			IFigure figure = (IFigure) nodeFigureMap.get(node_i);
			if (figure != null) {
				figures.add(figure);
			}
		}
		return figures;
	}

	// /////////////////////////////////////////
	//
	// Dispose methods
	//
	// ////////////////////////////////////////
	public void dispose() {
		if (this.font != null) {
			this.font.dispose();
		}
	}

}
