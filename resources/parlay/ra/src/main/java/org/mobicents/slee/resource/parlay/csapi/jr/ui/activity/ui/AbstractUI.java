package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui;

import javax.slee.resource.ActivityHandle;

import org.csapi.ui.IpAppUIOperations;
import org.csapi.ui.IpUI;
import org.mobicents.csapi.jr.slee.ui.IpUIConnection;

public interface AbstractUI extends IpUIConnection, IpAppUIOperations {
    /**
     * Initialises the internal resources.
     */
    void init();

    /**
     * Destroys or deallocates all resources used.
     */
    void dispose();

    /**
     * @return 
     */
    ActivityHandle getActivityHandle();

    /**
     * @return Parlay Gateway CORBA IOR e.g. IpUI or IpUICall
     */
    IpUI getIpUI();
}
