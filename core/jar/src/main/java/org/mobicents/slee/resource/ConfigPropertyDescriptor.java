/*
 * ConfigProperty.java
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */ 

package org.mobicents.slee.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Represents configuration property element of the resource adaptor descriptor.
 *
 * @author Oleg Kulikov
 */
public class ConfigPropertyDescriptor {
    
    /**
     * This element identifies the name of the JavaBean configuration property 
     * used by the resource adaptor.
     */
    private String name;
    
    /**
     * This element identifies the type of the JavaBean configuration property 
     * used by the resource adaptor.
     */
    private String type;
    
    /**
     * This element identifies the default value of the configuration property 
     * used by the resource adaptor.
     */
    private Object value;
    
    /** 
     * Creates a new instance of ConfigProperty 
     *
     * @param name the name of the JavaBean configuration property
     * @param type the type of the JavaBean configuration property
     * @param value the default value of the configuration property
     */
    public ConfigPropertyDescriptor(String name, String type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    /**
     * Gets the name of the configuration property.
     *
     * @return the name of the configuration property.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the type of the configuration property.
     *
     * @return the fully qualified class name of the configuration property.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the default value of the configuration property.
     * 
     * @return the value of the configuration property.
     */
    public Object getValue() {
        return value;
    }
    
    public String toString() {
        return "config-property {name=" + name + ", class=" + type + 
                ", default value" + value + "}";
    }
}
