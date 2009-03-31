/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.TCKTestErrorException;

/**
 * Defines the TCK activity's interface to the TCK resource.
 */
public interface TCKResourceActivityInterface extends Remote {

    public void endActivity(TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

    public boolean isLive(TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

    public void sendSbbMessage(Object payload, TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

}