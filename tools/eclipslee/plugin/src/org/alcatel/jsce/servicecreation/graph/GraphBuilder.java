
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.alcatel.jsce.servicecreation.ui.WaitDialog;
import org.alcatel.jsce.util.JainUtils;
import org.alcatel.jsce.util.ProjectFileManager;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileSpecRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorTypeBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 *  Description:
 * <p>
 * Object responsible for building the graph according to the SBB XML
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class GraphBuilder {

	protected DTDXML[] xml;

	/**
	 * 
	 */
	public GraphBuilder() {
	}

	/**
	 * Build nodes and edges corresponding to the specified sbb.
	 * @param sbbXML the sbb which will be shown.
	 * @param projectName the name of the project
	 */
	public DirectedGraph buildSbbGraph(SbbXML sbbXML, String projectName) {
		DirectedGraph graph = new DirectedGraph();
		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();
		
		
		
		//1. The sbb node
		HashMap rootMap = new HashMap();
		rootMap.put("TYPE", new Integer(JainUtils.SBB_TYPEID));
		rootMap.put("NAME", sbbXML.getName());
		rootMap.put("VERSION", sbbXML.getVersion());
		rootMap.put("VENDOR", sbbXML.getVendor());
		rootMap.put("H", new Integer(80));
		rootMap.put("W", new Integer(120));
		Node root = new Node(rootMap);
		root.height = 80;
		root.width = 120;
		nodes.add(root);
		
		//2. Extract the Profile spec
		// Extract profile: the aim is to identify profile spe. used
		SbbProfileSpecRefXML[] profileSpecXMLs = sbbXML.getProfileSpecRefs();
		for (int i = 0; i < profileSpecXMLs.length; i++) {
			SbbProfileSpecRefXML refXML_i = profileSpecXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.PROFILE_TYPEID));
			map_i.put("NAME", refXML_i.getName());
			map_i.put("VERSION", refXML_i.getVersion());
			map_i.put("VENDOR", refXML_i.getVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			Node ref_i = new Node(map_i);
			ref_i.height = 80;
			ref_i.width = 120;
			Edge edgeRef_i = new Edge(root, ref_i);
			edges.add(edgeRef_i);
			nodes.add(ref_i);
		}
		
		//Extract SBB child
		extractSbbChild(nodes, edges, sbbXML, root, ProjectFileManager.getInstance().lookupSbb(projectName, new NullProgressMonitor()));
		// Extract ra : the aim is to indentify all ra used
		SbbResourceAdaptorTypeBindingXML[] adaptorTypeBindingXMLs = sbbXML.getResourceAdaptorTypeBindings();
		for (int i = 0; i < adaptorTypeBindingXMLs.length; i++) {
			SbbResourceAdaptorTypeBindingXML bindingXML = adaptorTypeBindingXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.RES_TYPE_TYPEID));
			map_i.put("NAME", bindingXML.getResourceAdaptorTypeName());
			map_i.put("VERSION", bindingXML.getResourceAdaptorTypeVersion());
			map_i.put("VENDOR", bindingXML.getResourceAdaptorTypeVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			Node ref_i = new Node(map_i);
			ref_i.height = 80;
			ref_i.width = 150;
			Edge edgeRef_i = new Edge(root, ref_i);
			edges.add(edgeRef_i);
			nodes.add(ref_i);
		}
		
		// Extract event: The aim is to know if event are used or not
		SbbEventXML[] sbbEventXMLs = sbbXML.getEvents();
		//For the event we have 1 node of several events -> an array of Hash
		List mapList = new ArrayList();
		for (int i = 0; i < sbbEventXMLs.length; i++) {
			SbbEventXML eventXML = sbbEventXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.EVENT_TYPEID));
			map_i.put("NAME", eventXML.getName());
			map_i.put("VERSION", eventXML.getVersion());
			map_i.put("VENDOR", eventXML.getVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			mapList.add(map_i);
		}
		int size =0;
		if(sbbEventXMLs.length < 8){
			size = 25*sbbEventXMLs.length;
		}else{
			size = 20*sbbEventXMLs.length;
		}
		if(sbbEventXMLs.length>0){
			HashMap eventMap = new HashMap();
			eventMap.put("TYPE", new Integer(JainUtils.EVENT_TYPEID));
			eventMap.put("LIST", (HashMap[]) mapList.toArray(new HashMap[mapList.size()]));
			eventMap.put("H", new Integer(size));
			eventMap.put("W", new Integer(300));
			Node eventNodei = new Node(eventMap);
			eventNodei.width = 300;
			eventNodei.height = size;
			Edge edgeRef_i = new Edge(root, eventNodei);
			edges.add(edgeRef_i);
			nodes.add(eventNodei);
		}
		
		//Setting hte graph nodes and edges
		graph.nodes = nodes;
		graph.edges = edges;
		
		new DirectedGraphLayout().visit(graph);
		return graph;
	}

	private void extractSbbChild(NodeList nodes, EdgeList edges, SbbXML sbbXML, Node root, SbbJarXML[] allsbbJarXMLs) {
		SbbRefXML[] sbbRefXMLs = sbbXML.getSbbRefs();
		for (int i = 0; i < sbbRefXMLs.length; i++) {
			SbbRefXML  refXML_i = sbbRefXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.SBB_TYPEID));
			map_i.put("NAME", refXML_i.getName());
			map_i.put("VERSION", refXML_i.getVersion());
			map_i.put("VENDOR", refXML_i.getVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			Node ref_i = new Node(map_i);
			ref_i.height = 80;
			ref_i.width = 120;
			Edge edgeRef_i = new Edge(root, ref_i);
			edges.add(edgeRef_i);
			nodes.add(ref_i);
			try {
				SbbJarXML sbbChildJarXML = SbbFinder.searchSBBJarXml(allsbbJarXMLs, refXML_i.getName(),  refXML_i.getVendor(),  refXML_i.getVersion());
				if(sbbChildJarXML==null){
					throw new ComponentNotFoundException("The child "+ refXML_i.getName() +" is not in the project !");
				}
				SbbXML sbbChildXML = sbbChildJarXML.getSbb(refXML_i.getName(),  refXML_i.getVendor(),  refXML_i.getVersion());
				try {
					//1. Extract sbb stat form that children
					extractSbbChild(nodes, edges, sbbChildXML, ref_i, allsbbJarXMLs);
				} catch (Exception e) {
					SCELogger.logError(" Error while extracting the sbb children "+ sbbChildXML.getName() +" sbb", e);
				}
			} catch (ComponentNotFoundException e) {
				SCELogger.logError(" Error while extracting the sbb children from "+ sbbXML.getName() +" sbb", e);
			}
			
		}
	}

	/**
	 * @param sbbXML
	 * @param projectName  the project name used to retrieve sbb child in jars dir.
	 * @return the root node of the subgraph
	 */
	public Node buildCompundSbbGraph(Subgraph drawing, NodeList nodes, EdgeList edges, SbbXML sbbXML, String projectName) {

		//1. The sbb node
		HashMap rootMap = new HashMap();
		rootMap.put("TYPE", new Integer(JainUtils.SBB_TYPEID));
		rootMap.put("NAME", sbbXML.getName());
		rootMap.put("VERSION", sbbXML.getVersion());
		rootMap.put("VENDOR", sbbXML.getVendor());
		rootMap.put("H", new Integer(80));
		rootMap.put("W", new Integer(120));
		Node root = new Node(rootMap, drawing);
		root.height = 80;
		root.width = 120;
		nodes.add(root);
		
		//2. Extract the Profile spec
		// Extract profile: the aim is to identify profile spe. used
		SbbProfileSpecRefXML[] profileSpecXMLs = sbbXML.getProfileSpecRefs();
		for (int i = 0; i < profileSpecXMLs.length; i++) {
			SbbProfileSpecRefXML refXML_i = profileSpecXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.PROFILE_TYPEID));
			map_i.put("NAME", refXML_i.getName());
			map_i.put("VERSION", refXML_i.getVersion());
			map_i.put("VENDOR", refXML_i.getVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			Node ref_i = new Node(map_i, drawing);
			ref_i.height = 80;
			ref_i.width = 120;
			Edge edgeRef_i = new Edge(root, ref_i);
			edges.add(edgeRef_i);
			nodes.add(ref_i);
		}
		//Extract SBB child
		SbbRefXML[] sbbRefXMLs = sbbXML.getSbbRefs();
		if(sbbRefXMLs.length >0){
			SbbXML[] allSBB = getAllSbbInJars(projectName);
			for (int i = 0; i < sbbRefXMLs.length; i++) {
				SbbRefXML  refXML_i = sbbRefXMLs[i];
				HashMap map_i = new HashMap();
				map_i.put("TYPE", new Integer(JainUtils.SBB_TYPEID));
				map_i.put("NAME", refXML_i.getName());
				map_i.put("VERSION", refXML_i.getVersion());
				map_i.put("VENDOR", refXML_i.getVendor());
				map_i.put("H", new Integer(80));
				map_i.put("W", new Integer(120));
				SbbXML sbbXMLRef = extractSbb(refXML_i.getName(), refXML_i.getVendor(), refXML_i.getVersion(), allSBB);
				if(sbbXMLRef!=null){
					HashMap drawingMap = new HashMap();
					drawingMap.put("TYPE", new Integer(JainUtils.UNKWNOWN_TYPEID));
					Subgraph sbb_i_RefSubgraph = new Subgraph(drawingMap);
					nodes.add(sbb_i_RefSubgraph);
					Node rootSubgraph = buildCompundSbbGraph(sbb_i_RefSubgraph, nodes, edges, sbbXMLRef, projectName);
					Edge edgeRef_i = new Edge(root, rootSubgraph);
					edges.add(edgeRef_i);
				}else{
					//TODO when the sbb ref is not found in the jar dir we must draw an error
				}
		}
		}
		// Extract ra : the aim is to indentify all ra used
		SbbResourceAdaptorTypeBindingXML[] adaptorTypeBindingXMLs = sbbXML.getResourceAdaptorTypeBindings();
		for (int i = 0; i < adaptorTypeBindingXMLs.length; i++) {
			SbbResourceAdaptorTypeBindingXML bindingXML = adaptorTypeBindingXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.RES_TYPE_TYPEID));
			map_i.put("NAME", bindingXML.getResourceAdaptorTypeName());
			map_i.put("VERSION", bindingXML.getResourceAdaptorTypeVersion());
			map_i.put("VENDOR", bindingXML.getResourceAdaptorTypeVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			Node ref_i = new Node(map_i, drawing);
			ref_i.height = 80;
			ref_i.width = 120;
			Edge edgeRef_i = new Edge(root, ref_i);
			edges.add(edgeRef_i);
			nodes.add(ref_i);
		}
		// Extract event: The aim is to know if event are used or not
		SbbEventXML[] sbbEventXMLs = sbbXML.getEvents();
		//For the event we have 1 node of several events -> an array of Hash
		List mapList = new ArrayList();
		for (int i = 0; i < sbbEventXMLs.length; i++) {
			SbbEventXML eventXML = sbbEventXMLs[i];
			HashMap map_i = new HashMap();
			map_i.put("TYPE", new Integer(JainUtils.EVENT_TYPEID));
			map_i.put("NAME", eventXML.getName());
			map_i.put("VERSION", eventXML.getVersion());
			map_i.put("VENDOR", eventXML.getVendor());
			map_i.put("H", new Integer(80));
			map_i.put("W", new Integer(120));
			mapList.add(map_i);
		}
		HashMap eventMap = new HashMap();
		eventMap.put("TYPE", new Integer(JainUtils.EVENT_TYPEID));
		eventMap.put("LIST", (HashMap[]) mapList.toArray(new HashMap[mapList.size()]));
		eventMap.put("H", new Integer(20*sbbEventXMLs.length));
		eventMap.put("W", new Integer(300));
		Node eventNodei = new Node(eventMap, drawing);
		eventNodei.width = 300;
		eventNodei.height = 20*sbbEventXMLs.length;
		Edge edgeRef_i = new Edge(root, eventNodei);
		edges.add(edgeRef_i);
		nodes.add(eventNodei);
		
		return root;

	}

	private SbbXML extractSbb(String name, String vendor, String version, SbbXML[] allSBB) {
		for (int i = 0; i < allSBB.length; i++) {
			SbbXML sbbXML = allSBB[i];
			if(sbbXML.getName().equals(name) && sbbXML.getVendor().equals(vendor) && sbbXML.getVersion().equals(version))
				return sbbXML;
		}
		return null;
	}

	/**
	 * @param projectName the project name
	 * @return all sbb XML from the jar directory.
	 */
	private SbbXML[] getAllSbbInJars(final String projectName) {
		List sbbs = new ArrayList();
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				xml = SbbFinder.getDefault().getComponents(
						BaseFinder.JAR_DIR, projectName, monitor, BaseFinder.SBB_CHILD_JAR);
			}
		};
		/* 1. Launch the wait dialog box */
		WaitDialog waitDialog = new WaitDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				"Searching for SBB child components ...");
		waitDialog.open();
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();
		try {
			/*
			 * 2. launch the bysy cursor, block the ui Thread, allow us to
			 * keep an GUI responding
			 */
			progressService.busyCursorWhile(runnable);
			/*
			 * 3. Refressh the SBB event panel, this operation must be
			 * outside of the progressive service
			 */
			
				for (int i = 0; i < xml.length; i++) {
					SbbJarXML jarXML = (SbbJarXML) xml[i];
					SbbXML[] sbbXMLs = jarXML.getSbbs();
					for (int j = 0; j < sbbXMLs.length; j++) {
						SbbXML sbbXML_j = sbbXMLs[j];
						sbbs.add(sbbXML_j);
					}
				}

		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waitDialog.close();
		return (SbbXML	[]) sbbs.toArray(new SbbXML[sbbs.size()]);
	}

}
