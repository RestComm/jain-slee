
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.backend;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.alarm.Alarm;
import org.alcatel.jsce.alarm.AlarmManager;
import org.alcatel.jsce.alarm.AlarmsCatalog;
import org.alcatel.jsce.keystore.KeystoreManager;
import org.alcatel.jsce.servicecreation.graph.GraphicalManager;
import org.alcatel.jsce.statevent.EventCatalog;
import org.alcatel.jsce.statevent.EventManager;
import org.alcatel.jsce.statevent.EventType;
import org.alcatel.jsce.util.jad.DecompilatorBridge;
import org.alcatel.jsce.util.jad.IDecompiler;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.mobicents.eclipslee.servicecreation.PaddingManager;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;

/**
 *  Description:
 * <p>
 * This object is the main signal handler between the Eclipse front-end and 
 * the back-end. 
 * 
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class MainControl {
	/** Specialized object for the Alarms catalogs management*/
	private AlarmManager alarmManager = null;
	/** Specialized object for the Stat event catlogs management*/
	private EventManager eventManager = null;
	/** Specialized object for the keystore management*/
	private KeystoreManager keystoreManager = null;
	/** The URL of the plug-in location*/
	private URL configDirectory = null;
	/** The jad bridge*/
	private DecompilatorBridge jadBrigde = null;
	/** The graphical manager for drawing graphs*/
	private GraphicalManager graphicalManager = null;
	/** The padding Manager which hold data for a client state editor*/
	private PaddingManager paddingManager = null;

	/**
	 * Constructor.
	 */
	public MainControl() {
		init();
		
	}
	
	///////////////////////////////////////////
	//
	// Init.
	//
	//////////////////////////////////////////
	
	private void init() {
		try {
			alarmManager = new AlarmManager();
			eventManager = new EventManager();
			keystoreManager = new KeystoreManager();
			jadBrigde = DecompilatorBridge.getInstance();
			graphicalManager = new GraphicalManager();
			paddingManager = new PaddingManager();
			/*
			//URL catalogPath = ServiceCreationPlugin.getDefault().find(new Path("catalog"));
			URL catalogPath = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("catalog"), null);
			//URL resolve = Platform.resolve(catalogPath);
			URL resolve = FileLocator.resolve(catalogPath);
			File resolveFile = new File(resolve.getFile());
			File configFile = resolveFile.getParentFile();
			configDirectory = configFile.toURL();
			
			SCELogger.logInfo("Config Directory: "+ configDirectory +"  , resolve: "+ resolve+ 
					",  resolveFile "+ resolveFile.toString() + ", configFile "+ configFile);
			*/
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////
	//
	// Access to backend features.
	//
	//////////////////////////////////////////
	
	/**
	 * @return the list of all alarm catalogs in the configuration directory 
	 * and in all external catalog directories.
	 */
	public List  getAllAlarmCatalogs() {
			return this.alarmManager.getAllAlarmCatalogs(configDirectory);
	}
	
	/**
	 * @return the list of all stat event catalogs in the configuration directory 
	 * and in all external  catalog directories. A list of  @link EventCatalog
	 */
	public List  getAllStatEventCatalogs() {
			return this.eventManager.getAllStatEventCatalogs(configDirectory);
	}
	
	/**
     * Opens all  the alarm catalog XML files in the sepcifesd directory.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog directory.
     * @return a list of  @link AlarmsCatalog
     */
    public List getAllAlarmCatalogs(URL directory) {
    	return this.alarmManager.getAllCatalog(directory);
    }
    
    /**
     * Opens all  the stat event catalog XML files in the sepcified directory.
     * 
     * @param fileLocation
     *                  is the absolute URL path of the catalog directory.
     * @return a list of  @link EventCatalog
     */
    public List getAllStatEventCatalogs(URL directory) {
    	return this.eventManager.getAllCatalog(directory);
    }
	
	/**
	 * Add an alarm catalog location.
	 * @param catalogLocation The url of the new alarm catalog location.
	 */
	public void addAlarmCatalogURL(URL catalogLocation){
		/*Warning for the moment we allow only one location, then we
		 * clear the list before adding the new url*/
		this.alarmManager.getExternalAlarmCatalogURL().clear();
		this.alarmManager.getExternalAlarmCatalogURL().add(catalogLocation);
	}
	
	/**
	 * Add an Stat Event catalog location.
	 * @param catalogLocation The url of the new stat event catalog location.
	 */
	public void addStatEventCatalogURL(URL catalogLocation){
		/*Warning for the moment we allow only one location, then we
		 * clear the list before adding the new url*/
		this.eventManager.getExternalStatEventCatalogURLs().clear();
		this.eventManager.getExternalStatEventCatalogURLs().add(catalogLocation);
	}
	
	/**
	 * @param dirLocation the directory location where we are looking for the catalog
	 * @param name the catalog name
	 * @param feature the feature number
	 * @return the catalog we are looking for
	 */
	public EventCatalog lookupStatEventCatalog(URL dirLocation, String name, String feature) {
		List catalogs = getAllStatEventCatalogs(dirLocation);
		for (Iterator iter = catalogs.iterator(); iter.hasNext();) {
			EventCatalog catalog_i = (EventCatalog) iter.next();
			if(catalog_i.getFeatureID().equals(feature) && catalog_i.getCatalogName().equals(name)){
				return catalog_i;
			}
		}
		return null;
	}
	
	/**
	 * @param source the source code of the java file from which we want to extract alarms.
	 * @param catalog the catalog in which we must add the new alarms
	 * @return a map with keys the alarms: a_k =-> list of method call using the alarm a_k
	 */
	public HashMap extractAllarmsFromSource(String source, AlarmsCatalog catalog){
		return alarmManager.extractAllAlarmsfromSources(source,catalog);
	}
	
	/**
	 * @param source the source code of the java file from which we want to extract stat events.
	 * @param catalog the catalog in which we must add the new stat events
	 * @return the list of event stat created.
	 */
	public List extractAllStatEventsFromSource(String source, EventType catalog) {
		return eventManager.extractAllStatEvents(source, catalog);
		
	}
	
	/**
	 * @return the list of external catalog URLs
	 * */
	public List getAlarmCatalogURLs(){
		return this.alarmManager.getExternalAlarmCatalogURL();
	}
	
	/**
	 * @return the list of external catalog URLs
	*/ 
	public List getStatEventCatalogURLs(){
		return this.eventManager.getExternalStatEventCatalogURLs();
	}
	
	/**
	 * Creates a new alarm in the specified catalog.
	 * @param alarm the OSP alarm to create
	 * @param catalog the catalog in which we must create the new alarm
	 */
	public void createAlarmInCatalog(Alarm alarm, AlarmsCatalog catalog) {
		this.alarmManager.createAlarmInCatalog(alarm, catalog);
	}
	
	/**
	 * Writes the alarm catalog xml tree in an xml file, and creates it if it's needed.
	 * @param catalog
	 */
	public void createAlarmCatalog(AlarmsCatalog catalog){
		this.alarmManager.createCatalog(catalog);
	}
	
	/**
	 * Writes the stat catalog xml tree in an xml file, and creates it if it's needed.
	 * @param catalog
	 */
	public void createStatEventCatalog(EventCatalog catalog){
		this.eventManager.createNewEventStat( catalog);
	}

	/**
	 * @param selected_stat the list of stat ID (OSP-flavoured)
	 * @return the list of stat events corresponding to the ID cotnained in the list.
	 */
	public List extractStatEventFromId(List selected_stat) {
		return this.eventManager.extractStatEventFromId(selected_stat, getAllStatEventCatalogs() );
	}

	/**
	 * Extract the corresponding alarm.
	 * @param id the OSP id of the Alarm : "osp.\<catalogSubLocation\>.alarm number
	 */
	public Alarm extractAlarmFromId(String id) {
		return this.alarmManager.extractAlarmFromId(id, getAllAlarmCatalogs());
		
	}
	
	/**
	 * Loads the keystore parameters in the keystore manager
	 * @param location The keystore location*
	 * @param alias The alias to sign under
	 * @param storePass Password for keystore integrity
	 * @param keyStoreKeyPass password for private key (if different)
	 */
	public void setKeystoreAttribute(File location, String alias, String storePass, String keyStoreKeyPass){
		this.keystoreManager.setKeystoreLocation(location);
		this.keystoreManager.setKeyPass(keyStoreKeyPass);
		this.keystoreManager.setAlias(alias);
		this.keystoreManager.setStorePass(storePass);
	}
	
	/**
	 * @return the keystore location set in the keystore manager
	 */
	public File getKeyStoreAttributeLocation(){
		return this.keystoreManager.getKeystoreLocation();
	}
	
	/**
	 * @return the keystore storepass  set in the keystore manager
	 */
	public String getKeyStoreAttributeStorePass(){
		return this.keystoreManager.getStorePass();
	}
	
	/**
	 * @return the keystore alias  set in the keystore manager
	 */
	public String getKeyStoreAttributeAlias(){
		return this.keystoreManager.getAlias();
	}
	
	/**
	 * @return the keystore alias  set in the keystore manager
	 */
	public String getKeyStoreAttributeKeyPass(){
		return this.keystoreManager.getKeyPass();
	}
	
	
	
	///////////////////////////////////////////
	//
	// J2EE generation
	//
	//////////////////////////////////////////
	
	
	///////////////////////////////////////////
	//
	// Jad bridge
	//
	//////////////////////////////////////////
	
	/**
	 * @return the jad bridge instance.
	 */
	public IDecompiler getDecompilerBridge(){
		return jadBrigde;
	}
	
	///////////////////////////////////////////
	//
	// Graph Drawing
	//
	//////////////////////////////////////////
	
	/**
	 * Build the graphical representation of this sbb as a comound directed graph.
	 * @param sbbXML the sbb to represent
	 * @param projectName the project name
	 * @param drawing the drawing in which we will draw the graph
	 * @return the new drawing
	 */
	public void buildSbbCompoundGraphFigure(SbbXML sbbXML, Figure figure, String projectName) {
		this.graphicalManager.drawCompoundSbbgraph(sbbXML, figure, projectName);
		
	}

	/**
	 * @return the current drawing
	 */
	public IFigure getCurrentDrawing(){
		IFigure current =  this.graphicalManager.getCurrentDrawing();
		if(current == null){
			return new Figure();
		}else{
			return current;
		}
	}

	/**
	 * @return Returns the graphicalManager.
	 */
	public GraphicalManager getSelectionListener() {
		return graphicalManager;
	}

	/**
	 * List all nodes which intersect the selection area.
	 * @param children the list of figure contained in the drawing
	 * @param bounds the selection  area bound
	 * @return the list of nodes.
	 */
	public List getNodeInstersected(List children, Rectangle bounds) {
		return this.graphicalManager.getNodeInstersected(children, bounds);
	}

	/**
	 * @param selectedNodes the list of nodes for which we are looking for figures.
	 * @return the list of figure corresponding to the list of nodes
	 */
	public List getFiguresFromNodes(List selectedNodes) {
		return this.graphicalManager.getFiguresFromNodes(selectedNodes);
	}

	/**
	 * @return Returns the paddingManager.
	 */
	public PaddingManager getPaddingManager() {
		return paddingManager;
	}

	/**
	 * @param paddingManager The paddingManager to set.
	 */
	public void setPaddingManager(PaddingManager paddingManager) {
		this.paddingManager = paddingManager;
	}

	/**
	 * Set the padding for a state editor client.
	 */
	public void setPaddingAttribute(int top, int bottom, int left, int right) {
		this.paddingManager.setBottom(bottom);
		this.paddingManager.setTop(top);
		this.paddingManager.setLeft(left);
		this.paddingManager.setRigth(right);
	}
	
}
