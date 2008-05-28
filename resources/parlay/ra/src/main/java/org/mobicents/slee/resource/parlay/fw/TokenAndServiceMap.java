package org.mobicents.slee.resource.parlay.fw;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.csapi.IpService;

/**
 * This class stores service interfaces and service tokens as a dual look-up.
 * 
 * The token:service relationship must be a unique key.
 */
public class TokenAndServiceMap {

    /**
     * Holds references to services and their token identifier.
     */
    public TokenAndServiceMap() {
        tokenToIpServiceMap = new HashMap();
        serviceToTokenMap = new HashMap();
    }

    // CONSTANTS
    // .......................................................
    private static final String lineSeparator = System
            .getProperty("line.separator");

    // VARIABLES
    // .......................................................
    private Map tokenToIpServiceMap = null;

    private Map serviceToTokenMap = null;

    /**
     * Stores the specified service token and service interface cross reference.
     * 
     * @param serviceToken
     *            the service token
     * @param service
     *            the service
     */
    public synchronized void put(String serviceToken, IpService service) {
        tokenToIpServiceMap.put(serviceToken, service);
        serviceToTokenMap.put(service, serviceToken);
    }

    /**
     * Stores the specified service token and service interface cross reference.
     * 
     * @param service
     *            the service
     * @param serviceToken
     *            the service token
     */
    public synchronized void put(IpService service, String serviceToken) {
        tokenToIpServiceMap.put(serviceToken, service);
        serviceToTokenMap.put(service, serviceToken);
    }

    /**
     * Retrieves the service corresponding to the specified token.
     * 
     * @param serviceToken
     *            the service token
     * @return the service
     */
    public synchronized IpService get(String serviceToken) {
        return (IpService) tokenToIpServiceMap.get(serviceToken);
    }

    /**
     * Retrieves the token corresponding to the specified service.
     * 
     * @param service
     *            the service
     * @return the service token
     */
    public synchronized String get(IpService service) {
        return (String) serviceToTokenMap.get(service);
    }

    /**
     * Removes the lookup corresponding to the specified token.
     * 
     * @param serviceToken
     *            the service token
     * @return the service
     */
    public synchronized IpService remove(String serviceToken) {
        IpService result = null;

        result = (IpService) tokenToIpServiceMap.remove(serviceToken);

        serviceToTokenMap.remove(result);

        return result;
    }

    /**
     * Removes the lookup corresponding to the specified service.
     * 
     * @param service
     *            the service
     * @return the service token
     */
    public synchronized String remove(IpService service) {
        String result = null;

        result = (String) serviceToTokenMap.remove(service);

        tokenToIpServiceMap.remove(result);

        return result;
    }

    /**
     * Method tokens.
     * 
     * @return String[]
     */
    public String[] tokens() {
        Set tokenSet = tokenToIpServiceMap.keySet();
        String[] result = (String[]) tokenSet.toArray(new String[tokenSet
                .size()]);

        return result;
    }

    /**
     * Removes all mappings from this map.
     */
    public synchronized void clear() {
        tokenToIpServiceMap.clear();
        serviceToTokenMap.clear();
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
        StringBuffer value = new StringBuffer("TokenAndServiceMap");
        value.append(lineSeparator);
        value.append("Size =");
        value.append(tokenToIpServiceMap.size());
        value.append(lineSeparator);

        return value.toString();
    }
}

