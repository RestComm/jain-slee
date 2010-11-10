
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
package org.mobicents.eclipslee.servicecreation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.alcatel.jsce.backend.MainControl;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 *  Description:
 * <p>
 * This object manages any kind of access to the preference store of Eclispe.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class PreferenceStoreManager {
	  private IPreferenceStore preferenceStore;
	  private ServiceCreationPlugin creationPlugin;
	  public static String EXTERNAL_ALARM_CAT_URL = "org.alcatel.jsce.external.alarm";
	  public static String EXTERNAL_STAT_EVENT_CAT_URL = "org.alcatel.jsce.external.stat.event";
	  public static String KEYSTORE_URL = "org.alcatel.jsce.external.keystore";
	  public static String KEYSTORE_ALIAS = "org.alcatel.jsce.external.keystore.alias";
	  public static String KEYSTORE_STORE_PASS= "org.alcatel.jsce.external.keystore.storepass";
	  public static String KEYSTORE_KEY_PASS= "org.alcatel.jsce.external.keystore.keypass";
	  public static String PADDING_TOP= "org.alcatel.jsce.external.client.state.editor.padding.top";
	  public static String PADDING_BOTTOM= "org.alcatel.jsce.external.client.state.editor.padding.bottom";
	  public static String PADDING_LEFT= "org.alcatel.jsce.external.client.state.editor.padding.left";
	  public static String PADDING_RIGHT= "org.alcatel.jsce.external.client.state.editor.padding.right";
	  
	    

	/**
	 * Constructor.
	 * @param mainControl the main back-end handler.
	 */
	public PreferenceStoreManager(MainControl mainControl) {
		/*Opens the preference store and loads them via the main control*/
		String alarmExternalCat = ServiceCreationPlugin.getDefault().getPluginPreferences().getString(PreferenceStoreManager.EXTERNAL_ALARM_CAT_URL);
		if(alarmExternalCat.length()>0){
			File file = new File(alarmExternalCat);
			if(file.isDirectory()){
				/*The string represents a valid file*/
				try {
					URL externaLAlarmCatURL = file.toURL();
					mainControl.addAlarmCatalogURL(externaLAlarmCatURL);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		/*Opens the stat event catalog in preferences*/
		String statEventCatalog = ServiceCreationPlugin.getDefault().getPluginPreferences().getString(PreferenceStoreManager.EXTERNAL_STAT_EVENT_CAT_URL);
		if(statEventCatalog.length()>0){
			File file = new File(statEventCatalog);
			if(file.isDirectory()){
				/*The string represents a valid file*/
				try {
					URL externalStatEventCatURL = file.toURL();
					mainControl.addStatEventCatalogURL(externalStatEventCatURL);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		/*Opens the keystore location*/
		String keystoreLocation = ServiceCreationPlugin.getDefault().getPluginPreferences().getString(PreferenceStoreManager.KEYSTORE_URL);
		if(keystoreLocation.length()>0){
			File keyStorefile = new File(keystoreLocation);
			if(keyStorefile.isFile()){
				//The string is valid
				String keyStoreAlias = ServiceCreationPlugin.getDefault().getPluginPreferences().getString((PreferenceStoreManager.KEYSTORE_ALIAS));
				String keyStorePass = ServiceCreationPlugin.getDefault().getPluginPreferences().getString(PreferenceStoreManager.KEYSTORE_STORE_PASS);
				String keyStoreKeyPass = ServiceCreationPlugin.getDefault().getPluginPreferences().getString(PreferenceStoreManager.KEYSTORE_KEY_PASS);
				mainControl.setKeystoreAttribute(keyStorefile, keyStoreAlias, keyStorePass, keyStoreKeyPass);
			}
		}else{
			//no key store defined
			mainControl.setKeystoreAttribute(null, "", "", "");
		}
		
		/*Open the padding data*/
		int top = ServiceCreationPlugin.getDefault().getPluginPreferences().getInt((PreferenceStoreManager.PADDING_TOP));
		int bottom = ServiceCreationPlugin.getDefault().getPluginPreferences().getInt(PreferenceStoreManager.PADDING_BOTTOM);
		int left = ServiceCreationPlugin.getDefault().getPluginPreferences().getInt(PreferenceStoreManager.PADDING_LEFT);
		int right = ServiceCreationPlugin.getDefault().getPluginPreferences().getInt(PreferenceStoreManager.PADDING_RIGHT);
		mainControl.setPaddingAttribute(top, bottom, left, right);
	}
	
	/**
	 * Store the value according to the correpsonding key.
	 * @param key
	 * @param value
	 */
	public void storeValue(String key, String value){
		ServiceCreationPlugin.getDefault().getPluginPreferences().setValue(key, value);
	}
	
	/**
	 * Store the value according to the correpsonding key.
	 * @param key
	 * @param value
	 */
	public void storeValue(String key, int value){
		ServiceCreationPlugin.getDefault().getPluginPreferences().setValue(key, value);
	}

}
