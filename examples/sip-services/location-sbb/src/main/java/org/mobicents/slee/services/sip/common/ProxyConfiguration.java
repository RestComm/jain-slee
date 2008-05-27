package org.mobicents.slee.services.sip.common;

public interface ProxyConfiguration {

	public static final String MBEAN_NAME_PREFIX="slee:sipproxyconfigurator=";
	/**
	 * List of supported uri schemes by this proxy/ accepted uri schemes
	 * @return
	 */
	public String[] getSupportedURISchemes();
	/**
	 * List of domains for which this proxy should act.
	 * @return
	 */
	public String[] getLocalDomainNames();
	/**
     * This is only called to fill in the via header.
     * 
     * @return the sipHostName by local host lookup or by consulting
     * the load balancer.
     * 
     */
    public String getSipHostname();
    /**
     * Port for communication
     * @return
     */
    public int getSipPort();
    
    /**
     * Transport used by this proxy
     * @return
     */
    public String[] getSipTransports();

    

}
