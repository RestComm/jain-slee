package org.mobicents.slee.resource.parlay;

import javax.slee.resource.ResourceException;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 * 
 */
public interface ParlayResourceAdapterSbbInterface {

    ParlayConnection getParlayConnection() throws ResourceException;
}
