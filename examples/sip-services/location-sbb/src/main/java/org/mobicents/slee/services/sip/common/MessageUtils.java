package org.mobicents.slee.services.sip.common;

import javax.sip.address.Address;
import javax.sip.address.URI;
import javax.sip.header.HeaderAddress;

public class MessageUtils {

	
	
	protected ProxyConfiguration config=null;
	public MessageUtils(ProxyConfiguration config)
	{
		this.config=config;
	}
	/**
     * returns string in form "sip:user@domain"
     */
   public static String getCanonicalAddress(HeaderAddress header) {
        Address na = header.getAddress();

        URI uri = na.getURI();

        String addr = uri.toString();

        // the URI may contain sip:user@host:port;transport=etc etc
        // we want everything upto and including the host
        int index = addr.indexOf(':');
        index = addr.indexOf(':', index+1);
        if (index != -1) {
            // Strip off port and any optional tags following the host part of the URI
            addr = addr.substring(0, index);
        }

        return addr;
    }

    public boolean isLocalDomain(URI uri) {
        //logger.fine("isLocalDomain");
        // Is the request-uri for this domain
        // eg. sip:opencloud.com

        boolean belongs = false;
        String uriDomain = getDomain(uri);
        String[] localDomainNames = config.getLocalDomainNames();
        for (int i = 0; i < localDomainNames.length; i++ ) {
            belongs = belongsToDomain(uriDomain, localDomainNames[i]);
            if (belongs) break;
        }

        return belongs;
    }

    private boolean belongsToDomain(String uriDomain, String domainSpec) {
        switch (domainSpec.indexOf("*.")) {
           // Case 1, the configured local domain contains no wildcards,  e.g. simply 'opencloud.com'
           case -1: return domainSpec.equalsIgnoreCase(uriDomain);
           // Case 2, the configured local domain contains a wildcard,  e.g. '*.opencloud.com'
           // only allow this at the start of the domain spec and only in the form *.domain
           case 0:  return uriDomain.toLowerCase().endsWith(domainSpec.substring(2).toLowerCase());
           // Ignore local domains specified in any other format
           default:
                //logger.fine("I don't understand the format of local domain "+domainSpec);
                return false;
        }
    }

    public String getDomain(URI uri) {
        String address = uri.toString();

        // strip off user part, if any. eg. ben@opencloud.com -> opencloud.com
        int index = address.indexOf('@');
        if (index != -1) address = address.substring(index+1);

        // Strip off protocol part (NIST SIP 1.1 getSchemeData() no longer available and toString() returns _whole_ URI!)
        // If there was a user part stripped above it will have been after the protocol part so we wouldn't expect to see a protocol part here
        index = address.indexOf(':');
        if (index != -1) address = address.substring(index+1);

        // Lastly possibly strip off the port and any subsequent tags
        // This is the fix for bugs caused by the REGISTER sip:host:5060;transport=UDP line seen with some SIP UA'a
        index = address.indexOf(':');
        if (index != -1) address = address.substring(0, index);

        return address;
    }

    public boolean isSupportedURIScheme(URI uri) {

        String uriScheme = uri.getScheme();
        String[] supportedURISchemes = config.getSupportedURISchemes();

        for (int i = 0; i < supportedURISchemes.length; i++ ) {
            if (supportedURISchemes[i].equalsIgnoreCase(uriScheme)) return true;
        }

        return false;
    }
	
	
}
