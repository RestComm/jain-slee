/**
 * 
 */
package org.mobicents.slee.resources.ss7.isup.events;

import java.io.Serializable;

/**
 * @author Oifa Yulian
 *
 */
public class ResetEvent implements Serializable {

	private int dpc;
	private int cic;
	
	public ResetEvent(int CIC,int DPC) {
		super();
		this.cic = CIC;
		this.dpc = DPC;
	}
	/**
	 * @return the dpc
	 */
	public int getDPC() {
		return dpc;
	}
	/**
	 * @return the cic
	 */
	public int getCIC() {
		return cic;
	}
}
