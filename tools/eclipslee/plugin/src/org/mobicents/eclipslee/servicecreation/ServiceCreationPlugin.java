/**
 *   Copyright 2005 Open Cloud Ltd.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.alcatel.jsce.backend.MainControl;
import org.alcatel.jsce.interfaces.com.ICommunication;
import org.alcatel.jsce.servicecreation.graph.component.figure.ColorManager;
import org.alcatel.jsce.util.image.ImageManager;
import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ServiceCreationPlugin extends AbstractUIPlugin implements ICommunication {
	
	//The shared instance.
	private static ServiceCreationPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/** Back-end main handler*/
	private MainControl mainControl = null;
	
	/** Manages all the preference store*/
	private PreferenceStoreManager storeManager = null;
	
	public static final String NATURE_ID = "org.mobicents.eclipslee.servicecreation.sleenature";
	public static final String OSP_NATURE_ID = "org.alcatel.jsce.servicecreation.jospnature";

	/**
	 * The constructor.
	 */
	public ServiceCreationPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.mobicents.eclipslee.ServiceCreationPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		SCELogger.logInfo("Starting SCE-SE plug-in");
		startupBackEnd();
		checkForSleeJars();
	}

	/**
	 * Start the non-dependent Eclipse code.
	 * ForOSP SCE code.
	 */
	private void startupBackEnd() {
		/*1. Create and initialize back-end */
		mainControl = new MainControl();
		/*2. Create the prefrence store and load prefrences*/
		storeManager = new PreferenceStoreManager(mainControl);
		/*3. Register the graphical manager as Iresource change listener*/
		ResourcesPlugin.getWorkspace().addResourceChangeListener(mainControl.getSelectionListener());
		
		
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		/*Cleaning the images cache*/
		ImageManager.getInstance().disposeImages();
		/*Clearing fonts on the manager*/
		getMainControl().getSelectionListener().dispose();
		/*Clearing the color cache*/
		ColorManager.getInstance().dispose();
		/*Removing resource change listener*/
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(mainControl.getSelectionListener());
		
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static ServiceCreationPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ServiceCreationPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static void log(String message) {
		
		if (logEnabled == false) return;
		
		if (logStream == null) {
			File logFile = new File("/tmp/eclipse.log");
			try {
				logFile.createNewFile();
				logStream = new FileOutputStream(logFile);
			} catch (FileNotFoundException e) {
				return;
			} catch (IOException e) {
				return;
			}
		}
		
		try {
			logStream.write(message.getBytes());
			logStream.write('\n');
			logStream.flush();
		} catch (IOException e) {
			return;
		}
	}	
	public static void log(Throwable e) {
		
		if (logEnabled == false) return;
		
		String message = e.getClass().getName() + ": " + e.getMessage();
		StackTraceElement trace[] = e.getStackTrace();
		for (int i = 0; i < trace.length; i++)
			message += "\t" + trace[i] + "\n";

		if (e.getCause() != null) {
			message += "Nested:\n";			
			log(message);
			log(e.getCause());
		} else
			log(message);	
	}

	public static ZipFile getSleeAPI_1_1ZipFile() throws IOException {
        URL url = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("lib/" + APIDialog.SLEE_API_ZIP_1_1), null);
        URL furl = FileLocator.toFileURL(url);
        ZipFile zipFile = new ZipFile(furl.getPath());
        return zipFile;
	}
		
	public InputStream getSleeJar() {
		try {
			// Was the SLEE jar packaged with the plug-in?
            ZipFile zipFile = getSleeAPI_1_1ZipFile();
            ZipEntry sleeJar = zipFile.getEntry("lib/" + APIDialog.SLEE_JAR);
            if (sleeJar == null) {          // SLEE 1.0 stores slee.jar in root, not lib
                sleeJar = zipFile.getEntry(APIDialog.SLEE_JAR);
            }
            InputStream is = null;
            if (sleeJar != null) {
                is = zipFile.getInputStream(sleeJar);
            }
			if (is != null)
				return is;
		} catch (IOException e) {
		}
		
		try {
			// Was it added by the user afterwards via the dialog?
			File file = this.getStateLocation().append(APIDialog.SLEE_JAR).toFile();
			if (file.exists())
				return new FileInputStream(file);
		} catch (IOException e) {		
		}
		return null;	
	}
	
	/**
	 * @return an handle on the /lib/jslee-1_1-fr-api.zip of the plug-in.
	 */
	public InputStream getSleeAPIZip() {
		try {
			// Was the SLEE API jar packaged with the plug-in?
			//InputStream is = ServiceCreationPlugin.getDefault().openStream(new Path("lib/" + APIDialog.SLEE_API_ZIP));
			InputStream is = FileLocator.openStream(ServiceCreationPlugin.getDefault().getBundle(), new Path("lib/" + APIDialog.SLEE_API_ZIP), false);
			if (is != null)
				return is;
		} catch (IOException e) {
		}
		
		try {			
			// Was it added by the user afterwards by the dialog?
			File file = this.getStateLocation().append(APIDialog.SLEE_API_ZIP).toFile();		
			if (file.exists())
				return new FileInputStream(file);
		} catch (IOException e) {
		}
		return null;	
	}
	
	/**
	 * @return an handle on the /lib/sleedtd.jar (the strictly restricted standard files) of the plug-in.
	 */
