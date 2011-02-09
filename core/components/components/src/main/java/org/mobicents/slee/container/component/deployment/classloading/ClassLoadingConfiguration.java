/**
 * 
 */
package org.mobicents.slee.container.component.deployment.classloading;

/**
 * @author martins
 *
 */
public class ClassLoadingConfiguration {
	
	private boolean loadClassesFirstFromAS = true;
	
	/**
	 * @return
	 */
	public boolean isLoadClassesFirstFromAS() {
		return loadClassesFirstFromAS;
	}

	/**
	 * 
	 * @param value
	 */
	public void setLoadClassesFirstFromAS(boolean value) {
		this.loadClassesFirstFromAS = value;
	}
}
