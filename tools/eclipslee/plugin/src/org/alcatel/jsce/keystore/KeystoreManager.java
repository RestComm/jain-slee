
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
package org.alcatel.jsce.keystore;

import java.io.File;

/**
 *  Description:
 * <p>
 * This class manages the keystore parameters settings from the preference store and 
 * the prefrence page of Eclipse.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class KeystoreManager {
	/** The keystore location*/
	private File keystoreLocation = null;
	/** The alias to sign under*/
	private String alias = "";
	/** Password for keystore integrity*/
	private String storePass = "";
	/** Password for private key (if different)*/
	private String keyPass = "";
	/** Defeines wheter the keysotre is set or not*/
	private boolean set = false;

	/**
	 * 
	 */
	public KeystoreManager() {
	}
	
	///////////////////////////////////////////
	//
	// Access methods
	//
	//////////////////////////////////////////
	
	public String getStorePass() {
		return storePass;
	}

	public void setStorePass(String storePass) {
		this.storePass = storePass;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public File getKeystoreLocation() {
		return keystoreLocation;
	}

	public void setKeystoreLocation(File keystoreLocation) {
		if(keystoreLocation!=null){
			this.keystoreLocation = keystoreLocation;
			this.setSet(true);
		}else{
			this.setSet(false);
		}
	}
	
	public String getKeyPass() {
		return keyPass;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}
	
	

}
