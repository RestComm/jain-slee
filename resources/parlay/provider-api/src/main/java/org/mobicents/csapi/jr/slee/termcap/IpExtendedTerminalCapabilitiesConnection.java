package org.mobicents.csapi.jr.slee.termcap;

/**
 * This interface can be used as an extended version of terminal capability monitoring.  The application programmer can use this interface to request terminal capability reports that are triggered by their changes. Note that the underlying mechanisms for this network feature are currently not fully standardised.													This interface, or IpTerminalCapabilities, shall be implemented by a Terminal Capabilities SCF as a minimum requirement.  The triggeredTerminalCapabilityStartReq() and triggeredTerminalCapabilityStop() methods shall be implemented as a minimum requirement. An implementation of IpExtendedTerminalCapabilities is not required to implement the minimum mandatory methods of IpTerminalCapabilities.
 *
 * 
 * 
 */
public interface IpExtendedTerminalCapabilitiesConnection extends org.mobicents.csapi.jr.slee.termcap.IpTerminalCapabilitiesConnection{


    /**
     *     Request for terminal capability reports when the capabilities change or when the application obviously does not have the current terminal capability information when this method is invoked. 
@return  assignmentID 
Specifies the assignment ID of the triggered terminal capability reporting request. 
     *     @param terminals Specifies the terminal(s) for which the capabilities shall be reported. TpAddress fields have the following use:
·	Plan: Used to indicate the numbering plan
·	AddrString: Used to indicate the subscriber address
·	Name: Used to indicate the terminal identity. May be applied also together with AddrString to indicate subscriber's particular terminal. The precise format is not defined.
·	Presentation: No defined use
·	Screening: No defined use
·	SubAddressString: No defined use
Hence it is possible to indicate the subscriber and/or the terminal identification. This terminal addressing is implementation specific e.g. subscriber identification may not always be sufficient information to get the capabilities of the terminal. 
    @param capabilityScope Specifies the scope of the capabilities that the application is interested in. The contents are implementation specific. One possibility is to use the CC/PP definitions as in TpTerminalCapabilities.
    @param criteria Specifies the trigger conditions for the reports e.g. software or hardware update.

     */
    int triggeredTerminalCapabilityStartReq(org.csapi.TpAddress[] terminals,org.csapi.termcap.TpTerminalCapabilityScope capabilityScope,int criteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.P_INVALID_CRITERIA,org.csapi.termcap.P_INVALID_TERMINAL_ID,javax.slee.resource.ResourceException;


    /**
     *     Stop reporting for terminal capability changes that were started by triggeredTerminalCapabilityStartReq().
     *     @param assignmentID Specifies the assignment ID for the task to be stopped.

     */
    void triggeredTerminalCapabilityStop(int assignmentID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ASSIGNMENT_ID,javax.slee.resource.ResourceException;


} // IpExtendedTerminalCapabilitiesConnection

