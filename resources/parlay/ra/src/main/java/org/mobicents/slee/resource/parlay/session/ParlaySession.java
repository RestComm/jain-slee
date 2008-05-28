package org.mobicents.slee.resource.parlay.session;

import java.util.Properties;

import javax.slee.resource.ResourceException;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;

/**
 * Stores all Parlay Session state.
 */
public interface ParlaySession {

    /**
     * Initialise Parlay Session. Will authenticate the RA with the Parlay
     * Gateway. A failure to authenticate will result in an exception being
     * thrown and the session will be invalid.
     * 
     * @throws FwSessionException
     */
    void init() throws FwSessionException;

    /**
     * Destroys the Parlay Session. endAccess() will be called on the Parlay
     * Gateway. Once this is called no further communication will be possible
     * with the Gateway via this session.
     */
    void destroy();

    /**
     * Returns an identifier for a service matching the specified name and
     * service properties.
     * 
     * @param serviceTypeName
     *            The Parlay service type name e.g. P_USER_INTERACTION.
     * @param serviceProperties
     *            The Parlay service properties in (Name;Value) pairs. For
     *            parameters with a set of values use a comma separated list.
     * @return an identifier for the service.
     * @throws ResourceException
     */
    TpServiceIdentifier getService(String serviceTypeName,
            Properties serviceProperties) throws FwSessionException, ResourceException;

    /**
     * Gets the session for the specified service identifier.
     * 
     * @param serviceIdentifier
     * @return
     */
    ServiceSession getServiceSession(TpServiceIdentifier serviceIdentifier);

    
    /**
     * @param mpccsListener The mpccsListener to set.
     */
    void setMpccsListener(MpccsListener mpccsListener);
    
    /**
     * @param gccsListener The gccsListener to set.
     */
    void setGccsListener(GccsListener gccsListener);
    
    /**
     * @param uiListener The uiListener to set.
     */
    void setUiListener(UiListener uiListener);
}