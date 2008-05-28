package org.mobicents.csapi.jr.slee.termcap;

/**
 * The Terminal Capabilities SCF interface IpTerminalCapabilities contains the synchronous method getTerminalCapabilities. The application has to provide the terminaIdentity as input to this method. The result indicates whether or not the terminal capabilities are available in the network and, in case they are, it will return the terminal capabilities (see the data definition of TpTerminalCapabilities for more information).  The network may override some capabilities that have been indicated by  the terminal itself due to network policies or other restrictions or modifications in the supported capabilities.																									This interface, or IpExtendedTerminalCapabilities shall be implemented by a Terminal Capabilities SCF as a minimum requirement.  If this interface is implemented, the getTerminalCapabilities()method shall be implemented as a minimum requirement.
 *
 * 
 * 
 */
public interface IpTerminalCapabilitiesConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method is used by an application to get the capabilities of a user's terminal. Direction: Application to Network.
@return result : Specifies the latest available capabilities of the user's terminal.
This information, if available, is returned as CC/PP headers as specified in W3C (see [6] in ES 202 915-1) and adopted in the WAP UAProf specification (see [9] in ES 202 915-1). It contains URLs; terminal attributes and values, in RDF format; or a combination of both. 
     *     @param terminalIdentity Identifies the terminal. It may be a logical address known by the WAP Gateway/PushProxy.

     */
    org.csapi.termcap.TpTerminalCapabilities getTerminalCapabilities(String terminalIdentity) throws org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID,javax.slee.resource.ResourceException;


} // IpTerminalCapabilitiesConnection