//	public InputStream getsleeDTD() {
//		try {
//			//InputStream is = ServiceCreationPlugin.getDefault().openStream(new Path("lib/" + "sleedtd.jar"));
//			InputStream is = FileLocator.openStream( ServiceCreationPlugin.getDefault().getBundle(), new Path("lib/" + "sleedtd.jar"), false);
//			if (is != null)
//				return is;
//		} catch (IOException e) {
//		}
//		
//		try {			
//			// Was it added by the user afterwards by the dialog?
//			File file = this.getStateLocation().append("sleedtd.jar").toFile();		
//			if (file.exists())
//				return new FileInputStream(file);
//		} catch (IOException e) {
//		}
//		return null;	
//	}
	
	private void checkForSleeJars() {
		InputStream is = getSleeAPIZip();
		if (is == null) {
			// Present a dialog to the user telling them about the SLEE API
			// jar, where they may download it from, and to tell the plug-in
			// where it is.				
			APIDialog dialog = new APIDialog(new Shell());			
			if (dialog.open() != Window.OK) {
				// Nag message.
				MessageDialog.openError(new Shell(), "Warning", "You did not provide the location of a valid JAIN SLEE API zip file.  The Plug-In will continue to run, but with reduced functionality." +
						"\n\n" +
						"You will be prompted for the location of this zip file next time Eclipse is restarted.");

			}	
		}
	}
	
	private static final boolean logEnabled = false;
	private static FileOutputStream logStream = null;
	
	///////////////////////////////////////////
	//
	// OSP JSCE utilities
	//
	//////////////////////////////////////////
	
	/**
	 * Add an alarm catalog location.
	 * @param catalogLocation The url of the new alarm catalog location.
	 */
	public void addAlarmCatalogURL(URL catalogLocation) {
		if (catalogLocation != null) {
			/* 1. Set the location in the alarm manager */
			getMainControl()	.addAlarmCatalogURL(catalogLocation);
			/* 2. Store the location in preferences */
			getStoreManager().storeValue(PreferenceStoreManager.EXTERNAL_ALARM_CAT_URL,
					catalogLocation.getFile());
		} else {
			getMainControl().getAlarmCatalogURLs().clear();
			/* Remove the location from the preference store */
			getStoreManager().storeValue(	PreferenceStoreManager.EXTERNAL_ALARM_CAT_URL, "");
		}
	}
	
	/**
	 * Add an Stat StatEvent catalog location.
	 * @param statEventDirURL The url of the new stat event catalog location.
	 */
	public void addStatEventCatalogURL(URL statEventDirURL) {
		if (statEventDirURL != null) {
			/* 1. Set the location in the stat event manager */
			getMainControl().addStatEventCatalogURL(statEventDirURL);
			/* 2. Store the location in preferences */
			getStoreManager().storeValue(
					PreferenceStoreManager.EXTERNAL_STAT_EVENT_CAT_URL,
					statEventDirURL.getFile());
		} else {
			getMainControl().getStatEventCatalogURLs().clear();
			/* Remove the location from the preference store */
			getStoreManager().storeValue(
					PreferenceStoreManager.EXTERNAL_STAT_EVENT_CAT_URL, "");
		}
	}
	
	/**
	 * Loads the keystore parameters in the keystore manager
	 * @param location The keystore location*
	 * @param alias The alias to sign under
	 * @param storePass Password for keystore integrity
	 * @param keyStoreKeyPass password for private key (if different)
	 */
	public void setKeystoreAttribute(File location, String alias, String storePass, String KeyPass){
		if(location!=null){
			//1. Update the keystore manager
			getMainControl().setKeystoreAttribute(location, alias, storePass, KeyPass);
			//2. Update the store manager of Eclipse
			getStoreManager().storeValue(PreferenceStoreManager.KEYSTORE_URL, location.toString());
			getStoreManager().storeValue(PreferenceStoreManager.KEYSTORE_ALIAS, alias);
			getStoreManager().storeValue(PreferenceStoreManager.KEYSTORE_KEY_PASS, KeyPass);
			getStoreManager().storeValue(PreferenceStoreManager.KEYSTORE_STORE_PASS, storePass);
		}
	} 

	/**
	 * @return Returns the mainControl.
	 */
	public MainControl getMainControl() {
		return mainControl;
	}

	/**
	 * @param mainControl The mainControl to set.
	 */
	public void setMainControl(MainControl mainControl) {
		this.mainControl = mainControl;
	}

	/**
	 * @return Returns the storeManager.
	 */
	public PreferenceStoreManager getStoreManager() {
		return storeManager;
	}

	/**
	 * @param storeManager The storeManager to set.
	 */
	public void setStoreManager(PreferenceStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	/**
	 * @see org.alcatel.jsce.interfaces.com.ICommunication#sendMessageInfo(java.lang.String)
	 */
	public void sendMessageInfo(String msg) {
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "SCE-SE message", msg);
		
	}

	/**
	 * @see org.alcatel.jsce.interfaces.com.ICommunication#sendMessageError(java.lang.String)
	 */
	public void sendMessageError(String msg) {
		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "SCE-SE message", msg);
		
	}

	public void loadPadding(Insets padding) {
		//		1. Update the keystore manager
		getMainControl().setPaddingAttribute(padding.top,padding.bottom, padding.left, padding.left);
		//2. Update the store manager of Eclipse
		getStoreManager().storeValue(PreferenceStoreManager.PADDING_BOTTOM, padding.bottom);
		getStoreManager().storeValue(PreferenceStoreManager.PADDING_TOP , padding.top);
		getStoreManager().storeValue(PreferenceStoreManager.PADDING_RIGHT, padding.right);
		getStoreManager().storeValue(PreferenceStoreManager.PADDING_LEFT, padding.left);
	}

}
