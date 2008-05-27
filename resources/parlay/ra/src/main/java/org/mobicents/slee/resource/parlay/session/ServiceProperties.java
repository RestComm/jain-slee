package org.mobicents.slee.resource.parlay.session;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.slee.resource.ResourceException;

import org.csapi.fw.TpServiceProperty;

/**
 * 
 * Class Description for ServiceProperties
 */
public class ServiceProperties {

    private static final String SERVICEP_PROPERTIES = "TpServiceProperty[]";

    private static final String CLOSE_BRACKET = "] :";

    private static final String VALUE = "Value [";

    private static final String PROPERTY_NAME = "Property Name :";

    private TpServiceProperty[] serviceProperties;

    private static final String lineSeparator = System
            .getProperty("line.separator");
    
    private ServiceProperties(TpServiceProperty[] properties) {
        serviceProperties = properties;
    }

    public static ServiceProperties load(Properties properties) throws ResourceException {

        if(properties == null) {
            throw new ResourceException("Null value not allowed for service properties");
        }
        
        // Get the specified properties list
        Enumeration propertyNames = properties.keys();

        // Vectorfor all props
        List propertiesVector = new Vector();

        // loop the list
        while (propertyNames.hasMoreElements()) {
            // Get the property name
            String propertyName = (String) propertyNames.nextElement();

            // Get the value(s)
            String[] values = properties.getProperty(propertyName).split(",");

            TpServiceProperty serviceProperty = new TpServiceProperty(
                    propertyName, values);

            propertiesVector.add(serviceProperty);
        }

        TpServiceProperty[] serviceProperties = (TpServiceProperty[]) propertiesVector
                .toArray(new TpServiceProperty[0]);
        
        return new ServiceProperties(serviceProperties);
    }

    /**
     * Defines a method used to provide the caller with a string representation
     * of the class.
     * 
     * @return This is an developer defined representation of the class object
     *         as a string
     *  
     */
    public String toString() {
        StringBuffer value = new StringBuffer(SERVICEP_PROPERTIES);
        value.append(lineSeparator);

        for (int i = 0; i < serviceProperties.length; i++) {
            value.append(PROPERTY_NAME);
            value.append(serviceProperties[i].ServicePropertyName);
            value.append(lineSeparator);

            String[] propertyValues = serviceProperties[i].ServicePropertyValueList;
            for (int j = 0; j < propertyValues.length; j++) {
                value.append(VALUE);
                value.append(j);
                value.append(CLOSE_BRACKET);
                value.append(propertyValues[j]);
                value.append(lineSeparator);
            }

        }
        value.append(lineSeparator);

        return value.toString();
    }

    /**
     * @return Returns the serviceProperties.
     */
    public TpServiceProperty[] getServiceProperties() {
        return serviceProperties;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        boolean result = true;

        ServiceProperties rhs = (ServiceProperties) obj;

        for (int i = 0; i < rhs.getServiceProperties().length; i++) {
            boolean matchFound = false;
            int match = 0;
            for (int j = 0; j < serviceProperties.length; j++) {
                if (rhs.getServiceProperties()[i].ServicePropertyName
                        .equals(serviceProperties[j].ServicePropertyName)) {
                    matchFound = true;
                    match = j;
                    break;
                }
            }
            if (matchFound) {
                result = assertEquals(
                        rhs.getServiceProperties()[i].ServicePropertyValueList,
                        serviceProperties[match].ServicePropertyValueList);
            }
            else {
                result = false;
                break;
            }
        }

        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return 1;
    }

    private static boolean assertEquals(String[] string1, String[] string2) {
        int i = 0;

        if (string1.length != string2.length) {
            return false;
        }

        while (i < string1.length) {
            if (!string1[i].equals(string2[i])) {
                return false;
            }

            i++;
        }
        return true;
    }
}