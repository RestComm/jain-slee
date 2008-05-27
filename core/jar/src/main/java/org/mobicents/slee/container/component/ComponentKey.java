/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 
 * Creates the keys used inside the slee to identify objects
 * 
 * @author F.Moggia
 * @author Ivelin Ivanov
 * 
 * 
 */
public class ComponentKey implements Serializable, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8786296602099616559L;

	private String name;

    private String version;

    private String vendor;

    private String key;
    
    private static final String SEPARATOR = "#";
    
    /**
     * The pattern used to replace / with # because / is a special separator in the cache directory
     */
    private Pattern separatorPtn =  Pattern.compile("/");

    /**
     * Creates a new instance of ComponentKey the component key is computed
     * 
     *  
     */
    public ComponentKey(String name, String vendor, String version) {
        this.name = name;
        this.version = version;
        this.vendor = vendor;
        StringBuffer sbuff = new StringBuffer();
        sbuff.append(name).append(SEPARATOR).append(vendor).append(SEPARATOR).append(
                version);
        key = sbuff.toString();
    }
    
    public ComponentKey ( String text) {
    	String normalizedKey = separatorPtn.matcher(text).replaceAll(SEPARATOR);
    	StringTokenizer st = new StringTokenizer(normalizedKey);
    	try { 
    	    name = st.nextToken(SEPARATOR);
    	    vendor = st.nextToken(SEPARATOR);
    	    version = st.nextToken();
    	} catch ( NoSuchElementException ex ) {
    	    this.name  = "";
    	    this.vendor = "";
    	    this.version = "";
    	}
    	this.key = normalizedKey;
    }

    public String toString() {
        return key;

    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the vendor.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     *            The vendor to set.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		return ((ComponentKey)obj).key.equals(this.key);
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Override the core hash code. 
     */
    public int hashCode() {
    	return this.key.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        return o != this ? key.compareTo(((ComponentKey) o).key) : 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

}
